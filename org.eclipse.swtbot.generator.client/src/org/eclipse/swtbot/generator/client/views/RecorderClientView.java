/*******************************************************************************
 * Copyright (c) 2016, 2017 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
 *    Isaac Arvestad (Ericsson) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.client.views;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.generator.client.Recorder;
import org.eclipse.swtbot.generator.client.Recorder.ConnectionState;
import org.eclipse.swtbot.generator.client.RecorderClientStatusListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.texteditor.AbstractTextEditor;

/**
 * RecorderClientView shows SWTBot recorder output as it is received from a
 * RecorderServer.
 */
public class RecorderClientView extends ViewPart implements RecorderClientStatusListener, IElementChangedListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.eclipse.swtbot.generator.client.view.recorder.client";

	private static int DEFAULT_PORT_NUMBER = 8000;

	private static String IS_CONNECTED_LABEL = "Connected";
	private static String TRYING_CONNECT_LABEL = "Connecting...";
	private static String NOT_CONNECTED_LABEL = "No connection";

	private static String CONNECT_LABEL = "   Connect   ";
	private static String CANCEL_CONNECT_LABEL = "Cancel";
	private static String DISCONNECT_LABEL = "Disconnect";

	private static String ADD_TO_METHOD_TOGGLE_LABEL = "Add to method: ";

	private static String START_RECORDING_LABEL = "Record";
	private static String STOP_RECORDING_LABEL = "Stop recording";

	private Button connectButton;
	private Text portText;
	private Label statusLabel;

	private ComboViewer availableMethodsDropDown;
	private Button addToMethodToggle;
	private Button refreshAvailableMethodsButton;

	private Button recordingButton;
	private SourceViewer viewer;

	/**
	 * Initialize the Recorder singleton. If it already is initialized, reset
	 * it.
	 */
	public RecorderClientView() {
		if (Recorder.INSTANCE.isInitialized() == false) {
			Recorder.INSTANCE.initialize();
		} else {
			Recorder.INSTANCE.reset();
		}

		Recorder.INSTANCE.addStatusListener(this);
	}

	@Override
	public void dispose() {
		Recorder.INSTANCE.removeStatusListener(this);
		super.dispose();
	}

	/**
	 * Updates the UI state for any connection state change.
	 */
	public void updateUI() {
		updateUIForConnectionState(Recorder.INSTANCE.getConnectionState());
		portText.setText(String.valueOf(Recorder.INSTANCE.getPort()));
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setLayout(new GridLayout(1, false));

		Composite launchContainer = new Composite(group, SWT.NONE);
		launchContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		launchContainer.setLayout(new GridLayout(2, false));

		connectButton = new Button(launchContainer, SWT.PUSH);
		connectButton.setText(CONNECT_LABEL);
		connectButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		connectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (Recorder.INSTANCE.getConnectionState() == ConnectionState.CONNECTED) {
					Recorder.INSTANCE.interruptRecorderClient();
				} else if (Recorder.INSTANCE.getConnectionState() == ConnectionState.CONNECTING) {
					Recorder.INSTANCE.interruptRecorderClient();
				} else {
					Recorder.INSTANCE.startRecorderClient(getPort());
				}
				updateUIForConnectionState(Recorder.INSTANCE.getConnectionState());
			}
		});

		Composite portLaunchContainer = new Composite(launchContainer, SWT.NONE);
		portLaunchContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		portLaunchContainer.setLayout(new GridLayout(2, false));

		Label portLabel = new Label(portLaunchContainer, SWT.NONE);
		portLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		portLabel.setText("port:");

		portText = new Text(portLaunchContainer, SWT.RIGHT);
		portText.setText(String.valueOf(DEFAULT_PORT_NUMBER));
		portText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		portText.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				String port = portText.getText() + e.text;

				try {
					Integer.parseInt(port);
				} catch (NumberFormatException e2) {
					if (port.length() > 0) {
						e.doit = false;
					}
				}
			}
		});

		statusLabel = new Label(group, SWT.SHADOW_IN);
		statusLabel.setText("No connection");

		Label horizontalSeparator = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
		horizontalSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Composite methodSelectionContainer = new Composite(group, SWT.NONE);
		methodSelectionContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		methodSelectionContainer.setLayout(new GridLayout(3, false));

		addToMethodToggle = new Button(methodSelectionContainer, SWT.CHECK);
		addToMethodToggle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		addToMethodToggle.setText(ADD_TO_METHOD_TOGGLE_LABEL);
		addToMethodToggle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (Recorder.INSTANCE.isInsertingDirectlyInEditor()) {
					Recorder.INSTANCE.setInsertingDirectlyInEditor(false);
					availableMethodsDropDown.getControl().setEnabled(false);
					refreshAvailableMethodsButton.setEnabled(false);
				} else {
					Recorder.INSTANCE.setInsertingDirectlyInEditor(true);
					availableMethodsDropDown.getControl().setEnabled(true);
					refreshAvailableMethodsButton.setEnabled(true);

					// If the list is empty we refresh.
					if (availableMethodsDropDown.getInput() == null
							|| ((IMethod[]) availableMethodsDropDown.getInput()).length == 0) {
						updateMethodSelectionDropDown();
					}
				}
			}
		});
		addToMethodToggle.setEnabled(false);

		availableMethodsDropDown = new ComboViewer(methodSelectionContainer);
		availableMethodsDropDown.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		availableMethodsDropDown.getControl().setEnabled(false);
		availableMethodsDropDown.setContentProvider(ArrayContentProvider.getInstance());
		availableMethodsDropDown.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				IMethod method = (IMethod) element;

				String parameterNamesString = "";
				try {
					String[] parameterNames = method.getParameterNames();
					for (int i = 0; i < parameterNames.length; i++) {
						if (i == 0) {
							parameterNamesString += parameterNames[i];
						} else {
							parameterNamesString += ", " + parameterNames[i];
						}
					}

				} catch (JavaModelException e) {
					e.printStackTrace();
				}

				return method.getElementName() + "(" + parameterNamesString + ")";
			}
		});
		availableMethodsDropDown.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Recorder.INSTANCE.setSelectedMethod((IMethod) selection.getFirstElement());
			}
		});

		refreshAvailableMethodsButton = new Button(methodSelectionContainer, SWT.PUSH);
		refreshAvailableMethodsButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		refreshAvailableMethodsButton.setEnabled(false);
		refreshAvailableMethodsButton.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("org.eclipse.swtbot.generator.client", "icons/refresh.gif").createImage());
		refreshAvailableMethodsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateMethodSelectionDropDown();
			}
		});

		recordingButton = new Button(group, SWT.PUSH);
		recordingButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		recordingButton.setText(START_RECORDING_LABEL);
		recordingButton.setEnabled(false);
		recordingButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (Recorder.INSTANCE.isRecording()) {
					recordingButton.setText(START_RECORDING_LABEL);
					Recorder.INSTANCE.setRecording(false);
				} else {
					recordingButton.setText(STOP_RECORDING_LABEL);
					Recorder.INSTANCE.setRecording(true);
				}
			}
		});

		viewer = new SourceViewer(group, new VerticalRuler(0), SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setDocument(Recorder.INSTANCE.getDocument());
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		getSite().setSelectionProvider(viewer);

		JavaCore.addElementChangedListener(this);
		updateUIForConnectionState(Recorder.INSTANCE.getConnectionState());
		updateMethodSelectionDropDown();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	/**
	 * @return The currently chosen port.
	 */
	private int getPort() {
		return Integer.parseInt(portText.getText());
	}

	@Override
	public void connectionStarted() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				updateMethodSelectionDropDown();
				updateUIForConnectionState(ConnectionState.CONNECTED);
			}
		});
	}

	@Override
	public void connectionEnded() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				updateUIForConnectionState(ConnectionState.DISCONNECTED);
			}
		});
	}

	/**
	 * Updates all UI elements to match a new connection state.
	 *
	 * @param state
	 *            The new connection state.
	 */
	private void updateUIForConnectionState(ConnectionState state) {
		switch (state) {
		case CONNECTED:
			statusLabel.setText(IS_CONNECTED_LABEL);
			recordingButton.setText(START_RECORDING_LABEL);
			connectButton.setText(DISCONNECT_LABEL);
			addToMethodToggle.setSelection(false);

			recordingButton.setEnabled(true);
			connectButton.setEnabled(true);
			portText.setEnabled(false);
			addToMethodToggle.setEnabled(true);
			availableMethodsDropDown.getControl().setEnabled(false);
			refreshAvailableMethodsButton.setEnabled(false);

			break;
		case CONNECTING:
			statusLabel.setText(TRYING_CONNECT_LABEL);
			recordingButton.setText(START_RECORDING_LABEL);
			connectButton.setText(CANCEL_CONNECT_LABEL);
			addToMethodToggle.setSelection(false);

			recordingButton.setEnabled(false);
			connectButton.setEnabled(true);
			portText.setEnabled(false);
			addToMethodToggle.setEnabled(false);
			availableMethodsDropDown.getControl().setEnabled(false);
			refreshAvailableMethodsButton.setEnabled(false);

			break;
		case DISCONNECTED:
			statusLabel.setText(NOT_CONNECTED_LABEL);
			recordingButton.setText(START_RECORDING_LABEL);
			connectButton.setText(CONNECT_LABEL);
			addToMethodToggle.setSelection(false);

			recordingButton.setEnabled(false);
			connectButton.setEnabled(true);
			portText.setEnabled(true);
			addToMethodToggle.setEnabled(false);
			availableMethodsDropDown.getControl().setEnabled(false);
			refreshAvailableMethodsButton.setEnabled(false);

			break;
		}
	}

	/**
	 * Updates the method selection drop down if the change event is likely to
	 * affect the currently available methods in the method selection drop down.
	 */
	@Override
	public void elementChanged(ElementChangedEvent event) {
		if (Recorder.INSTANCE.shouldUpdateMethodSelectionViewer(event)) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					updateMethodSelectionDropDown();
				}
			});
		}
	}

	/**
	 * Returns the active editor or null if it fails.
	 *
	 * @return The active editor.
	 */
	private IEditorPart getActiveEditor() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null) {
			return null;
		}
		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
		if (workbenchWindow == null) {
			return null;
		}
		IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
		if (workbenchPage == null) {
			return null;
		}
		return workbenchPage.getActiveEditor();
	}

	/**
	 * Updates the method selection drop down.
	 */
	@SuppressWarnings("restriction")
	private void updateMethodSelectionDropDown() {
		IEditorPart activeEditor = getActiveEditor();

		if (activeEditor == null) {
			Recorder.INSTANCE.setSelectedMethod(null);
			Recorder.INSTANCE.setSelectedMethodDocument(null);

			availableMethodsDropDown.setInput(null);
		} else if (activeEditor instanceof org.eclipse.jdt.internal.ui.javaeditor.JavaEditor) {
			ITypeRoot root = org.eclipse.jdt.internal.ui.javaeditor.EditorUtility.getEditorInputJavaElement(activeEditor, false);
			IType type = root.findPrimaryType();
			final IMethod[] methods;
			try {
				methods = type.getMethods();

				IMethod methodToSelect = null;
				if (Recorder.INSTANCE.getSelectedMethod() != null) {
					methodToSelect = findSimilarMethod(Recorder.INSTANCE.getSelectedMethod(), methods);
				}
				IDocument currentActiveDocument = ((AbstractTextEditor) activeEditor).getDocumentProvider()
						.getDocument(activeEditor.getEditorInput());

				Recorder.INSTANCE.setSelectedMethod(methodToSelect);
				Recorder.INSTANCE.setSelectedMethodDocument(currentActiveDocument);
				availableMethodsDropDown.setInput(methods);

				// If methodToSelect is non-null we want to select it to
				// maintain the user's previous selection.
				if (methodToSelect != null) {
					ISelection selection = new StructuredSelection(methodToSelect);
					availableMethodsDropDown.setSelection(selection);
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Tries to find a similar method in an array of IMethod instances using
	 * <code>IMethod.isSimilar</code>
	 * 
	 * @param method
	 *            The method to search for.
	 * @param methods
	 *            The array to search through.
	 * @return The first similar method found or null if no similar method was
	 *         found.
	 */
	private IMethod findSimilarMethod(IMethod method, IMethod[] methods) {
		IMethod similarMethod = null;
		for (IMethod candidate : methods) {
			if (method.isSimilar(candidate)) {
				similarMethod = candidate;
				break;
			}
		}

		return similarMethod;
	}
}
