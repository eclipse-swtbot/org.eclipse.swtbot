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
import org.eclipse.swtbot.generator.test.AbstractGeneratorTest;
import org.eclipse.swtbot.generator.test.TestDialog;
import org.eclipse.swtbot.generator.ui.StartupRecorder;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.Before;

public abstract class AbstractJDTGeneratorTest extends AbstractGeneratorTest {

	@Override
	@Before
	public void setUp(){

		this.recorderDialog = (JDTRecorderDialog)StartupRecorder.openRecorder("org.eclipse.swtbot.generator.dialog.jdt"); //$NON-NLS-1$

		this.bot = new SWTBot();
		this.bot.waitUntil(shellIsActive("SWTBot Test Recorder"),5000);
		this.bot.button("Start Recording").click();
		this.bot.waitUntil(shellIsActive("Add new method"),1000);

		SWTBotShell methodShell = new SWTBot().shell("Add new method");
		methodShell.bot().textWithLabel("Method name:").setText("firstMethod");
		bot.waitUntil(widgetIsEnabled(bot.button("OK")),1000);
		methodShell.bot().button("OK").click();
		this.display = Display.getDefault();
		this.display.syncExec(new Runnable() {
			@Override
			public void run() {
				dialog = new TestDialog(new Shell(), AbstractJDTGeneratorTest.this);
				dialog.open();
			}
		});
	}

}
