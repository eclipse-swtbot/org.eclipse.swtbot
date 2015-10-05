/*******************************************************************************
 * Copyright (c) 2008, 2015 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Patrick Tasse - Fix radio menu item click behavior (Bug 451126 & Bug 397649)
 *                   - Test dynamic menus
 *                   - Improve SWTBot menu API and implementation (Bug 479091) 
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.utils.SWTUtils.getTextPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertArrayEquals;
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

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
@RunWith(Parameterized.class)
public class SWTBotPopupMenuTest extends AbstractControlExampleTest {

	private SWTBotShell	popupShell;
	private SWTBotText	listeners;
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
		SWTBotMenu menuItem;

		menuItem = popupShell.contextMenu().menu("Push").hide();
		assertArrayEquals(new String[] { "POP_UP", "Push" }, getTextPath(menuItem.widget));
		menuItem = popupShell.contextMenu().menu("Check").hide();
		assertArrayEquals(new String[] { "POP_UP", "Check" }, getTextPath(menuItem.widget));
		menuItem = popupShell.contextMenu().menu("Radio1").hide();
		assertArrayEquals(new String[] { "POP_UP", "Radio1" }, getTextPath(menuItem.widget));
		menuItem = popupShell.contextMenu().menu("Radio2").hide();
		assertArrayEquals(new String[] { "POP_UP", "Radio2" }, getTextPath(menuItem.widget));
		menuItem = popupShell.contextMenu().menu("Cascade").hide();
		assertArrayEquals(new String[] { "POP_UP", "Cascade" }, getTextPath(menuItem.widget));
	}

	@Test
	public void findsSubMenus() throws Exception {
		SWTBotMenu menuItem;

		menuItem = popupShell.contextMenu().menu("Cascade").menu("Push").hide();
		assertArrayEquals(new String[] { "POP_UP", "Cascade", "Push" }, getTextPath(menuItem.widget));
		menuItem = popupShell.contextMenu().menu("Cascade").menu("Check").hide();
		assertArrayEquals(new String[] { "POP_UP", "Cascade", "Check" }, getTextPath(menuItem.widget));
		menuItem = popupShell.contextMenu().menu("Cascade").menu("Radio1").hide();
		assertArrayEquals(new String[] { "POP_UP", "Cascade", "Radio1" }, getTextPath(menuItem.widget));
		menuItem = popupShell.contextMenu().menu("Cascade").menu("Radio2").hide();
		assertArrayEquals(new String[] { "POP_UP", "Cascade", "Radio2" }, getTextPath(menuItem.widget));
		menuItem = popupShell.contextMenu().menu("Cascade").menu("Cascade").hide();
		assertArrayEquals(new String[] { "POP_UP", "Cascade", "Cascade" }, getTextPath(menuItem.widget));

		menuItem = popupShell.contextMenu().menu("Cascade", "Cascade", "Push").hide();
		assertArrayEquals(new String[] { "POP_UP", "Cascade", "Cascade", "Push" }, getTextPath(menuItem.widget));
		menuItem = popupShell.contextMenu().menu("Cascade", "Cascade", "Check").hide();
		assertArrayEquals(new String[] { "POP_UP", "Cascade", "Cascade", "Check" }, getTextPath(menuItem.widget));
		menuItem = popupShell.contextMenu().menu("Cascade", "Cascade", "Radio1").hide();
		assertArrayEquals(new String[] { "POP_UP", "Cascade", "Cascade", "Radio1" }, getTextPath(menuItem.widget));
		menuItem = popupShell.contextMenu().menu("Cascade", "Cascade", "Radio2").hide();
		assertArrayEquals(new String[] { "POP_UP", "Cascade", "Cascade", "Radio2" }, getTextPath(menuItem.widget));
	}

	@Test
	public void findsMenusRecursive() throws Exception {
		SWTBotMenu menuItem;

		menuItem = popupShell.contextMenu("Push").hide();
		assertArrayEquals(new String[] { "POP_UP", "Push" }, getTextPath(menuItem.widget));
		menuItem = popupShell.contextMenu().menu("Push", true, 0).hide();
		assertArrayEquals(new String[] { "POP_UP", "Push" }, getTextPath(menuItem.widget));
		menuItem = popupShell.contextMenu().menu("Push", true, 1).hide();
		assertArrayEquals(new String[] { "POP_UP", "Cascade", "Push" }, getTextPath(menuItem.widget));
		menuItem = popupShell.contextMenu().menu("Push", true, 2).hide();
		assertArrayEquals(new String[] { "POP_UP", "Cascade", "Cascade", "Push" }, getTextPath(menuItem.widget));
	}

	@Test
	public void clicksMenu() throws Exception {
		SWTBotRootMenu popupMenu = popupShell.contextMenu();
		assertThat(listeners.getText(), containsString("MenuDetect [35]: Event {type=35 Shell {Title:0} time="));
		assertThat(listeners.getText(), containsString("Show [22]: MenuEvent{Menu {Push, |, Check, Radio1, Radio2, Cascade} time="));
		bot.button("Clear").click();

		SWTBotMenu menuItem = popupMenu.menu("Push");
		assertThat(listeners.getText(), containsString("Arm [30]: ArmEvent{MenuItem {Push} time="));
		bot.button("Clear").click();

		menuItem.click();
		assertThat(listeners.getText(), containsString("Hide [23]: MenuEvent{Menu {Push, |, Check, Radio1, Radio2, Cascade} time="));
		assertThat(listeners.getText(), containsString("Selection [13]: SelectionEvent{MenuItem {Push} time="));
		assertThat(listeners.getText(), containsString("Clicked on menu item: POP_UP > Push"));
	}

	@Test
	public void clicksSubMenu() throws Exception {
		SWTBotRootMenu popupMenu = popupShell.contextMenu();
		assertThat(listeners.getText(), containsString("MenuDetect [35]: Event {type=35 Shell {Title:0} time="));
		assertThat(listeners.getText(), containsString("Show [22]: MenuEvent{Menu {Push, |, Check, Radio1, Radio2, Cascade} time="));
		bot.button("Clear").click();

		SWTBotMenu cascade1 = popupMenu.menu("Cascade");
		assertThat(listeners.getText(), containsString("Arm [30]: ArmEvent{MenuItem {Cascade} time="));
		assertThat(listeners.getText(), containsString("Show [22]: MenuEvent{Menu {Push, |, Check, Radio1, Radio2, Cascade} time="));
		bot.button("Clear").click();

		SWTBotMenu cascade2 = cascade1.menu("Cascade");
		assertThat(listeners.getText(), containsString("Arm [30]: ArmEvent{MenuItem {Cascade} time="));
		assertThat(listeners.getText(), containsString("Show [22]: MenuEvent{Menu {Push, |, Check, Radio1, Radio2} time="));
		bot.button("Clear").click();

		SWTBotMenu menuItem = cascade2.menu("Push");
		assertThat(listeners.getText(), containsString("Arm [30]: ArmEvent{MenuItem {Push} time="));
		bot.button("Clear").click();

		menuItem.click();
		assertThat(listeners.getText(), containsString("Hide [23]: MenuEvent{Menu {Push, |, Check, Radio1, Radio2} time="));
		assertThat(listeners.getText(), containsString("Hide [23]: MenuEvent{Menu {Push, |, Check, Radio1, Radio2, Cascade} time="));
		assertThat(listeners.getText(), containsString("Hide [23]: MenuEvent{Menu {Push, |, Check, Radio1, Radio2, Cascade} time="));
		assertThat(listeners.getText(), containsString("Selection [13]: SelectionEvent{MenuItem {Push} time="));
		assertThat(listeners.getText(), containsString("Clicked on menu item: POP_UP > Cascade > Cascade > Push"));
	}

	@Test
	public void clicksCheckMenuItem() throws Exception {
		SWTBotMenu item = popupShell.contextMenu("Check").hide();
		boolean checked = item.isChecked();
		popupShell.contextMenu("Check").click();
		assertTrue(checked != item.isChecked());
		popupShell.contextMenu("Check").click();
		assertTrue(checked == item.isChecked());
	}

	@Test
	public void clicksRadioMenuItem() throws Exception {
		SWTBotMenu item1 = popupShell.contextMenu("Radio1").hide();
		SWTBotMenu item2 = popupShell.contextMenu("Radio2").hide();
		popupShell.contextMenu("Radio1").click();
		assertTrue(item1.isChecked());
		assertTrue(!item2.isChecked());
		popupShell.contextMenu("Radio1").click();
		assertTrue(item1.isChecked());
		assertTrue(!item2.isChecked());
		popupShell.contextMenu("Radio2").click();
		assertTrue(!item1.isChecked());
		assertTrue(item2.isChecked());
	}

	@Before
	public void setUp() throws Exception {
		bot = new SWTBot().activeShell().bot();
		bot.tabItem("Menu").activate();

		bot.checkBox("Listen").select();
		listeners = bot.textInGroup("Listeners");

		bot.checkBox("SWT.POP_UP").select();
		bot.checkBox("SWT.DROP_DOWN").select();

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
		bot.button("Clear").click();
	}

	@After
	public void tearDown() throws Exception {
		bot.button("Close All Shells").click();
	}
}
