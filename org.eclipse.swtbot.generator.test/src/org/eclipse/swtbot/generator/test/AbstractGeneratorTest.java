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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public abstract class AbstractGeneratorTest {

	private Shell shell;
	protected SWTBot bot;
	protected RecorderDialog recorderDialog;

	public abstract void populateTestArea(Composite composite);

	@Before
	public void setUp() {
		this.shell = new Shell();
		populateTestArea(this.shell);
		this.shell.open();
		this.recorderDialog = StartupRecorder.openRecorder();
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				recorderDialog.getGeneratedCodeText().setText("");
			}
		});
		this.recorderDialog.getRecorderGenerator().switchRecording();
		this.bot = new SWTBot();
		this.shell.setFocus();
	}

	@After
	public void tearDown() {
		if (! this.shell.isDisposed()) {
			this.shell.close();
		}
		this.shell = null;
	}

	/**
	 * Process all recorded events and generates code, ignoring all future events
	 * to compute generated code.
	 */
	public void flushEvents() {
		this.recorderDialog.getRecorderGenerator().flushGenerationRules();
	}
}
