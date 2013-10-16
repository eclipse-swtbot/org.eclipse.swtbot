/*******************************************************************************
 * Copyright (c) 2013 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.jdt.test;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellIsActive;
import static org.eclipse.swtbot.swt.finder.waits.Conditions.widgetIsEnabled;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.generator.jdt.editor.JDTRecorderDialog;
import org.eclipse.swtbot.generator.test.TestDialog;
import org.eclipse.swtbot.generator.ui.StartupRecorder;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractGeneratorTest {

	private TestDialog dialog;
	protected SWTBot bot;
	private JDTRecorderDialog recorderDialog;

	@Before
	public void setUp(){

		recorderDialog = (JDTRecorderDialog)StartupRecorder.openRecorder("org.eclipse.swtbot.generator.dialog.jdt"); //$NON-NLS-1$

		this.bot = new SWTBot();
		bot.waitUntil(shellIsActive("SWTBot Test Recorder"),5000);
		bot.button("Start Recording").click();
		bot.waitUntil(shellIsActive("Add new method"),1000);

		SWTBotShell methodShell = new SWTBot().shell("Add new method");
		methodShell.bot().textWithLabel("Method name:").setText("firstMethod");
		bot.waitUntil(widgetIsEnabled(bot.button("OK")),1000);
		methodShell.bot().button("OK").click();
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				dialog = new TestDialog(new Shell());
				dialog.open();
			}
		});
	}

	@After
	public void tearDown() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (!dialog.getShell().isDisposed()) {
					dialog.close();
				}
				dialog = null;
			}
		});
	}

	/**
	 * Process all recorded events and generates code, ignoring all future events
	 * to compute generated code.
	 */
	public void flushEvents() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				recorderDialog.getRecorder().flushGenerationRules();
			}
		});

	}
}
