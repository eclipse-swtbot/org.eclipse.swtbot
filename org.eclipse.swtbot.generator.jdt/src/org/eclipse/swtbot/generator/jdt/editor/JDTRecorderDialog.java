/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Rastislav Wagner (Red Hat)
 *******************************************************************************/
package org.eclipse.swtbot.generator.jdt.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swtbot.generator.ui.BotGeneratorEventDispatcher;
import org.eclipse.swtbot.generator.ui.BotGeneratorEventDispatcher.CodeGenerationListener;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swtbot.generator.framework.GenerationRule;
import org.eclipse.swtbot.generator.framework.Generator;
import org.eclipse.swtbot.generator.framework.IRecorderDialog;
import org.eclipse.swtbot.generator.jdt.editor.document.ClassDocument;
import org.eclipse.swtbot.generator.jdt.editor.listener.AnnotationSelectionListener;
import org.eclipse.swtbot.generator.jdt.editor.listener.ClassAnnotationSelectionListener;
import org.eclipse.swtbot.generator.jdt.editor.listener.MethodSelectionListener;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.PreferenceConstants;

public class JDTRecorderDialog extends TitleAreaDialog implements IRecorderDialog{

	private BotGeneratorEventDispatcher recorder;
	private List<Generator> availableGenerators;
	private Map<CTabItem, SourceViewer> tabViewer;
	private Map<CTabItem, ToolBar> tabToolBar;
	private CTabFolder classTabFolder;
	private Button recordPauseButton;
	private MethodSelectionListener methodListener;
	private List<Shell> ignoredShells;
	public static final String ID = "org.eclipse.swtbot.generator.dialog.jdt";


