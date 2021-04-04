/*******************************************************************************
 * Copyright (c) 2021 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Aparna Argade(Cadence Design Systems, Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.nebula.stepbar.finder.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.nebula.widgets.stepbar.Stepbar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.nebula.stepbar.finder.SWTStepbarBot;
import org.eclipse.swtbot.nebula.stepbar.finder.widgets.SWTBotStepbar;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToggleButton;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class SWTBotStepbarTest {
	public SWTStepbarBot bot;
	public static Stepbar stepbar;
	private static int index = 0;
	private static boolean error = false;
	public Shell shell;

	@Before
	public void setUp() {
		bot = new SWTStepbarBot();
		runInUIThread();
	}

	private void runInUIThread() {
		final Display display = Display.getDefault();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				shell = createShell(display, "Nebula Stepbar Test");
				stepbar = createStepbar(shell);
				shell.open();
			}
		});
	}

	protected Shell createShell(final Display display, final String text) {
		Shell shell = new Shell(display);
		shell.setText(text);
		shell.setLayout(new FillLayout());
		return shell;
	}

	/**
	 * Example taken from
	 * https://github.com/eclipse/nebula/blob/master/widgets/stepbar/org.eclipse.nebula.widgets.stepbar.snippets/src/org/eclipse/nebula/widgets/stepbar/StepBarSnippet.java
	 *
	 * @param shell
	 * @return stepbar control
	 */
	private Stepbar createStepbar(final Shell shell) {
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		final FillLayout layout1 = new FillLayout(SWT.VERTICAL);
		layout1.marginWidth = layout1.marginHeight = 10;
		shell.setLayout(layout1);

		Stepbar bar = new Stepbar(shell, SWT.BOTTOM | SWT.BORDER);
		bar.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1));
		bar.setSteps(new String[] { "First step", "Second step", "Third step" });

		final Button previous = new Button(shell, SWT.PUSH);
		previous.setText("Previous step");
		previous.setEnabled(false);
		previous.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, false));

		final Button next = new Button(shell, SWT.PUSH);
		next.setText("Next step");
		next.setLayoutData(new GridData(GridData.END, GridData.FILL, false, false));

		previous.addListener(SWT.Selection, e -> {
			if (error) {
				e.doit = false;
				return;
			}
			index--;
			next.setEnabled(true);
			previous.setEnabled(index != 0);
			bar.setCurrentStep(index);
		});
		next.addListener(SWT.Selection, e -> {
			if (error) {
				e.doit = false;
				return;
			}
			index++;
			next.setEnabled(index != bar.getSteps().size() - 1);
			previous.setEnabled(true);
			bar.setCurrentStep(index);
		});

		final Label separator = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setBackground(shell.getBackground());
		separator.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false, 2, 1));

		final GridData bottomGD = new GridData(GridData.BEGINNING, GridData.FILL, true, false, 2, 1);
		bottomGD.widthHint = 400;

		final Button toggleShowError = new Button(shell, SWT.TOGGLE);
		toggleShowError.setText("Show error on step");
		toggleShowError.setLayoutData(bottomGD);
		toggleShowError.addListener(SWT.Selection, e -> {
			error = !error;
			bar.setErrorState(error);
		});
		return bar;
	}

	@Test
	public void testStepbar() {
		SWTBotStepbar swtbotstepbar = bot.stepbar();
		SWTBotButton next = bot.button("Next step");
		SWTBotButton previous = bot.button("Previous step");
		SWTBotToggleButton error = bot.toggleButton("Show error on step");
		assertEquals("Initial step is 0", swtbotstepbar.getCurrentStep(), 0);
		assertFalse("No error initially", swtbotstepbar.getErrorState());
		List<String> stepslst = swtbotstepbar.getSteps();
		assertEquals("Three steps configured", stepslst.size(), 3);
		assertEquals("First step text", stepslst.get(0), "First step");
		assertEquals("Second step text", stepslst.get(1), "Second step");
		assertEquals("Third step text", stepslst.get(2), "Third step");
		next.click();
		assertEquals("Current step is 1", swtbotstepbar.getCurrentStep(), 1);
		next.click();
		assertEquals("Current step is 2", swtbotstepbar.getCurrentStep(), 2);
		previous.click();
		assertEquals("Current step is 1", swtbotstepbar.getCurrentStep(), 1);
		error.click();
		assertTrue("Got Error!", swtbotstepbar.getErrorState());
	}

	@After
	public void tearDown() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				if (shell != null) {
					shell.dispose();
				}
			}
		});
	}

}
