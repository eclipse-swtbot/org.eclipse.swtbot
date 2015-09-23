/*******************************************************************************
 * Copyright (c) 2008, 2015 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Patrick Tasse - Fix radio menu item click behavior (Bug 451126 & Bug 397649),
 *                     test dynamic menus
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertNotSameWidget;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertSameWidget;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertTextContains;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.hamcrest.core.AnyOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
@RunWith(Parameterized.class)
public class SWTBotPopupMenuTest extends AbstractControlExampleTest {

	private SWTBotShell	popupShell;
	private MenuType menuType;

	private enum MenuType {
		StaticMenu, DynamicMenu
	}

	@Parameters(name = "{0}")
	public static Collection<MenuType> parameters() {
		return Arrays.asList(MenuType.values());
	}

	public SWTBotPopupMenuTest(MenuType menuType) {
		this.menuType = menuType;
	}

	@Test
	public void findsMenus() throws Exception {
		assertNotNull(popupShell.contextMenu("Push").widget);
		if (menuType == MenuType.StaticMenu) {
			assertSameWidget(popupShell.contextMenu("Push").widget, popupShell.contextMenu("Push").widget);
		} else {
			/* Dynamic menu manager disposes the old MenuItem */
			assertNotSameWidget(popupShell.contextMenu("Push").widget, popupShell.contextMenu("Push").widget);
		}
	}

	@Test
	public void findsSubMenus() throws Exception {
		SWTBotMenu push1 = popupShell.contextMenu("Push");
		SWTBotMenu cascade1 = popupShell.contextMenu("Cascade");
		SWTBotMenu push2 = cascade1.menu("Push");
		SWTBotMenu cascade2 = cascade1.menu("Cascade");
		SWTBotMenu push3 = cascade2.menu("Push");
		assertNotNull(push1.widget);
		assertNotNull(push2.widget);
		assertNotNull(push3.widget);
		assertNotSameWidget(push2.widget, push1.widget);
		assertNotSameWidget(push3.widget, push1.widget);
		assertNotSameWidget(push3.widget, push2.widget);
		if (menuType == MenuType.StaticMenu) {
			assertSameWidget(push3.widget, cascade2.menu("Push").widget);
			assertSameWidget(push3.widget, cascade1.menu("Cascade").menu("Push").widget);
			assertSameWidget(push3.widget, popupShell.contextMenu("Cascade").menu("Cascade").menu("Push").widget);
		} else {
			/* Dynamic menu manager disposes the old MenuItem */
			assertNotSameWidget(push3.widget, cascade2.menu("Push").widget);
			assertNotSameWidget(push3.widget, cascade1.menu("Cascade").menu("Push").widget);
			assertNotSameWidget(push3.widget, popupShell.contextMenu("Cascade").menu("Cascade").menu("Push").widget);
		}
	}

	@Test
	public void clicksMenu() throws Exception {
		bot.button("Clear").click();
		popupShell.contextMenu("Push").click();
		String text = bot.textInGroup("Listeners").getText();
		String expectedLinux = "Show [22]: MenuEvent{Menu {Push, , Check, Radio1, Radio2, Cascade} time=";
		String expectedWindows = "Show [22]: MenuEvent{Menu {Push, |, Check, Radio1, Radio2, Cascade} time=";
		assertThat(text, AnyOf.anyOf(containsString(expectedWindows), containsString(expectedLinux)));
		assertTextContains("Selection [13]: SelectionEvent{MenuItem {Push} time=", bot.textInGroup("Listeners").widget);
	}

	@Test
	public void clicksCheckMenuItem() throws Exception {
		SWTBotMenu item = popupShell.contextMenu("Check");
		boolean checked = item.isChecked();
		item.click();
		assertTrue(checked != item.isChecked());
		item.click();
		assertTrue(checked == item.isChecked());
	}

	@Test
	public void clicksRadioMenuItem() throws Exception {
		SWTBotMenu item1 = popupShell.contextMenu("Radio1");
		SWTBotMenu item2 = popupShell.contextMenu("Radio2");
		item1.click();
		assertTrue(item1.isChecked());
		assertTrue(!item2.isChecked());
		item1.click();
		assertTrue(item1.isChecked());
		assertTrue(!item2.isChecked());
		item2.click();
		assertTrue(!item1.isChecked());
		assertTrue(item2.isChecked());
	}

	@Before
	public void setUp() throws Exception {
		bot = new SWTBot().activeShell().bot();
		bot.tabItem("Menu").activate();

		bot.checkBox("Listen").select();

		bot.checkBox("SWT.CASCADE").select();
		bot.checkBox("SWT.CHECK").select();
		bot.checkBox("SWT.PUSH").select();
		bot.checkBox("SWT.RADIO").select();
		bot.checkBox("SWT.SEPARATOR").select();

		bot.checkBox("Sub-Menu").select();
		bot.checkBox("Sub-Sub-Menu").select();

		if (menuType.equals(MenuType.StaticMenu)) {
			bot.checkBox("Menu Manager").deselect();
			bot.button("Create Shell").click();
			popupShell = bot.shell("Title:0");
		} else if (menuType.equals(MenuType.DynamicMenu)) {
			bot.checkBox("Menu Manager").select();
			bot.checkBox("Dynamic").select();
			bot.button("Create Shell").click();
			popupShell = bot.shell("Title:0");
		}
	}

	@After
	public void tearDown() throws Exception {
		bot.button("Close All Shells").click();
	}
}
