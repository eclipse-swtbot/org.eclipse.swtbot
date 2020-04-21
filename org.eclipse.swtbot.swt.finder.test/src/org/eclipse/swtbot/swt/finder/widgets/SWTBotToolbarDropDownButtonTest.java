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
 *     Ketan Padegaonkar - http://swtbot.org/bugzilla/show_bug.cgi?id=88
 *     Patrick Tasse - support click with modifiers
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertNotSameWidget;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.pass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotToolbarDropDownButtonTest extends AbstractControlExampleTest {

	@Test
	public void findsToolBarButtonWithIndex() throws Exception {
		SWTBotToolbarDropDownButton button0 = bot.toolbarDropDownButton("Drop Down");
		SWTBotToolbarDropDownButton button1 = bot.toolbarDropDownButton("Drop Down", 1);
		assertNotSameWidget(button0.widget, button1.widget);
	}

	@Test
	public void clicksToolBarButton() throws Exception {
		try {
			bot.checkBox("Listen").select();
			SWTBotToolbarDropDownButton button = bot.toolbarDropDownButton("Drop Down");
			button.click();
			assertEventMatches(bot.textInGroup("Listeners"), "Selection [13]: SelectionEvent{ToolItem {Drop Down} time=0 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, button.widget) + " text=null doit=true}");
		} finally {
			bot.checkBox("Listen").deselect();
		}
	}

	@Test
	public void clicksToolBarButtonWithModifier() throws Exception {
		try {
			bot.checkBox("Listen").select();
			SWTBotToolbarDropDownButton button = bot.toolbarDropDownButton("Drop Down");
			button.click(SWT.SHIFT);
			assertEventMatches(bot.textInGroup("Listeners"), "Selection [13]: SelectionEvent{ToolItem {Drop Down} time=0 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(SWT.SHIFT, button.widget) + " text=null doit=true}");
		} finally {
			bot.checkBox("Listen").deselect();
		}
	}

	@Test
	@Ignore("Broken on cocoa")
	public void getsAllMenuItems() throws Exception {
		List<? extends SWTBotMenu> menuItems = bot.toolbarDropDownButton("Drop Down").menuItems(anyMenuItem);
		menuItems.get(0).click();
		assertEquals(7, menuItems.size());
	}

	@Test
	@Ignore
	public void clicksADropDownMenuItem() throws Exception {
		SWTBotToolbarDropDownButton button = bot.toolbarDropDownButton("Drop Down");
		try {
			bot.menu("Kiwi");
			fail("The menu item should not exist");
		} catch (WidgetNotFoundException e) {
			pass();
		}
		bot.shell("SWT Controls").activate();
		button.menuItem("Kiwi").click();
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("ToolBar").activate();
	}
}
