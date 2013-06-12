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
package org.eclipse.swtbot.generator.test;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.generator.ui.RecorderDialog;
import org.eclipse.swtbot.generator.ui.StartupRecorder;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractGeneratorTest {

	private Display display;
	private Shell shell;
	protected SWTBot bot;
	protected RecorderDialog recorderDialog;

	public abstract void populateTestArea(Composite composite);

	@Before
	public void setUp() {
		this.recorderDialog = StartupRecorder.openRecorder();
		this.bot = new SWTBot();
		SWTBotShell recorderShell = this.bot.shell("SWTBot Test Recorder");
		recorderShell.bot().button("Start Recording").click();
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				recorderDialog.getGeneratedCodeText().setText("");
			}
		});

		this.display = Display.getDefault();
		this.display.syncExec(new Runnable() {
			public void run() {
				AbstractGeneratorTest.this.shell = new Shell();
				AbstractGeneratorTest.this.shell.setText("Test Area");
				populateTestArea(AbstractGeneratorTest.this.shell);
				AbstractGeneratorTest.this.shell.open();
				AbstractGeneratorTest.this.shell.setFocus();
			}
		});
	}

	@After
	public void tearDown() {
		this.display.syncExec(new Runnable() {
			public void run() {
				if (! AbstractGeneratorTest.this.shell.isDisposed()) {
					AbstractGeneratorTest.this.shell.close();
				}
				AbstractGeneratorTest.this.shell = null;
			}
		});

	}

	/**
	 * Process all recorded events and generates code, ignoring all future events
	 * to compute generated code.
	 */
	public void flushEvents() {
		this.display.syncExec(new Runnable() {
			public void run() {
				AbstractGeneratorTest.this.recorderDialog.getRecorderGenerator().flushGenerationRules();
			}
		});
	}
}
