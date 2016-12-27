/*******************************************************************************
 * Copyright (c) 2008, 2017 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Aparna Argade - Bug 508710
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertSameWidget;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.eclipse.swtbot.swt.finder.utils.TableCollection;
import org.eclipse.swtbot.swt.finder.utils.TableRow;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotTableTest extends AbstractControlExampleTest {

	private SWTBotTable	table;

	@Test
	public void findsTable() throws Exception {
		assertEquals(Table.class, table.widget.getClass());
	}

	@Test
	public void getsRowCount() throws Exception {
		assertEquals(16, table.rowCount());
	}

	@Test
	public void getsColumnCount() throws Exception {
		assertEquals(4, table.columnCount());
	}

	@Test
	public void getsAllColumnHeadings() throws Exception {
		bot.checkBox("Header Visible").click();
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("Name");
		arrayList.add("Type");
		arrayList.add("Size");
		arrayList.add("Modified");
		assertEquals(arrayList, table.columns());
	}

	@Test
	public void getsColumnTextBasedOnRowColumnNumbers() throws Exception {
		assertEquals("2556", table.cell(1, 2));
		assertEquals("2556", table.cell(4, 2));
		assertEquals("2556", table.cell(7, 2));

		assertEquals("Index:5", table.cell(5, 0));
		assertEquals("tomorrow", table.cell(7, 3));
	}

	@Test
	public void getsTextCellBasedOnRowNumberColumnText() throws Exception {
		assertEquals("2556", table.cell(1, "Size"));
		assertEquals("2556", table.cell(4, "Size"));
		assertEquals("2556", table.cell(7, "Size"));

		assertEquals("Index:5", table.cell(5, "Name"));
		assertEquals("tomorrow", table.cell(7, "Modified"));
	}

	@Test
	public void getsSelectionCount() throws Exception {
		TableCollection selection = table.selection();
		assertEquals(new TableCollection(), selection);
		assertEquals(0, selection.columnCount());
		assertEquals(0, selection.rowCount());
		assertEquals(0, table.selectionCount());
	}

	@Test
	public void setsSelection() throws Exception {
		table.select(5);
		assertEquals(1, table.selectionCount());
	}

	@Test
	public void settingSelectionOnInvalidRowTextThrowsException() throws Exception {
		table.select("Index:15");
		try {
			table.select("Index:16");
			fail("Expecting an exception");
		} catch (Exception e) {
			assertEquals("Could not find item:Index:16 in table", e.getMessage());
		}
	}

	@Test
	public void settingSelectionOnInvalidRowNumberThrowsException() throws Exception {
		table.select(15);
		try {
			table.select(16);
			fail("Expecting an exception");
		} catch (Exception e) {
			assertEquals("The row number: 16 does not exist in the table", e.getMessage());
		}
	}

	@Test
	public void unselectsSelection() throws Exception {
		bot.radio("SWT.MULTI").click();
		bot.checkBox("Listen").select();
		bot.button("Clear").click();
		table = bot.tableInGroup("Table");
		table.select(new int[] { 1, 2, 3 });
		SWTBotText text = bot.textInGroup("Listeners");
		bot.button("Clear").click();

		table.unselect();
		assertEquals(0, table.selectionCount());
		//event.item should contain the item getting unselected, stateMask should reflect CTRL key
		assertEventMatches(text, "MouseDown [3]: MouseEvent{Table {} time=0 data=null button=1 stateMask=0x40000 x=0 y=0 count=1}");
		assertEventMatches(text, "Selection [13]: SelectionEvent{Table {} time=0 data=null item=TableItem {Index:1} detail=0 x=0 y=0 width=0 height=0 stateMask=0xc0000 text=null doit=true}");
		assertEventMatches(text, "MouseUp [4]: MouseEvent{Table {} time=0 data=null button=1 stateMask=0xc0000 x=0 y=0 count=1}");

		assertEventMatches(text, "Selection [13]: SelectionEvent{Table {} time=0 data=null item=TableItem {Index:2} detail=0 x=0 y=0 width=0 height=0 stateMask=0xc0000 text=null doit=true}");
		assertEventMatches(text, "Selection [13]: SelectionEvent{Table {} time=0 data=null item=TableItem {Index:3} detail=0 x=0 y=0 width=0 height=0 stateMask=0xc0000 text=null doit=true}");
	}

	@Test
	public void setEmptySelection() throws Exception {
		bot.radio("SWT.MULTI").click();
		table = bot.tableInGroup("Table");
		table.select(new int[] { 1, 2, 3 });
		assertEquals(3, table.selectionCount());
		table.select(new int[] {});
		assertEquals(0, table.selectionCount());

		table.select(new String[] { "Index:5", "Index:6" });
		assertEquals(2, table.selectionCount());
		table.select(new String[] {});
		assertEquals(0, table.selectionCount());
	}

	@Test
	public void getsSingleSelection() throws Exception {
		table.select(10);
		TableCollection selection = table.selection();
		assertEquals(1, selection.rowCount());
		assertEquals(4, selection.columnCount());
		assertEquals(new TableCollection().add(new TableRow(new String[] { "Index:10", "databases", "2556", "tomorrow" })), selection);
	}

	@Test
	public void findsAnyTable() throws Exception {
		assertSameWidget(table.widget, bot.table().widget);
	}

	@Test
	public void getsMultipleSingleSelection() throws Exception {
		bot.radio("SWT.MULTI").click();
		bot.checkBox("Listen").select();
		bot.button("Clear").click();
		table = bot.tableInGroup("Table");

		table.select(new int[] { 5, 10 });

		TableCollection selection = table.selection();
		assertEquals("Index:5", selection.get(0, 0));
		assertEquals("images", selection.get(0, 1));
		assertEquals("91571", selection.get(0, 2));
		assertEquals("yesterday", selection.get(0, 3));

		assertEquals(new TableRow(new String[] { "Index:10", "databases", "2556", "tomorrow" }), selection.get(1));

		SWTBotText text = bot.textInGroup("Listeners");

		// first selection: event.item should contain the first selected item,
		// stateMask should not reflect CTRL key
		assertEventMatches(text, "MouseDown [3]: MouseEvent{Table {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=1}");
		assertEventMatches(text, "Selection [13]: SelectionEvent{Table {} time=0 data=null item=TableItem {Index:5} detail=0 x=0 y=0 width=0 height=0 stateMask=0x80000 text=null doit=true}");
		assertEventMatches(text, "MouseUp [4]: MouseEvent{Table {} time=0 data=null button=1 stateMask=0x80000 x=0 y=0 count=1}");

		// subsequent selection: event.item should contain the second selected item,
		//stateMask should reflect CTRL key
		assertEventMatches(text, "MouseDown [3]: MouseEvent{Table {} time=0 data=null button=1 stateMask=0x40000 x=0 y=0 count=1}");
		assertEventMatches(text, "Selection [13]: SelectionEvent{Table {} time=0 data=null item=TableItem {Index:10} detail=0 x=0 y=0 width=0 height=0 stateMask=0xc0000 text=null doit=true}");
		assertEventMatches(text, "MouseUp [4]: MouseEvent{Table {} time=0 data=null button=1 stateMask=0xc0000 x=0 y=0 count=1}");
	}

	@Test
	public void settingMultipleSelectionOfInvalidIndicesThrowsException() throws Exception {
		bot.radio("SWT.MULTI").click();
		table = bot.tableInGroup("Table");
		table.select(new int[] { 4, 15, 6 });
		try {
			table.select(new int[] { 4, 16, 6 });
			fail("Expecting an exception");
		} catch (Exception e) {
			assertEquals("The row number: 16 does not exist in the table", e.getMessage());
		}
	}

	@Test
	public void throwsExceptionIfTheRowNumberIsIllegal() throws Exception {
		try {
			table.cell(100, 1);
			fail("Was expecting an exception");
		} catch (Exception e) {
			assertEquals("The row number (100) is more than the number of rows(16) in the table.", e.getMessage());
		}
	}

	@Test
	public void throwsExceptionIfTheRowNumberIsIllegal2() throws Exception {
		try {
			table.cell(100, "Size");
			fail("Was expecting an exception");
		} catch (Exception e) {
			assertEquals("The row number (100) is more than the number of rows(16) in the table.", e.getMessage());
		}
	}

	@Test
	public void throwsExceptionIfTheColumnNumberIsIllegal() throws Exception {
		try {
			table.cell(1, 100);
			fail("Was expecting an exception");
		} catch (Exception e) {
			assertEquals("The column number (100) is more than the number of column(4) in the table.", e.getMessage());
		}
	}

	@Test
	public void throwsExceptionIfMultipleIndicesOnSingleSelect() throws Exception {
		try {
			table.select(new int[] {1,2,3});
			fail("Was expecting an exception");
		} catch (Exception e) {
			assertEquals("Table does not support multi selection.", e.getMessage());
		}
	}

	@Test
	public void throwsExceptionIfTheColumnNameIsIllegal() throws Exception {
		try {
			table.cell(1, "some column");
			fail("Was expecting an exception");
		} catch (Exception e) {
			assertEquals("The column `some column' is not found.", e.getMessage());
		}
	}

	@Test
	public void getsIndexOfItem() throws Exception {
		assertEquals(7, table.indexOf("Index:7"));
		assertEquals(-1, table.indexOf("non existent item"));
	}

	@Test
	public void getsIndexOfItemInAColumnWithIndex() throws Exception {
		assertEquals(7, table.indexOf("Index:7", 0));
		assertEquals(1, table.indexOf("2556", 2));
		assertEquals(-1, table.indexOf("non existent item", 3));

	}

	@Test
	public void getsIndexOfItemInAColumnWithText() throws Exception {
		bot.radio("SWT.MULTI").click();
		table = bot.tableInGroup("Table");

		assertEquals(7, table.indexOf("Index:7", "Name"));
		assertEquals(1, table.indexOf("2556", "Size"));
		assertEquals(-1, table.indexOf("non existent item", "foo"));
	}

	@Test
	public void findsIndexOfColumn() throws Exception {
		bot.radio("SWT.MULTI").click();
		table = bot.tableInGroup("Table");

		assertEquals(0, table.indexOfColumn("Name"));
		assertEquals(2, table.indexOfColumn("Size"));
	}

	@Test
	public void selectsItemsBasedOnString() throws Exception {
		table.select("Index:9");
		TableCollection selection = table.selection();
		assertEquals("Index:9", selection.get(0, 0));
	}

	@Test
	public void findsItemsBasedOnIndex() throws Exception {
		SWTBotTableItem tableItem = table.getTableItem(3);
		assertEquals("Index:3", tableItem.getText());
	}

	@Test
	public void findsItemsBasedOnText() throws Exception {
		SWTBotTableItem tableItem = table.getTableItem("Index:3");
		assertEquals("Index:3", tableItem.getText());
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("Table").activate();
		table = bot.tableInGroup("Table");
	}
}
