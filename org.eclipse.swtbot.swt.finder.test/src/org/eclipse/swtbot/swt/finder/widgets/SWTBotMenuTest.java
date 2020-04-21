/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
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
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swtbot.swt.finder.test.AbstractMenuExampleTest;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotMenuTest extends AbstractMenuExampleTest {
	
	private static final String ABOUT_MENU_TEXT = "About Address Book...";

	@Test
	public void findsMenus() throws Exception {
		assertText("&File", bot.menu("File").widget);
	}

	@Test
	public void findsSubMenus() throws Exception {
		SWTBotMenu menu = bot.menu("File").menu("Exit");
		assertText("E&xit", menu.widget);
	}

	@Test
	public void findsMenuRecursive() throws Exception {
		// "About" is both a submenu of Help and the last menu in the bar
		// with recursion we find the one in Help
		assertSame(bot.menu(ABOUT_MENU_TEXT, true).widget,
				bot.menu("Help").menu(ABOUT_MENU_TEXT).widget);
	}

	@Test
	public void findsMenuNotRecursive() throws Exception {
		// "About" is both a submenu of Help and the last menu in the bar
		// without recursion we find the one in the bar
		assertSame(bot.menu(ABOUT_MENU_TEXT, false).widget,
				bot.menu(ABOUT_MENU_TEXT, 1).widget);
	}

	@Test
	public void clicksSubMenus() throws Exception {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				addressBook
						.addAddressBook(new String[] { "last2", "first", "business phone", "home phone", "email@addres.ss", "fax number" });
				addressBook
						.addAddressBook(new String[] { "last6", "first", "business phone", "home phone", "email@addres.ss", "fax number" });
				addressBook
						.addAddressBook(new String[] { "last4", "first", "business phone", "home phone", "email@addres.ss", "fax number" });
			}
		});

		List<MenuItem> findControls = menuFinder.findMenus(anyMenuItem);
		MenuItem menuItem = findControls.get(14);
		assertText("Last Name", menuItem);

		bot.menu("Last Name").click();

		final TableItem[][] treeItems = new TableItem[][] { null };
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				treeItems[0] = addressBook.getTreeItems();
			}
		});
		TableItem[] tableItems = treeItems[0];
		assertEquals(3, tableItems.length);
		assertText("last2", tableItems[0]);
		assertText("last4", tableItems[1]);
		assertText("last6", tableItems[2]);

		new SWTBotMenu(menuItem).click();

		display.syncExec(new Runnable() {
			@Override
			public void run() {
				treeItems[0] = addressBook.getTreeItems();
			}
		});

		assertEquals(3, tableItems.length);
		assertText("last6", tableItems[0]);
		assertText("last4", tableItems[1]);
		assertText("last2", tableItems[2]);
	}

}
