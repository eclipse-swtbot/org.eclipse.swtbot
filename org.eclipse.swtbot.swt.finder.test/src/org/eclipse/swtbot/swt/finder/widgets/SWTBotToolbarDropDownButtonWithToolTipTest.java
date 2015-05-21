/*******************************************************************************
 * Copyright (c) 2008, 2015 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Ketan Padegaonkar - http://swtbot.org/bugzilla/show_bug.cgi?id=126
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertNotSameWidget;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.pass;
import static org.junit.Assert.fail;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotToolbarDropDownButtonWithToolTipTest extends AbstractControlExampleTest {

	@Test
	public void findsToolBarButtonWithIndex() throws Exception {
		SWTBotToolbarDropDownButton button0 = bot.toolbarDropDownButtonWithTooltip("SWT.DROP_DOWN");
		SWTBotToolbarDropDownButton button1 = bot.toolbarDropDownButtonWithTooltip("SWT.DROP_DOWN", 1);
		assertNotSameWidget(button0.widget, button1.widget);
	}

	@Test
	public void clicksToolBarButton() throws Exception {
		try {
			bot.checkBox("Listen").select();
			SWTBotToolbarDropDownButton button = bot.toolbarDropDownButtonWithTooltip("SWT.DROP_DOWN");
			button.click();
			assertEventMatches(bot.textInGroup("Listeners"), "Selection [13]: SelectionEvent{ToolItem {} time=0 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, button.widget) + " text=null doit=true}");
		} finally {
			bot.checkBox("Listen").deselect();
		}
	}

	@Test
	@Ignore
	public void clicksADropDownMenuItem() throws Exception {
		SWTBotToolbarDropDownButton button = bot.toolbarDropDownButtonWithTooltip("SWT.DROP_DOWN");
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
