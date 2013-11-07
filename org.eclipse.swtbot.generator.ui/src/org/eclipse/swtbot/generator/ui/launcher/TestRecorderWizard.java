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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.generator.ui.Messages;
import org.eclipse.swtbot.generator.ui.StartupRecorder;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;

public class TestRecorderWizard extends Wizard implements INewWizard {

	private TestRecorderWizardPage firstPage;

	@Override
	public void addPages() {
		setWindowTitle(Messages.recorderDialogTitle);
		firstPage = new TestRecorderWizardPage();
		addPage(firstPage);
	}

	@Override
	public boolean performFinish() {
		if (firstPage.runNewInstance()) {
			ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
			ILaunchConfigurationType type = manager.getLaunchConfigurationType("org.eclipse.swtbot.generator.ui.launcher.TestRecorderLaunchConfiguration");

			try {
				ILaunchConfiguration[] lcs = manager.getLaunchConfigurations(type);
				ILaunchConfigurationWorkingCopy workingCopy = null;
				for (int i = 0; i < lcs.length; ++i) {
					if (lcs[i].getName().equals("Test Recorder "+firstPage.getSelectedDialogName())) {
						workingCopy = lcs[i].getWorkingCopy();
						break;
					}
				}
				if (workingCopy == null) {
					workingCopy = type.newInstance(null, "Test Recorder "+firstPage.getSelectedDialogName());
					String recorderDialog = " -D"+StartupRecorder.DIALOG_PROPERTY+"=" +firstPage.getSelectedDialogId();
					workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, recorderDialog);
					
				}
				final ILaunchConfiguration config = workingCopy.doSave();
				Display.getCurrent().syncExec(new Runnable() {

					public void run() {
						DebugUITools.openLaunchConfigurationDialog(PlatformUI
								.getWorkbench().getActiveWorkbenchWindow()
								.getShell(), config,
								"org.eclipse.debug.ui.launchGroup.run", null);

					}
				});

			} catch (CoreException ex) {
				ex.printStackTrace();
			}
		} else {
			StartupRecorder.openRecorder(firstPage.getSelectedDialogId());
		}
		return true;
	}

	public void init(IWorkbench arg0, IStructuredSelection arg1) {

	}

}
