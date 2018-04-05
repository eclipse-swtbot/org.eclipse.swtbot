/*******************************************************************************
 * Copyright (c) 2008, 2018 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Aparna Argade - Bug 510835
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertText;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertTextContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.eclipse.swt.widgets.List;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.SWTBotAssert;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotListTest extends AbstractControlExampleTest {

	@Test
	public void findsAListWithoutLabel() throws Exception {
		SWTBotList list = bot.list();
		assertNotNull(list.widget);
		assertEquals(List.class, list.widget.getClass());
	}

	@Test
	public void setsAndGetsSingleSelectionByText() throws Exception {
		SWTBotList list = bot.listInGroup("List");
		list.select("Bananas");

		assertEquals(1, list.selectionCount());
		assertEquals("Bananas", list.selection()[0]);
	}

	@Test
	public void setsAndGetsSingleSelectionByIndex() throws Exception {
		SWTBotList list = bot.listInGroup("List");
		list.select(3);

		assertEquals(1, list.selectionCount());
		assertEquals("Grapefruit", list.selection()[0]);
	}

	@Test
	public void setsAndGetsMultipleSelectionByText() throws Exception {
		bot.radio("SWT.MULTI").click();
		SWTBotList list = bot.listInGroup("List");
		list.select(new String[] { "Grapefruit", "Peaches", "Apricots" });

		assertEquals(3, list.selectionCount());
		assertEquals("Grapefruit", list.selection()[0]);
		assertEquals("Peaches", list.selection()[1]);
		assertEquals("Apricots", list.selection()[2]);
	}

	@Test
	public void setsAndGetsMultipleSelectionByIndex() throws Exception {
		bot.radio("SWT.MULTI").click();
		SWTBotList list = bot.listInGroup("List");
		list.select(new int[] { 2, 4, 6 });

		assertEquals(3, list.selectionCount());
		assertEquals("Bananas", list.selection()[0]);
		assertEquals("Peaches", list.selection()[1]);
		assertEquals("Apricots", list.selection()[2]);
	}

	@Test
	public void unSelectsSelection() throws Exception {
		SWTBotList list = bot.listInGroup("List");
		list.select(3);
		assertEquals(1, list.selectionCount());
		try {
			list.unselect();
			fail("Expecting an exception");
		} catch (Exception e) {
			assertEquals("Single Select List does not support unselect", e.getMessage());
		}
	}

	@Test
	public void unSelectsSelectionMulti() throws Exception {
		bot.radio("SWT.MULTI").click();
		SWTBotList list = bot.listInGroup("List");
		list.select(new int[] {1, 2, 3});
		assertEquals(3, list.selectionCount());
		list.unselect(2);
		assertEquals(2, list.selectionCount());
		assertEquals("Oranges", list.selection()[0]);
		assertEquals("Grapefruit", list.selection()[1]);
		list.unselect();
		assertEquals(0, list.selectionCount());
	}

	@Test
	public void getsIndexOfItem() throws Exception {
		SWTBotList list = bot.listInGroup("List");
		assertEquals(2, list.indexOf("Bananas"));
	}

	@Test
	public void getsItemAtIndex() throws Exception {
		SWTBotList list = bot.listInGroup("List");
		assertEquals("Bananas", list.itemAt(2));
	}

	@Test
	public void selectionNotifiesListeners() throws Exception {
		SWTBotList list = bot.listInGroup("List");
		bot.checkBox("Listen").select();
		bot.button("Clear").click();

		list = bot.listInGroup("List");
		SWTBotText text = bot.textInGroup("Listeners");
		assertText("", text);

		list.select(3);
		verifyNotifySelect();
	}

	@Test
	public void selectionNotifiesListenersMulti() throws Exception {
		bot.radio("SWT.MULTI").click();
		bot.checkBox("Listen").select();
		bot.button("Clear").click();
		SWTBotList list = bot.listInGroup("List");

		SWTBotText text = bot.textInGroup("Listeners");
		assertText("", text);

		list.select(new int[] { 1, 3 });
		verifyNotifySelectMulti();
	}

	@Test
	public void deSelectNotifiesListeners() throws Exception {
		bot.radio("SWT.MULTI").click();
		bot.checkBox("Listen").select();
		bot.button("Clear").click();
		SWTBotList list = bot.listInGroup("List");
		SWTBotText text = bot.textInGroup("Listeners");
		list.unselect();
		//unselect should send no events when no items are already selected
		assertText("", text);

		list.select(new int[] { 2, 3});
		bot.button("Clear").click();
		assertText("", text);
		list.unselect();
		verifyNotifySelectMulti();
	}

	private void verifyNotifySelectMulti()
	{
		SWTBotText text = bot.textInGroup("Listeners");
		String[] events = SWTUtils.getText(text.widget).split("\n");
		int i = 0;
		SWTBotAssert.assertContains("MouseEnter [6]: MouseEvent{List {} ", events[i++]);
		SWTBotAssert.assertContains("Activate [26]: ShellEvent{List {} ", events[i++]);
		SWTBotAssert.assertContains("FocusIn [15]: FocusEvent{List {} ", events[i++]);
		SWTBotAssert.assertContains("MouseDown [3]: MouseEvent{List {} ", events[i]);
		SWTBotAssert.assertContains("stateMask=0x0", events[i++]);
		SWTBotAssert.assertContains("Selection [13]: SelectionEvent{List {} ", events[i]);
		SWTBotAssert.assertContains("stateMask=0x0", events[i++]);
		SWTBotAssert.assertContains("MouseUp [4]: MouseEvent{List {} ", events[i]);
		SWTBotAssert.assertContains("stateMask=0x80000", events[i++]);
		i = events[i].startsWith("Paint") ? (i + 1) : i;
		SWTBotAssert.assertContains("MouseDown [3]: MouseEvent{List {} ", events[i]);
		SWTBotAssert.assertContains("stateMask=0x40000", events[i++]);
		SWTBotAssert.assertContains("Selection [13]: SelectionEvent{List {} ", events[i]);
		SWTBotAssert.assertContains("stateMask=0x40000", events[i++]);
		SWTBotAssert.assertContains("MouseUp [4]: MouseEvent{List {} ", events[i]);
		SWTBotAssert.assertContains("stateMask=0xc0000", events[i++]);
	}

	private void verifyNotifySelect()
	{
		SWTBotText text = bot.textInGroup("Listeners");
		assertTextContains("MouseEnter [6]: MouseEvent{List {} ", text.widget);
		assertTextContains("Activate [26]: ShellEvent{List {} ", text.widget);
		assertTextContains("FocusIn [15]: FocusEvent{List {} ", text.widget);
		assertTextContains("Selection [13]: SelectionEvent{List {} ", text.widget);
		assertTextContains("MouseUp [4]: MouseEvent{List {} ", text.widget);
		assertTextContains("MouseDown [3]: MouseEvent{List {} ", text.widget);
	}

	private void verifynotifyPostSelectDoubleClick() {
		SWTBotText text = bot.textInGroup("Listeners");
		assertEventMatches(text,
				"MouseDown [3]: MouseEvent{List {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=1");
		assertEventMatches(text,
				"MouseDoubleClick [8]: MouseEvent{List {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=2}");
		assertEventMatches(text,
				"DefaultSelection [14]: SelectionEvent{List {} time=0 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=0x0 text=null doit=true");
		assertEventMatches(text,
				"MouseUp [4]: MouseEvent{List {} time=0 data=null button=1 stateMask=0x80000 x=0 y=0 count=1");
	}

	@Test
	public void canDoubleClickOnList() throws Exception {
		bot.checkBox("Listen").select();
		bot.button("Clear").click();
		SWTBotList list = bot.listInGroup("List");
		SWTBotText text = bot.textInGroup("Listeners");
		assertText("", text);
		final String item = "Bananas";
		list.doubleClick(item);
		assertEquals(1, list.selectionCount());
		assertEquals(item, list.selection()[0]);
		verifyNotifySelect();
		verifynotifyPostSelectDoubleClick();
	}

	@Test
	public void throwsExceptionInCaseOfInvalidIndexBasedSelection() throws Exception {
		SWTBot bot = new SWTBot();
		SWTBotList list = bot.listInGroup("List");
		try {
			list.select(100);
			fail("Was expecting an exception");
		} catch (RuntimeException e) {
			assertEquals("assertion failed: The index (100) is more than the number of items (9) in the list.", e.getMessage());
		}
	}

	@Test
	public void throwsExceptionInCaseOfInvalidTextBasedSelection() throws Exception {
		SWTBot bot = new SWTBot();
		SWTBotList list = bot.listInGroup("List");
		try {
			list.select("non existent item");
			fail("Was expecting an exception");
		} catch (RuntimeException e) {
			assertEquals("assertion failed: Item `non existent item' not found in list.", e.getMessage());
		}
	}

	@Test
	public void throwsExceptionInCaseOfInvalidIndexBasedSelectionMulti() throws Exception {
		SWTBot bot = new SWTBot();
		bot.radio("SWT.MULTI").click();
		SWTBotList list = bot.listInGroup("List");
		try {
			list.select(new int[] {1, 100});
			fail("Was expecting an exception");
		} catch (RuntimeException e) {
			assertEquals("assertion failed: The index (100) is more than the number of items (9) in the list.", e.getMessage());
		}
	}

	@Test
	public void throwsExceptionInCaseOfInvalidTextBasedSelectionMulti() throws Exception {
		SWTBot bot = new SWTBot();
		bot.radio("SWT.MULTI").click();
		SWTBotList list = bot.listInGroup("List");
		try {
			list.select(new String[] {"Bananas", "non existent item"});
			fail("Was expecting an exception");
		} catch (RuntimeException e) {
			assertEquals("assertion failed: Item `non existent item' not found in list.", e.getMessage());
		}
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("List").activate();
	}

}
