/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.generator.framework.GenerationRule;
import org.eclipse.swtbot.generator.framework.Generator;
import org.eclipse.swtbot.generator.framework.IRecorderDialog;
import org.eclipse.swtbot.generator.ui.BotGeneratorEventDispatcher.CodeGenerationListener;

public class RecorderDialog extends TitleAreaDialog implements IRecorderDialog{

	private BotGeneratorEventDispatcher recorder;
	private List<Generator> availableGenerators;
	private Text generatedCode;
	private List<Shell> ignoredShells;
	public static final String ID = "org.eclipse.swtbot.generator.dialog.basic";

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public RecorderDialog() {
		super(null);
		Shell recorderShell = new Shell(Display.getDefault(), SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE);
		recorderShell.setText("SWTBot test recorder");
		this.setParentShell(recorderShell);
		ignoredShells = new ArrayList<Shell>();
		ignoredShells.add(recorderShell);
		setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE | SWT.RESIZE | SWT.MAX);
		setBlockOnOpen(false);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("SWTBot Test Recorder");
		setTitle("SWTBot Test Recorder");
		setMessage("This dialog will track the generated code while you're recording your UI scenario.");
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(new GridLayout(1, false));

		Composite generatorSelectionContainer = new Composite(container, SWT.NONE);
		generatorSelectionContainer.setLayout(new GridLayout(2, false));
		Label selectorLabel = new Label(generatorSelectionContainer, SWT.NONE);
		selectorLabel.setText("Target Bot API:");
		selectorLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		ComboViewer comboViewer = new ComboViewer(generatorSelectionContainer);
		comboViewer.setContentProvider(new ArrayContentProvider());
		comboViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object o) {
				return ((Generator)o).getLabel();
			}
		});
		comboViewer.setInput(this.availableGenerators);
		comboViewer.setSelection(new StructuredSelection(this.recorder.getCurrentGenerator()));
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				Generator newGenerator = (Generator) ((IStructuredSelection)event.getSelection()).getFirstElement();
				recorder.setGenerator(newGenerator);
			}
		});

		this.generatedCode = new Text(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		this.generatedCode.setText("// Generated code goes there\n");
		this.generatedCode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite actionsComposite = new Composite(container, SWT.NONE);
		actionsComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		final Button recordPauseButton = new Button(actionsComposite, SWT.PUSH);
		recordPauseButton.setText(this.recorder.isRecording() ? "Pause" : "Start Recording");
		final Button copyButton = new Button(actionsComposite, SWT.PUSH);
		copyButton.setToolTipText("Copy");
		copyButton.setText("Copy");

		recordPauseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				recorder.setRecording(!recorder.isRecording());
				recordPauseButton.setText(recorder.isRecording() ? "Pause" : "Start Recording");
			}
		});
		this.recorder.addListener(new CodeGenerationListener() {

			public void handleCodeGenerated(GenerationRule code) {
				for(String action: code.getActions())
				generatedCode.setText(generatedCode.getText() + action + ";\n");

			}
		});
		copyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final Clipboard cb = new Clipboard(Display.getCurrent());
				TextTransfer textTransfer = TextTransfer.getInstance();
			    cb.setContents(new Object[] { generatedCode.getText() }, new Transfer[] { textTransfer });
			    cb.dispose();
			}
		});

		return container;
	}

	@Override
	public void createButtonsForButtonBar(Composite parent) {
		// Override to remove default buttons
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 650);
	}

	public Text getGeneratedCodeText() {
		return this.generatedCode;
	}

	public BotGeneratorEventDispatcher getRecorderGenerator() {
		return this.recorder;
	}

	public void setAvailableGenerators(List<Generator> availableGenerators) {
		this.availableGenerators = availableGenerators;

	}

	public void setRecorder(BotGeneratorEventDispatcher recorder) {
		this.recorder = recorder;
	}

	public List<Shell> getIgnoredShells() {
		return ignoredShells;
	}

	public String getName() {
		return "Basic Dialog";
	}

	public String getId() {
		return ID;
	}
}