	/**
	 * Create the dialog.
	 * 
	 */
	public JDTRecorderDialog() {
		super(null);
		ignoredShells = new ArrayList<Shell>();
		Shell recorderShell = new Shell(Display.getDefault(), SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE);
		ignoredShells.add(recorderShell);
		setParentShell(recorderShell);
		this.tabViewer = new HashMap<CTabItem, SourceViewer>();
		setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE | SWT.RESIZE | SWT.MAX);
		setBlockOnOpen(false);
		this.tabToolBar = new HashMap<CTabItem, ToolBar>();
	}
	
	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		this.getShell().setText("SWT Test Recorder");
		setTitle("Record test for SWT application");
		
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite generatorSelectionContainer = new Composite(container,SWT.NONE);
		generatorSelectionContainer.setLayout(new GridLayout(2, false));
		Label selectorLabel = new Label(generatorSelectionContainer, SWT.NONE);
		selectorLabel.setText("Target Bot API:");
		selectorLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,false));
		
		ComboViewer generatorSelectionCombo = new ComboViewer(generatorSelectionContainer);
		generatorSelectionCombo.setContentProvider(new ArrayContentProvider());
		generatorSelectionCombo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object o) {
				return ((Generator) o).getLabel();
			}
		});
		generatorSelectionCombo.setInput(this.availableGenerators);
		generatorSelectionCombo.setSelection(new StructuredSelection(this.recorder.getCurrentGenerator()));
		
		Image image  = this.recorder.getCurrentGenerator().getImage();
		if(image != null){
			image = new Image(Display.getDefault(), image.getImageData().scaledTo(80, 80));
			setTitleImage(image);
		} else {
			setTitleImage(null);
		}
		
		generatorSelectionCombo.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				Generator newGenerator = (Generator) ((IStructuredSelection) event.getSelection()).getFirstElement();
				recorder.setGenerator(newGenerator);
				updateAnnotationToolBar();
				Image image  = newGenerator.getImage();
				if(image != null){
					image = new Image(Display.getDefault(), image.getImageData().scaledTo(80,80));
					setTitleImage(image);
				} else {
					setTitleImage(null);
				}
				
			}
		});

		classTabFolder = new CTabFolder(container, SWT.CLOSE |SWT.BORDER);
		classTabFolder.setUnselectedCloseVisible(false);
		classTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,true));
		ToolBar addClassToolbar = new ToolBar(classTabFolder, SWT.HORIZONTAL);
		classTabFolder.setTopRight(addClassToolbar);
		ToolItem addClassItem = new ToolItem(addClassToolbar, SWT.NONE);
		addClassItem.setText("+");
		addClassItem.setToolTipText("Add class");
		addClassItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openClassShell();
			}
		});
		
		
		createTabItem(classTabFolder, "FirstClass");

		classTabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
				SourceViewer viewer = tabViewer.get(classTabFolder.getSelection());
				ClassDocument doc = (ClassDocument) viewer.getDocument();
				if (doc.getActiveMethod() == null) {
					recorder.setRecording(false);
					recordPauseButton.setText("Start Recording");
				}
				updateAnnotationToolBar();
			}
		});

		Composite actionsComposite = new Composite(container, SWT.NONE);
		actionsComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		recordPauseButton = new Button(actionsComposite, SWT.PUSH);
		recordPauseButton.setText("Start Recording");
		final Button copyButton = new Button(actionsComposite, SWT.PUSH);
		copyButton.setText("Copy");

		recordPauseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (recorder.isRecording()) {
					recorder.setRecording(false);
					recordPauseButton.setText("Start Recording");
				} else {
					SourceViewer viewer = tabViewer.get(classTabFolder.getSelection());
					ClassDocument doc = (ClassDocument) viewer.getDocument();
					if(doc.getActiveMethod() == null){
						openMethodShell(true);
					} else {
						recorder.setRecording(true);
						recordPauseButton.setText("Pause");
					}
				}
			}
		});

		this.recorder.addListener(new CodeGenerationListener() {
			public void handleCodeGenerated(GenerationRule code) {
				SourceViewer viewer = tabViewer.get(classTabFolder.getSelection());
				ClassDocument doc = (ClassDocument) viewer.getDocument();
				doc.addGenerationRule(code);
				viewer.setTopIndex(((ClassDocument) viewer.getDocument()).getLastOffset()-4);
			}
		});
		copyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final Clipboard cb = new Clipboard(Display.getDefault());
				TextTransfer textTransfer = TextTransfer.getInstance();
				SourceViewer viewer = tabViewer.get(classTabFolder.getSelection());
				cb.setContents(new Object[] { viewer.getDocument().get() }, new Transfer[] { textTransfer });
				cb.dispose();
			}
		});

		return container;
	}

	private void createTabItem(final CTabFolder tabFolder, String text) {
		CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		tabItem.setText(text);

		Composite composite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		ToolBar toolBar = new ToolBar(composite, SWT.HORIZONTAL);
		tabToolBar.put(tabItem, toolBar);
		RowLayout rLayout = new RowLayout();
		rLayout.fill = true;
		rLayout.justify = true;
		toolBar.setLayout(rLayout);
		ToolItem tItem = new ToolItem(toolBar, SWT.PUSH);
		tItem.setText("Add method");
		ToolItem methodItemDropDown = new ToolItem(toolBar, SWT.DROP_DOWN);
		methodItemDropDown.setText("No active method");

		ToolItem annotationsToolItem = new ToolItem(toolBar, SWT.DROP_DOWN);
		annotationsToolItem.setText("Method annotation");
		
		
		ToolItem annotationsClassToolItem = new ToolItem(toolBar, SWT.DROP_DOWN);
		annotationsClassToolItem.setText("Class annotation");

		methodListener = new MethodSelectionListener(methodItemDropDown, recorder,tabViewer,classTabFolder,annotationsToolItem);
		methodItemDropDown.addSelectionListener(methodListener);

		tItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
					openMethodShell(false);
			}
		});

		// create editor with syntax coloring
		final ClassDocument doc = new ClassDocument(text);
		final SourceViewer generatedCode = new SourceViewer(composite, new VerticalRuler(0), SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		JavaSourceViewerConfiguration javaConf = new JavaSourceViewerConfiguration(JavaUI.getColorManager(), PreferenceConstants.getPreferenceStore(), null, null);
		generatedCode.configure(javaConf);
		JavaTextTools tools = new JavaTextTools(PreferenceConstants.getPreferenceStore());
		tools.setupJavaDocumentPartitioner(doc);
		generatedCode.setDocument(doc);
		generatedCode.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		generatedCode.setEditable(false);
		tabItem.setControl(composite);
		tabFolder.setSelection(tabItem);
		tabViewer.put(tabItem, generatedCode);
		doc.setViewer(generatedCode);
		
		final AnnotationSelectionListener listenerAnnot = new AnnotationSelectionListener(annotationsToolItem, recorder,tabViewer,classTabFolder);
		annotationsToolItem.addSelectionListener(listenerAnnot);
		annotationsToolItem.setData(listenerAnnot);
		final ClassAnnotationSelectionListener listenerClassAnnot = new ClassAnnotationSelectionListener(annotationsClassToolItem, recorder,tabViewer,classTabFolder);
		annotationsClassToolItem.addSelectionListener(listenerClassAnnot);
		annotationsClassToolItem.setData(listenerClassAnnot);
		listenerAnnot.update();
	}
	
	private void openClassShell(){
		Shell s = new Shell();
		final AddClassDialog d = new AddClassDialog(s);
		ignoredShells.add(s);
		
	    this.getShell().getDisplay().asyncExec(new Runnable() {
				
	    	public void run() {
				if(Window.OK == d.open()){
					String classText = d.getClassName();
					createTabItem(classTabFolder, classText);
					recorder.setRecording(false);
					recordPauseButton.setText("Start Recording");
				}
	    	}
	    });
		
	}
	
	
	private void openMethodShell(final boolean fromStartButton){
		Shell s = new Shell();
		SourceViewer viewer = tabViewer.get(classTabFolder.getSelection()); 
		final ClassDocument doc = (ClassDocument) viewer.getDocument();
		final AddMethodDialog d = new AddMethodDialog(s, doc.getMethods());
		ignoredShells.add(s);
		
		this.getShell().getDisplay().asyncExec(new Runnable() {
			
			public void run() {
				if(Window.OK == d.open()){
					String methodText = d.getMethodName();
					doc.addMethod(methodText);
					methodListener.add(methodText);
					if(fromStartButton){
						recorder.setRecording(true);
						recordPauseButton.setText("Pause");
					}
					updateAnnotationToolBar();
				}
				
			}
		});
		
	}

	@Override
	protected Point getInitialSize() {
		return new Point(585, 650);
	}
	
	@Override
	public void createButtonsForButtonBar(Composite parent) {
		// Override to remove default buttons
	}

	public BotGeneratorEventDispatcher getRecorder() {
		return recorder;
	}

	public void setRecorder(BotGeneratorEventDispatcher recorder) {
		this.recorder = recorder;
	}
	

	public List<Generator> getAvailableGenerators() {
		return availableGenerators;
	}

	public void setAvailableGenerators(List<Generator> availableGenerators) {
		this.availableGenerators = availableGenerators;
	}

	public List<Shell> getIgnoredShells() {
		return ignoredShells;
	}

	public String getName() {
		return "JDT Dialog";
	}
	
	public String getId() {
		return ID;
	}
	
	public void setRecording(boolean record){
		recorder.setRecording(record);
		if(record){
			recordPauseButton.setText("Pause");
		} else {
			recordPauseButton.setText("Start Recording");
		}
	}
	
	public String getGeneratedCodeText(){
		SourceViewer viewer = tabViewer.get(classTabFolder.getSelection());
		ClassDocument doc = (ClassDocument) viewer.getDocument();
		return doc.get();
	}
	
	private void updateAnnotationToolBar(){
		ToolBar activeToolBar = tabToolBar.get(classTabFolder.getSelection());
		for(ToolItem item: activeToolBar.getItems()){
			if(item.getData() instanceof AnnotationSelectionListener){
				((AnnotationSelectionListener)item.getData()).addItems(recorder.getCurrentGenerator().createAnnotationRules());
				((AnnotationSelectionListener)item.getData()).update();
			} else if(item.getData() instanceof ClassAnnotationSelectionListener){
				((ClassAnnotationSelectionListener)item.getData()).addItems(recorder.getCurrentGenerator().createAnnotationRules());
				((ClassAnnotationSelectionListener)item.getData()).update();
			}
		}
	}
	
	
}
