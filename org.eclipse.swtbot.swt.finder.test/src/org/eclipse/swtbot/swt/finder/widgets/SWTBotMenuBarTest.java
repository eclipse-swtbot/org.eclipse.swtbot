/*******************************************************************************
 * Copyright (c) 2008, 2016 Ketan Padegaonkar and others.
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
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.utils.SWTUtils.getTextPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SWTBotMenuBarTest extends AbstractControlExampleTest {

	private SWTBotText	listeners;
	private MenuType menuType;

	private enum MenuType {
		StaticMenu, DynamicMenu
	}

	@Parameters(name = "{0}")
	public static Collection<MenuType> parameters() {
		return Arrays.asList(MenuType.values());
	}

	public SWTBotMenuBarTest(MenuType menuType) {
		this.menuType = menuType;
	}

	@Test
	public void findsMenus() throws Exception {
		SWTBotMenu menuItem;

		menuItem = bot.menu().menu("Cascade").menu("Push").hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "&Push\tCtrl+Shift+P" }, getTextPath(menuItem.widget));
		menuItem = bot.menu().menu("Cascade").menu("Check").hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "&Check\tCtrl+Shift+C" }, getTextPath(menuItem.widget));
		menuItem = bot.menu().menu("Cascade").menu("Radio1").hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "Radio&1\tCtrl+Shift+1" }, getTextPath(menuItem.widget));
		menuItem = bot.menu().menu("Cascade").menu("Radio2").hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "Radio&2\tCtrl+Shift+2" }, getTextPath(menuItem.widget));
		menuItem = bot.menu().menu("Cascade").menu("Cascade").hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "C&ascade" }, getTextPath(menuItem.widget));
	}

	@Test
	public void findsSubMenus() throws Exception {
		SWTBotMenu menuItem;

		menuItem = bot.menu().menu("Cascade").menu("Cascade").menu("Push").hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "C&ascade", "&Push\tCtrl+Shift+P" }, getTextPath(menuItem.widget));
		menuItem = bot.menu().menu("Cascade").menu("Cascade").menu("Check").hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "C&ascade", "&Check\tCtrl+Shift+C" }, getTextPath(menuItem.widget));
		menuItem = bot.menu().menu("Cascade").menu("Cascade").menu("Radio1").hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "C&ascade", "Radio&1\tCtrl+Shift+1" }, getTextPath(menuItem.widget));
		menuItem = bot.menu().menu("Cascade").menu("Cascade").menu("Radio2").hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "C&ascade", "Radio&2\tCtrl+Shift+2" }, getTextPath(menuItem.widget));
		menuItem = bot.menu().menu("Cascade").menu("Cascade").menu("Cascade").hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "C&ascade", "C&ascade" }, getTextPath(menuItem.widget));

		menuItem = bot.menu().menu("Cascade").menu("Cascade", "Cascade", "Push").hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "C&ascade", "C&ascade", "&Push\tCtrl+Shift+P" }, getTextPath(menuItem.widget));
		menuItem = bot.menu().menu("Cascade").menu("Cascade", "Cascade", "Check").hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "C&ascade", "C&ascade", "&Check\tCtrl+Shift+C" }, getTextPath(menuItem.widget));
		menuItem = bot.menu().menu("Cascade").menu("Cascade", "Cascade", "Radio1").hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "C&ascade", "C&ascade", "Radio&1\tCtrl+Shift+1" }, getTextPath(menuItem.widget));
		menuItem = bot.menu().menu("Cascade").menu("Cascade", "Cascade", "Radio2").hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "C&ascade", "C&ascade", "Radio&2\tCtrl+Shift+2" }, getTextPath(menuItem.widget));
	}

	@Test
	public void findsMenusRecursive() throws Exception {
		SWTBotMenu menuItem;

		menuItem = bot.menu("Push").hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "&Push\tCtrl+Shift+P" }, getTextPath(menuItem.widget));
		menuItem = bot.menu().menu("Cascade").menu("Push", true, 0).hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "&Push\tCtrl+Shift+P" }, getTextPath(menuItem.widget));
		menuItem = bot.menu().menu("Cascade").menu("Push", true, 1).hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "C&ascade", "&Push\tCtrl+Shift+P" }, getTextPath(menuItem.widget));
		menuItem = bot.menu().menu("Cascade").menu("Push", true, 2).hide();
		assertArrayEquals(new String[] { "BAR", "C&ascade", "C&ascade", "C&ascade", "&Push\tCtrl+Shift+P" }, getTextPath(menuItem.widget));
	}

	@Test
	public void clicksMenu() throws Exception {
		SWTBotRootMenu menuBar = bot.menu();
		SWTBotMenu cascade = menuBar.menu("Cascade");
		bot.button("Clear").click();

		SWTBotMenu menuItem = cascade.menu("Push");
		assertThat(listeners.getText(), containsString("Arm [30]: ArmEvent{MenuItem {&Push\tCtrl+Shift+P} time="));
		bot.button("Clear").click();

		menuItem.click();
		assertThat(listeners.getText(), containsString("Hide [23]: MenuEvent{Menu {&Push\tCtrl+Shift+P, |, &Check\tCtrl+Shift+C, Radio&1\tCtrl+Shift+1, Radio&2\tCtrl+Shift+2, C&ascade} time="));
		assertThat(listeners.getText(), containsString("Selection [13]: SelectionEvent{MenuItem {&Push\tCtrl+Shift+P} time="));
		assertThat(listeners.getText(), containsString("Clicked on menu item: BAR > C&ascade > &Push\tCtrl+Shift+P"));
	}

	@Test
	public void clicksSubMenu() throws Exception {
		SWTBotRootMenu menuBar = bot.menu();
		SWTBotMenu cascade = menuBar.menu("Cascade");
		bot.button("Clear").click();

		SWTBotMenu cascade1 = cascade.menu("Cascade");
		assertThat(listeners.getText(), containsString("Arm [30]: ArmEvent{MenuItem {C&ascade} time="));
		assertThat(listeners.getText(), containsString("Show [22]: MenuEvent{Menu {&Push\tCtrl+Shift+P, |, &Check\tCtrl+Shift+C, Radio&1\tCtrl+Shift+1, Radio&2\tCtrl+Shift+2, C&ascade} time="));
		bot.button("Clear").click();

		SWTBotMenu cascade2 = cascade1.menu("Cascade");
		assertThat(listeners.getText(), containsString("Arm [30]: ArmEvent{MenuItem {C&ascade} time="));
		assertThat(listeners.getText(), containsString("Show [22]: MenuEvent{Menu {&Push\tCtrl+Shift+P, |, &Check\tCtrl+Shift+C, Radio&1\tCtrl+Shift+1, Radio&2\tCtrl+Shift+2} time="));
		bot.button("Clear").click();

		SWTBotMenu menuItem = cascade2.menu("Push");
		assertThat(listeners.getText(), containsString("Arm [30]: ArmEvent{MenuItem {&Push\tCtrl+Shift+P} time="));
		bot.button("Clear").click();

		menuItem.click();
		assertThat(listeners.getText(), containsString("Hide [23]: MenuEvent{Menu {&Push\tCtrl+Shift+P, |, &Check\tCtrl+Shift+C, Radio&1\tCtrl+Shift+1, Radio&2\tCtrl+Shift+2} time="));
		assertThat(listeners.getText(), containsString("Hide [23]: MenuEvent{Menu {&Push\tCtrl+Shift+P, |, &Check\tCtrl+Shift+C, Radio&1\tCtrl+Shift+1, Radio&2\tCtrl+Shift+2, C&ascade} time="));
		assertThat(listeners.getText(), containsString("Hide [23]: MenuEvent{Menu {&Push\tCtrl+Shift+P, |, &Check\tCtrl+Shift+C, Radio&1\tCtrl+Shift+1, Radio&2\tCtrl+Shift+2, C&ascade} time="));
		assertThat(listeners.getText(), containsString("Selection [13]: SelectionEvent{MenuItem {&Push\tCtrl+Shift+P} time="));
		assertThat(listeners.getText(), containsString("Clicked on menu item: BAR > C&ascade > C&ascade > C&ascade > &Push\tCtrl+Shift+P"));
	}

	@Test
	public void clicksCheckMenuItem() throws Exception {
		SWTBotMenu item = bot.menu("Check").hide();
		boolean checked = item.isChecked();
		bot.menu("Check").click();
		assertTrue(checked != item.isChecked());
		bot.menu("Check").click();
		assertTrue(checked == item.isChecked());
	}

	@Test
	public void clicksRadioMenuItem() throws Exception {
		SWTBotMenu item1 = bot.menu("Radio1").hide();
		SWTBotMenu item2 = bot.menu("Radio2").hide();
		bot.menu("Radio1").click();
		assertTrue(item1.isChecked());
		assertTrue(!item2.isChecked());
		bot.menu("Radio1").click();
		assertTrue(item1.isChecked());
		assertTrue(!item2.isChecked());
		bot.menu("Radio2").click();
		assertTrue(!item1.isChecked());
		assertTrue(item2.isChecked());
	}

	@Test
	public void testMenuItems() {
		SWTBotRootMenu menuBar = bot.menu();
		assertEquals(Arrays.asList("Cascade"), menuBar.menuItems());

		SWTBotMenu cascade = menuBar.menu("Cascade");
		assertEquals(Arrays.asList("Push", "", "Check", "Radio1", "Radio2", "Cascade"), cascade.menuItems());

		SWTBotMenu push = cascade.menu("Push");
		assertEquals(Arrays.asList(), push.menuItems());
	}

	@Before
	public void setUp() throws Exception {
		bot = new SWTBot().activeShell().bot();
		bot.tabItem("Menu").activate();

		bot.checkBox("Listen").select();
		listeners = bot.textInGroup("Listeners");

		bot.checkBox("SWT.BAR").select();
		bot.checkBox("SWT.DROP_DOWN").select();

		bot.checkBox("SWT.CASCADE").select();
		bot.checkBox("SWT.CHECK").select();
		bot.checkBox("SWT.PUSH").select();
		bot.checkBox("SWT.RADIO").select();
		bot.checkBox("SWT.SEPARATOR").select();

		bot.checkBox("Images").select();
		bot.checkBox("Accelerators").select();
		bot.checkBox("Mnemonics").select();
		bot.checkBox("Sub-Menu").select();
		bot.checkBox("Sub-Sub-Menu").select();

		if (menuType.equals(MenuType.StaticMenu)) {
			bot.checkBox("Menu Manager").deselect();
			bot.button("Create Shell").click();
			bot.shell("Title:0").activate();
		} else if (menuType.equals(MenuType.DynamicMenu)) {
			bot.checkBox("Menu Manager").select();
			bot.checkBox("Dynamic").select();
			bot.button("Create Shell").click();
			bot.shell("Title:0").activate();
		}
		bot.button("Clear").click();
	}

	@After
	public void tearDown() throws Exception {
		bot.button("Close All Shells").click();
	}
}
