/*******************************************************************************
 * Copyright (c) 2013 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Rastislav Wagner (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.ui.launcher;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swtbot.generator.framework.IRecorderDialog;
import org.eclipse.swtbot.generator.ui.GeneratorExtensionPointManager;
import org.eclipse.swtbot.generator.ui.Messages;

public class TestRecorderWizardPage extends WizardPage {

	private ComboViewer combo;
	private Composite container;
	private Button newInstance;
	private Button currentInstance;

	protected TestRecorderWizardPage() {
		super(Messages.recorderDialogTitle);
		setDescription(Messages.recorderDescription);
		setTitle(Messages.recorderDialogTitle);
		URL imageUrl = null;
		try {
			imageUrl = new URL("platform:/plugin/org.eclipse.swtbot.generator/icons/swtbot_rec64.png");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if(imageUrl != null) {
			setImageDescriptor(ImageDescriptor.createFromURL(imageUrl));
		}
	}

	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		Label label1 = new Label(container, SWT.NONE);
		label1.setText(Messages.recorderDialog);

		combo = new ComboViewer(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		List<IRecorderDialog> dialogs = GeneratorExtensionPointManager.loadDialogs();
		combo.setContentProvider(new ArrayContentProvider());
		combo.setInput(dialogs);
		combo.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				return ((IRecorderDialog) element).getName();
			}
		});
		combo.setSelection(new StructuredSelection(dialogs.get(0)));

		Group eclipseGroup = new Group(container, SWT.NONE);
		eclipseGroup.setText(Messages.eclipseGroup);
		layout = new GridLayout();
		eclipseGroup.setLayout(layout);
		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true,false);
		gridData.horizontalSpan = 2;
		eclipseGroup.setLayoutData(gridData);

		currentInstance = new Button(eclipseGroup, SWT.RADIO);
		currentInstance.setText(Messages.currentEclipseInstance);
		currentInstance.setSelection(true);

		newInstance = new Button(eclipseGroup, SWT.RADIO);
		newInstance.setText(Messages.newEclipseInstance);

		hookListeners();

		setControl(container);
		setPageComplete(true);

	}

	private void hookListeners() {

		combo.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent arg0) {
				IStructuredSelection selection = (IStructuredSelection) arg0.getSelection();
				if (!selection.isEmpty()) {
					setPageComplete(true);
				} else {
					setPageComplete(false);
				}

			}
		});
	}

	public String getSelectedDialogId() {
		ISelection selection = combo.getSelection();
		if (!selection.isEmpty()) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			return ((IRecorderDialog) structuredSelection.getFirstElement()).getId();
		}
		return null;
	}
	
	public String getSelectedDialogName() {
		ISelection selection = combo.getSelection();
		if (!selection.isEmpty()) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			return ((IRecorderDialog) structuredSelection.getFirstElement()).getName();
		}
		return null;
	}

	public boolean runNewInstance() {
		return newInstance.getSelection();
	}

}
