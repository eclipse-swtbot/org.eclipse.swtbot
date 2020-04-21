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
 *     Ketan Patel - https://bugs.eclipse.org/bugs/show_bug.cgi?id=259837
 *     Jesper S Møller - https://bugs.eclipse.org/bugs/show_bug.cgi?id=322668
 *     Lorenzo Bettini - https://bugs.eclipse.org/bugs/show_bug.cgi?id=464687
 *     Patrick Tasse - Improve SWTBot menu API and implementation (Bug 479091) 
 *******************************************************************************/

package org.eclipse.swtbot.swt.finder.waits;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotRootMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.Matcher;

/**
 * This is a factory class to create some conditions provided with SWTBot.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public abstract class Conditions {

	/**
	 * Gets the condition for checking tables have the proper number of rows. Useful in cases where the table is
	 * populated continuously from a non UI thread.
	 *
	 * @param table the table
	 * @param rowCount the number of rows that the table must have, in order for {@link ICondition#test()} to evaluate
	 *            to <code>true</code>.
	 * @return <code>true</code> if the table has the number of rows specified. Otherwise <code>false</code>.
	 * @throws IllegalArgumentException Thrown if the row count is less then 1.
	 * @since 1.2
	 */
	public static ICondition tableHasRows(SWTBotTable table, int rowCount) {
		return new TableHasRows(table, rowCount);
	}

	/**
	 * Gets the condition for checking trees have the proper number of rows. Useful in cases where the tree is populated
	 * continuously from a non UI thread.
	 * 
	 * @param tree the tree
	 * @param rowCount the number of rows that the tree must have, in order for {@link ICondition#test()} to evaluate to
	 *            <code>true</code>.
	 * @return <code>true</code> if the tree has the number of rows specified. Otherwise <code>false</code>.
	 * @throws IllegalArgumentException Thrown if the row count is less then 1.
	 * @since 2.0
	 */
	public static ICondition treeHasRows(SWTBotTree tree, int rowCount) {
		return new TreeHasRows(tree, rowCount);
	}

	/**
	 * Gets the condition for checking that a tree item has a node with the
	 * given text. When the node is not found, if the tree item is expanded it
	 * is collapsed and re-expanded to attempt to make the node appear.
	 *
	 * @param treeItem
	 *            the treeItem
	 * @param text
	 *            the text of the child node to look for
	 * @return a condition that returns false until the tree item has a node with the given text.
	 * @throws NullPointerException
	 *             Thrown if the tree item is <code>null</code>.
	 * @since 2.6
	 */
	public static ICondition treeItemHasNode(SWTBotTreeItem treeItem, String text) {
		return new TreeItemHasNode(treeItem, text);
	}

	/**
	 * Gets the condition for checking if shells have closed. Useful in cases where a shell takes long to close.
	 *
	 * @param shell the shell to monitor.
	 * @return a condition that evaluates to false until the shell is closed or invisible.
	 * @since 1.2
	 */
	public static ICondition shellCloses(SWTBotShell shell) {
		return new ShellCloses(shell);
	}

	/**
	 * Gets the condition for checking if an awaited shell is visible and has focus
	 *
	 * @param shellText the text of the shell.
	 * @return a condition that evaluates to false until the shell is visible and has focus.
	 * @since 1.3
	 */
	public static ICondition shellIsActive(String shellText) {
		return new ShellIsActive(shellText);
	}

	/**
	 * @param matcher a matcher.
	 * @return a condition that waits until the matcher evaluates to true.
	 * @since 2.0
	 */
	public static <T extends Widget> WaitForObjectCondition<T> waitForWidget(Matcher<T> matcher) {
		return new WaitForWidget<T>(matcher);
	}

	/**
	 * @param matcher a matcher.
	 * @param parent the parent under which a widget will be found.
	 * @return a condition that waits until the matcher evaluates to true.
	 * @since 2.0
	 */
	public static <T extends Widget> WaitForObjectCondition<T> waitForWidget(Matcher<T> matcher, Widget parent) {
		return new WaitForWidgetInParent<T>(matcher, parent);
	}

	/**
	 * @param matcher the matcher.
	 * @return a condition that waits until the matcher evaluates to true.
	 * @since 2.0
	 */
	public static WaitForObjectCondition<Shell> waitForShell(Matcher<Shell> matcher) {
		return new WaitForShell(matcher);
	}

	/**
	 * @param matcher the matcher.
	 * @param parent the parent under which a shell will be found or <code>null</code> to search all shells.
	 * @return a condition that waits until the matcher evaluates to true.
	 * @since 2.0
	 */
	public static WaitForObjectCondition<Shell> waitForShell(Matcher<Shell> matcher, Shell parent) {
		return new WaitForShellInParent(parent, matcher);
	}

	/**
	 * Gets the condition to wait for a shell's menu bar.
	 *
	 * @param shell the shell.
	 * @return a condition that waits for the shell's menu bar.
	 * @since 2.4
	 */
	public static WaitForObjectCondition<Menu> waitForMenuBar(SWTBotShell shell) {
		return new WaitForMenuBar(shell);
	}

	/**
	 * Gets the condition to wait for a control's pop up menu.
	 *
	 * @param control the control.
	 * @return a condition that waits for the control's pop up menu.
	 * @since 2.4
	 */
	public static WaitForObjectCondition<Menu> waitForPopupMenu(Control control) {
		return new WaitForPopupMenu(control);
	}

	/**
	 * Gets the condition to wait for a root menu's matching menu item.
	 *
	 * @param menu the menu bar or pop up menu.
	 * @param matcher the matcher that can match menu items.
	 * @param recursive if set to true, will find depth-first in sub-menus as well.
	 * @param index the index of the menu item, in case there are multiple matching menu items.
	 * @return a condition that waits for the menu's matching menu item.
	 * @since 2.4
	 */
	public static WaitForObjectCondition<MenuItem> waitForMenuItem(SWTBotRootMenu menu, Matcher<MenuItem> matcher, boolean recursive, int index) {
		return new WaitForMenuItem(menu, matcher, recursive, index);
	}

	/**
	 * Gets the condition to wait for a menu's matching menu item.
	 *
	 * @param menu the menu.
	 * @param matcher the matcher that can match menu items.
	 * @param recursive if set to true, will find depth-first in sub-menus as well.
	 * @param index the index of the menu item, in case there are multiple matching menu items.
	 * @return a condition that waits for the menu's matching menu item.
	 * @since 2.4
	 */
	public static WaitForObjectCondition<MenuItem> waitForMenuItem(SWTBotMenu menu, Matcher<MenuItem> matcher, boolean recursive, int index) {
		return new WaitForMenuItem(menu, matcher, recursive, index);
	}

	/**
	 * @param shell the shell to search for the menu.
	 * @param matcher the matcher.
	 * @return a condition that waits until the matcher evaluates to true.
	 * @since 2.0
	 */
	public static WaitForObjectCondition<MenuItem> waitForMenu(SWTBotShell shell, Matcher<MenuItem> matcher) {
		return waitForMenu(shell, matcher, true);
	}

	/**
	 * @param shell the shell to search for the menu.
	 * @param matcher the matcher.
	 * @param recursive if set to true, will find submenus as well
	 * @return a condition that waits until the matcher evaluates to true.
	 * @since 2.3
	 */
	public static WaitForObjectCondition<MenuItem> waitForMenu(SWTBotShell shell, Matcher<MenuItem> matcher, boolean recursive) {
		return new WaitForMenu(shell, matcher, recursive);
	}

	/**
	 * @param widget the widget 
	 * @return a condition that waits until the widget is enabled.
	 * @since 2.0
	 */
	public static ICondition widgetIsEnabled(AbstractSWTBot<? extends Widget> widget){
		return new WidgetIsEnabledCondition(widget);
	}
}
