/*******************************************************************************
 * Copyright (c) 2017 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Aparna Argade - Bug 526768
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.junit.Before;
import org.junit.Test;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertTextContains;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 */
public class SWTBotExpandBarTest extends AbstractControlExampleTest {

	@Test
	public void shouldGetExpandItemCount() throws Exception {
		assertEquals(2, bot.expandBar().itemCount());
	}

	@Test
	public void shouldGetExpandedAndCollapsedItemCount() throws Exception {
		SWTBotExpandBar expandBar = bot.expandBar();
		assertEquals(1, expandBar.expandedItemCount());
		assertEquals(1, expandBar.collapsedItemCount());
	}

	@Test
	public void shouldExpandAnItem() throws Exception {
		SWTBotExpandBar expandBar = bot.expandBar();
		SWTBotExpandItem item1 = expandBar.expandItem("What is your favorite button?");
		SWTBotExpandItem item2 = expandBar.expandItem("What is your favorite icon?");
		assertEquals(2, expandBar.expandedItemCount());
		assertEquals(0, expandBar.collapsedItemCount());

		assertTrue(item1.isExpanded());
		assertTrue(item2.isExpanded());

		assertFalse(item1.isCollapsed());
		assertFalse(item2.isCollapsed());
	}

	@Test
	public void shouldCollapseAnItem() throws Exception {
		SWTBotExpandBar expandBar = bot.expandBar();
		SWTBotExpandItem item1 = expandBar.collapseItem("What is your favorite button?");
		SWTBotExpandItem item2 = expandBar.collapseItem("What is your favorite icon?");
		assertEquals(0, expandBar.expandedItemCount());
		assertEquals(2, expandBar.collapsedItemCount());

		assertFalse(item1.isExpanded());
		assertFalse(item2.isExpanded());

		assertTrue(item1.isCollapsed());
		assertTrue(item2.isCollapsed());
	}

	@Test
	public void shouldGetAllExpandBarItems() throws Exception {
		List<SWTBotExpandItem> items = bot.expandBar().getAllItems();
		assertEquals(2, items.size());
		assertEquals("What is your favorite button?", items.get(0).getText());
		assertEquals("What is your favorite icon?", items.get(1).getText());
	}

	@Test
	public void shouldThrowExceptionWhenItemIsNotFound() throws Exception {
		// a custom timeout could have been specified so we save it...
		long currentTimeout = SWTBotPreferences.TIMEOUT;
		// ...and we set it to the expected 5 seconds
		SWTBotPreferences.TIMEOUT = 5000;

		try {
			bot.expandBar().getExpandItem(withText("some text"));
			fail("Expected WNFE");
		} catch (WidgetNotFoundException e) {
			assertEquals("Could not find widget matching: (of type 'ExpandItem' and with text 'some text')", e.getMessage());
			assertEquals("Timeout after: 5000 ms.: Could not find widget matching: (of type 'ExpandItem' and with text 'some text')", e
					.getCause().getMessage());
		}

		// and we reset the possible custom timeout
		SWTBotPreferences.TIMEOUT = currentTimeout;
	}

	@Test
	public void shouldMatchEvents() throws Exception {
		bot.checkBox("Listen").select();
		SWTBotExpandBar expandBar = bot.expandBar();
		expandBar.expandItem("What is your favorite button?");
		SWTBotText text = bot.textInGroup("Listeners");
		assertTextContains("Activate [26]: ShellEvent{ExpandBar {} time=", text.widget);
		assertTextContains("FocusIn [15]: FocusEvent{ExpandBar {} time=", text.widget);
		assertTextContains("MouseDown [3]: MouseEvent{ExpandBar {} time=", text.widget);
		assertEventMatches(text,
				"Expand [17]: TreeEvent{ExpandBar {} time=0 data=null item=ExpandItem {What is your favorite button?} detail=0 x=0 y=0 width=0 height=0 stateMask=0x0 text=null doit=true}");
		assertTextContains("MouseUp [4]: MouseEvent{ExpandBar {} time=", text.widget);

		bot.button("Clear").click();
		expandBar.collapseItem("What is your favorite button?");
		assertTextContains("Activate [26]: ShellEvent{ExpandBar {} time=", text.widget);
		assertTextContains("FocusIn [15]: FocusEvent{ExpandBar {} time=", text.widget);
		assertTextContains("MouseDown [3]: MouseEvent{ExpandBar {} time=", text.widget);
		assertEventMatches(text,
				"Collapse [18]: TreeEvent{ExpandBar {} time=0 data=null item=ExpandItem {What is your favorite button?} detail=0 x=0 y=0 width=0 height=0 stateMask=0x0 text=null doit=true}");
		assertTextContains("MouseUp [4]: MouseEvent{ExpandBar {} time=", text.widget);
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("ExpandBar").activate();
	}

}
