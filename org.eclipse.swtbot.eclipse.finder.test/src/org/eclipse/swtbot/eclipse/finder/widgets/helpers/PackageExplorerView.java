/*******************************************************************************
 * Copyright (c) 2008, 2015 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Patrick Tasse - Improve SWTBot menu API and implementation (Bug 479091) 
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.widgets.helpers;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

/**
 * Screen object that represents the operations that can be performed on the package explorer view.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class PackageExplorerView {

	private SWTWorkbenchBot	bot	= new SWTWorkbenchBot();

	public void deleteProject(String projectName) throws Exception {
		SWTBotTree tree = tree();
		tree.setFocus();
		SWTBotTreeItem treeItem = tree.getTreeItem(projectName);
		treeItem.contextMenu().menu("Delete").click();
		SWTBotShell shell = bot.shell("Delete Resources");
		shell.activate();
		Button button = bot.widget(widgetOfType(Button.class), shell.widget);
		new SWTBotCheckBox(button).select();
		bot.button("OK").click();
		bot.waitUntil(Conditions.shellCloses(shell));
	}

	/**
	 * @return
	 * @throws WidgetNotFoundException
	 */
	private SWTBotTree tree() throws WidgetNotFoundException {
		return view().bot().tree();
	}

	/**
	 * @return
	 * @throws WidgetNotFoundException
	 */
	private SWTBotView view() throws WidgetNotFoundException {
		return bot.viewByTitle("Package Explorer");
	}

}
