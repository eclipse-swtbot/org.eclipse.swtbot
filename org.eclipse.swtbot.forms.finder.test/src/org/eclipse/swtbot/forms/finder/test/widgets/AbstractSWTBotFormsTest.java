/*******************************************************************************
 * Copyright (c) 2010 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Lorenzo Bettini - https://bugs.eclipse.org/bugs/show_bug.cgi?id=464687
 *******************************************************************************/
package org.eclipse.swtbot.forms.finder.test.widgets;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.forms.finder.SWTFormsBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.junit.After;
import org.junit.BeforeClass;

public abstract class AbstractSWTBotFormsTest {

	protected static SWTFormsBot		bot			= new SWTFormsBot();
	protected static SWTWorkbenchBot	workbench	= new SWTWorkbenchBot();

	@BeforeClass
	public static void beforeClass() {
		closeWelcomePage();
		closeAllViews();
		showFormsView();
	}

	private static void showFormsView() {
		// don't use recursion when searching for "Window" menu
		// to avoid a bug in ShowInMenu in Luna; "Show In..." is a submenu
		// of "Navigate" which comes before "Window"
		bot.menu("Window", false).menu("Show View").menu("Other...").click();
		SWTBot showViewDialogBot = bot.shell("Show View").bot();
		showViewDialogBot.tree().getTreeItem("Eclipse Forms Examples").expand().getNode("Eclipse Form").select();
		getOkButtonInShowViewDialog().click();
		workbench.viewByTitle("Eclipse Form");
	}

	private static SWTBotButton getOkButtonInShowViewDialog() {
		// Button text was "OK" before Oxygen M7, "Open" since.
		SWTBotButton button = bot.button();
		if (!button.getText().equals("Open") && !button.getText().equals("&Open") && !button.getText().equals("OK")) {
			button = bot.button(1);
		}
		return button;
	}

	private static void closeAllViews() {
		List<SWTBotView> views = workbench.views();
		for (SWTBotView view : views) {
			view.close();
		}
	}

	private static void closeWelcomePage() {
		try {
			SWTBotPreferences.TIMEOUT = 0;
			System.setProperty("org.eclipse.swtbot.search.timeout", "0");
			workbench.viewByTitle("Welcome").close();
		} catch (WidgetNotFoundException e) {
			// do nothing
		} finally {
			SWTBotPreferences.TIMEOUT = 5000;
		}
	}

	@After
	public void tearDown() throws Exception {
		saveAndCloseAllEditors();
	}

	/**
	 * @throws WidgetNotFoundException
	 */
	private void saveAndCloseAllEditors() {
		List<? extends SWTBotEditor> editors = workbench.editors();
		for (SWTBotEditor editor : editors) {
			editor.saveAndClose();
		}
	}
}
