/*******************************************************************************
 * Copyright (c) 2008, 2016 http://www.inria.fr/ and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     http://www.inria.fr/ - initial API and implementation
 *     Kristine Jetzke - Bug 379185
 *     Patrick Tasse - Add test for doubleClick()
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertText;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertTextContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Vincent MAHE &lt;vmahe [at] free [dot]fr&gt;
 * @version $Id$
 */
public class SWTBotTableItemTest extends AbstractControlExampleTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private SWTBotTable	table;
	private SWTBotText	listeners;

	@Test
	public void canRightClickOnALine() throws Exception {
		SWTBotTableItem line = table.getTableItem("Index:2");
		bot.button("Clear").click();
		line.contextMenu("getItem(Point) on mouse coordinates").click();
		assertTextContains("Selection [13]: SelectionEvent{Table {} ", listeners);
		assertTextContains("MenuDetect [35]: Event {type=35 Table {}", listeners);
		assertTextContains("getItem(Point(Point {", listeners);
	}

	@Test
	public void canFindALine() throws Exception {
		SWTBotTableItem line = table.getTableItem("Index:2");
		assertText("Index:2", line);
	}

	@Test
	public void canGetLineText() throws Exception {
		SWTBotTableItem line = table.getTableItem("Index:2");
		assertEquals("Index:2", line.getText());
	}

	@Test
	public void canGetTableItemText() throws Exception {
		SWTBotTableItem line = table.getTableItem("Index:0");
		assertEquals("classes", line.getText(1));
	}
	
	@Test
	public void checkingATableThatDoesNotHaveCheckStyleBitsThrowsException() throws Exception {
		try {
			table.getTableItem("Index:2").check();
			fail("Expecting an exception");
		} catch (IllegalArgumentException e) {
			assertEquals("The table does not have the style SWT.CHECK", e.getMessage());
		}
	}

	@Test
	public void canCheckALine() throws Exception {
		bot.checkBox("SWT.CHECK").select();
		table = bot.table();
		SWTBotTableItem item = table.getTableItem("Index:2");
		item.check();
		assertTrue(table.getTableItem("Index:2").isChecked());
		assertTextContains("Selection [13]: SelectionEvent{Table {} ", listeners);
		assertTextContains("data=null item=TableItem {Index:2} detail=32", listeners);
	}

	@Test
	public void canUnCheckALine() throws Exception {
		bot.checkBox("SWT.CHECK").select();
		table = bot.table();
		SWTBotTableItem item = table.getTableItem("Index:2");
		item.uncheck();
		assertFalse(table.getTableItem("Index:2").isChecked());
		assertTextContains("Selection [13]: SelectionEvent{Table {} ", listeners);
		assertTextContains("data=null item=TableItem {Index:2} detail=32", listeners);
	}

	@Test
	public void canToggleALine() throws Exception {
		bot.checkBox("SWT.CHECK").select();
		table = bot.table();
		SWTBotTableItem line = table.getTableItem("Index:2");
		assertFalse(line.isChecked());
		line.toggleCheck();
		assertTrue(line.isChecked());
		line.toggleCheck();
		assertFalse(line.isChecked());
	}

	@Test
	public void canSelectALine() throws Exception {
		SWTBotTableItem line = table.getTableItem("Index:2");
		bot.button("Clear").click();
		line.select();
		assertTextContains("Selection [13]: SelectionEvent{Table {} ", listeners);
	}

	@Test
	public void canClickALine() throws Exception {
		String ITEM_TEXT = "Index:2";
		SWTBotTableItem line = table.getTableItem(ITEM_TEXT);
		bot.button("Clear").click();
		line.click();
		assertEventMatches(listeners, "MouseEnter [6]: MouseEvent{Table {} time=0 data=null button=0 stateMask=0x0 x=0 y=0 count=0}");
		assertEventMatches(listeners, "Activate [26]: ShellEvent{Table {} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusIn [15]: FocusEvent{Table {} time=0 data=null}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Table {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=1}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Table {} time=0 data=null item=TableItem {Index:2} detail=0 x=0 y=0 width=0 height=0 stateMask=0x0 text=null doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Table {} time=0 data=null button=1 stateMask=0x80000 x=0 y=0 count=1}");
		assertEventMatches(listeners, "MouseExit [7]: MouseEvent{Table {} time=0 data=null button=0 stateMask=0x0 x=0 y=0 count=0}");
		assertEventMatches(listeners, "Deactivate [27]: ShellEvent{Table {} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusOut [16]: FocusEvent{Table {} time=0 data=null}");
		assertEquals(1, table.selectionCount());
		assertEquals(ITEM_TEXT, table.selection().get(0).get(0));
	}

	@Test
	public void canClickALineInAColumn() throws Exception {
		String ITEM_TEXT = "Index:2";
		SWTBotTableItem line = table.getTableItem(ITEM_TEXT);
		bot.button("Clear").click();
		line.click(2);
		assertEventMatches(listeners, "MouseEnter [6]: MouseEvent{Table {} time=0 data=null button=0 stateMask=0x0 x=0 y=0 count=0}");
		assertEventMatches(listeners, "Activate [26]: ShellEvent{Table {} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusIn [15]: FocusEvent{Table {} time=0 data=null}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Table {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=1}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Table {} time=0 data=null item=TableItem {Index:2} detail=0 x=0 y=0 width=0 height=0 stateMask=0x0 text=null doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Table {} time=0 data=null button=1 stateMask=0x80000 x=0 y=0 count=1}");
		assertEventMatches(listeners, "MouseExit [7]: MouseEvent{Table {} time=0 data=null button=0 stateMask=0x0 x=0 y=0 count=0}");
		assertEventMatches(listeners, "Deactivate [27]: ShellEvent{Table {} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusOut [16]: FocusEvent{Table {} time=0 data=null}");
		Point p = getCellCenter(line, 2);
		assertTextContains(" x=" + p.x + " y=" + p.y + " ", listeners);
		assertEquals(1, table.selectionCount());
		assertEquals(ITEM_TEXT, table.selection().get(0).get(0));
	}

	@Test
	public void canDoubleClickALine() throws Exception {
		String ITEM_TEXT = "Index:2";
		SWTBotTableItem line = table.getTableItem(ITEM_TEXT);
		bot.button("Clear").click();
		line.doubleClick();
		assertEventMatches(listeners, "MouseEnter [6]: MouseEvent{Table {} time=0 data=null button=0 stateMask=0x0 x=0 y=0 count=0}");
		assertEventMatches(listeners, "Activate [26]: ShellEvent{Table {} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusIn [15]: FocusEvent{Table {} time=0 data=null}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Table {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=1}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Table {} time=0 data=null item=TableItem {Index:2} detail=0 x=0 y=0 width=0 height=0 stateMask=0x0 text=null doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Table {} time=0 data=null button=1 stateMask=0x80000 x=0 y=0 count=1}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Table {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=2}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Table {} time=0 data=null item=TableItem {Index:2} detail=0 x=0 y=0 width=0 height=0 stateMask=0x0 text=null doit=true}");
		assertEventMatches(listeners, "MouseDoubleClick [8]: MouseEvent{Table {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=2}");
		assertEventMatches(listeners, "DefaultSelection [14]: SelectionEvent{Table {} time=0 data=null item=TableItem {Index:2} detail=0 x=0 y=0 width=0 height=0 stateMask=0x0 text=null doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Table {} time=0 data=null button=1 stateMask=0x80000 x=0 y=0 count=2}");
		assertEventMatches(listeners, "MouseExit [7]: MouseEvent{Table {} time=0 data=null button=0 stateMask=0x0 x=0 y=0 count=0}");
		assertEventMatches(listeners, "Deactivate [27]: ShellEvent{Table {} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusOut [16]: FocusEvent{Table {} time=0 data=null}");
		assertEquals(1, table.selectionCount());
		assertEquals(ITEM_TEXT, table.selection().get(0).get(0));
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("Table").activate();
		bot.checkBox("Horizontal Fill").select();
		bot.checkBox("Vertical Fill").select();
		bot.checkBox("Popup Menu").select();
		bot.checkBox("Listen").deselect();
		bot.checkBox("SWT.CHECK").deselect();
		bot.checkBox("Listen").select();
		table = bot.table();
		listeners = bot.textInGroup("Listeners");
	}
	
	@Test
	public void isGrayed() throws Exception {
		SWTBotTableItem itemGrayed = bot.tableInGroup("Table_Checked").getTableItem(0);
		assertTrue(itemGrayed.isGrayed());
		SWTBotTableItem itemNotGrayed = bot.table(2).getTableItem(1);
		assertFalse(itemNotGrayed.isGrayed());
	}
	
	@Test
	public void isGrayedTableNotChecked() throws Exception {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("does not have the style SWT.CHECK");
		
		table.getTableItem(0).isGrayed();
	}

	private Point getCellCenter(final SWTBotTableItem node, final int columnIndex) {
		 return UIThreadRunnable.syncExec(new Result<Point>() {
				@Override
				public Point run() {
					Rectangle bounds = node.widget.getBounds(columnIndex);
					return new Point(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
				}
			});
	}
}
