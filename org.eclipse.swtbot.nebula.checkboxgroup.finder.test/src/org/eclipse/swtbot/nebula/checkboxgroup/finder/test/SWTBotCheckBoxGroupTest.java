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
package org.eclipse.swtbot.nebula.checkboxgroup.finder.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.nebula.widgets.opal.checkboxgroup.CheckBoxGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.nebula.checkboxgroup.finder.SWTCheckBoxGroupBot;
import org.eclipse.swtbot.nebula.checkboxgroup.finder.widgets.SWTBotCheckBoxGroup;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class SWTBotCheckBoxGroupTest {
	public SWTCheckBoxGroupBot bot;
	public static CheckBoxGroup checkBoxGroup;
	public Shell shell;

	@Before
	public void setUp() {
		bot = new SWTCheckBoxGroupBot();
		runInUIThread();
	}

	private void runInUIThread() {
		final Display display = Display.getDefault();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				shell = createShell(display, "Nebula CheckBoxGroup Test");
				checkBoxGroup = createCheckBoxGroup(shell);
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

	/*
	 * Example taken from
	 * https://github.com/eclipse/nebula/blob/master/widgets/opal/checkboxgroup/org.
	 * eclipse.nebula.widgets.opal.checkboxgroup.snippets/src/org/eclipse/nebula/
	 * widgets/opal/checkboxgroup/snippets/SnippetCheckBoxGroup.java
	 */
	private CheckBoxGroup createCheckBoxGroup(final Shell shell) {
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		final FillLayout layout1 = new FillLayout(SWT.VERTICAL);
		layout1.marginWidth = layout1.marginHeight = 10;
		shell.setLayout(layout1);

		// Displays the group
		final CheckBoxGroup group = new CheckBoxGroup(shell, SWT.NONE);
		group.setLayout(new GridLayout(4, false));
		group.setText("Use proxy server");

		final Composite content = group.getContent();

		final Label lblServer = new Label(content, SWT.NONE);
		lblServer.setText("Server:");
		lblServer.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

		final Text txtServer = new Text(content, SWT.NONE);
		txtServer.setText("proxy.host.com");
		txtServer.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final Label lblPort = new Label(content, SWT.NONE);
		lblPort.setText("Port:");
		lblPort.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

		final Text txtPort = new Text(content, SWT.NONE);
		txtPort.setText("1234");
		txtPort.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final Label lblUser = new Label(content, SWT.NONE);
		lblUser.setText("User ID:");
		lblUser.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

		final Text txtUser = new Text(content, SWT.NONE);
		txtUser.setText("MyName");
		txtUser.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final Label lblPassword = new Label(content, SWT.NONE);
		lblPassword.setText("Password:");
		lblPassword.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

		final Text txtPassword = new Text(content, SWT.PASSWORD);
		txtPassword.setText("password");
		txtPassword.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		return group;
	}

	@Test
	public void testCheckBoxGroup() {
		SWTBotCheckBoxGroup checkboxgroup = bot.checkBoxGroup();
		SWTBotText text0 = bot.text(0);
		SWTBotText text1 = bot.text(1);
		SWTBotText text2 = bot.text(2);
		SWTBotText text3 = bot.text(3);

		// Initial state- checkboxgroup selected, children enabled
		assertTrue(checkboxgroup.getSelection());
		assertTrue(text0.isEnabled());
		assertTrue(text1.isEnabled());
		assertTrue(text2.isEnabled());
		assertTrue(text3.isEnabled());

		// CheckBoxGroup deselected, children disabled
		checkboxgroup.setSelection(false);
		assertFalse(checkboxgroup.getSelection());
		assertFalse(text0.isEnabled());
		assertFalse(text1.isEnabled());
		assertFalse(text2.isEnabled());
		assertFalse(text3.isEnabled());

		//checkboxgroup selected again, children enabled
		checkboxgroup.setSelection(true);
		assertTrue(checkboxgroup.getSelection());
		assertTrue(text0.isEnabled());
		assertTrue(text1.isEnabled());
		assertTrue(text2.isEnabled());
		assertTrue(text3.isEnabled());
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
