/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Mickael Istria (Red Hat Inc.) - initial implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.test.AbstractSWTShellTest;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

public class SWTBotButtonJFaceTest extends AbstractSWTShellTest {

	@Override
	protected void createUI(Composite parent) {
		// leave empty
	}

	@Test
	public void testJFaceWizardButtons() {
		Assume.assumeTrue("UI Harness is requires for that test", Display.getCurrent() != null);
		final WizardDialog dialog = new WizardDialog(shell, new Wizard() {
			@Override
			public void addPages() {
				addPage(new WizardPage("Page 1") {
					@Override
					public void createControl(Composite parent) {
						setControl(new Label(parent, SWT.NONE));
					}
				});
				addPage(new WizardPage("Page 2") {
					@Override
					public void createControl(Composite parent) {
						setControl(new Label(parent, SWT.NONE));
					}
				});
			}

			@Override
			public boolean performFinish() {
				return true;
			}
		});
		dialog.setBlockOnOpen(false);
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
				dialog.open();
			}
		});
		Shell shell = dialog.getShell();
		SWTBotButton nextButton = bot.buttonWithId(null, Integer.valueOf(IDialogConstants.NEXT_ID));
		nextButton.click();
		Assert.assertEquals("Page 2", dialog.getCurrentPage().getName());
		SWTBotButton finishButton = bot.buttonWithId(null, Integer.valueOf(IDialogConstants.FINISH_ID));
		finishButton.click();
		Assert.assertTrue(shell.isDisposed());
		Assert.assertNull(dialog.getShell());
	}

	@Test
	public void testJFaceErrorDialogButton() {
		boolean previousMode = ErrorDialog.AUTOMATED_MODE;
		try {
			ErrorDialog.AUTOMATED_MODE = false;
			final ErrorDialog dialog = new ErrorDialog(this.shell, "test dialog", "test message", Status.CANCEL_STATUS, IStatus.CANCEL);
			dialog.setBlockOnOpen(false);
			UIThreadRunnable.syncExec(new VoidResult() {
				@Override
				public void run() {
					dialog.open();
				}
			});
			Shell shell = dialog.getShell();
			SWTBotButton okButton = bot.buttonWithId(null, Integer.valueOf(IDialogConstants.OK_ID));
			okButton.click();
			Assert.assertTrue(shell.isDisposed());
			Assert.assertNull(dialog.getShell());
		} finally {
			ErrorDialog.AUTOMATED_MODE = previousMode;
		}
	}
}
