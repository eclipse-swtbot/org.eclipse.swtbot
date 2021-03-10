/*******************************************************************************
 * Copyright (c) 2008, 2021 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Ketan Patel - https://bugs.eclipse.org/bugs/show_bug.cgi?id=259720
 *     Kristine Jetzke - Bug 379185
 *     Patrick Tasse - Improve SWTBot menu API and implementation (Bug 479091)
 *     Aparna Argade(Cadence Design Systems, Inc.) - Bug 496519
 *     Laurent Redor (Obeo) - Bug 496519
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
import org.eclipse.swtbot.swt.finder.exceptions.AssertionFailedException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.eclipse.swtbot.swt.finder.utils.TableCollection;
import org.eclipse.swtbot.swt.finder.utils.TableRow;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @author Ketan Patel
 * @version $Id$
 */
public class SWTBotTreeItemTest extends AbstractControlExampleTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private SWTBotTree	tree;
	private SWTBotText	listeners;

	@Test
	public void canRightClickOnANode() throws Exception {
		SWTBotTreeItem node = tree.expandNode("Node 2").expandNode("Node 2.2").expandNode("Node 2.2.1");
		bot.button("Clear").click();
		node.contextMenu("getItem(Point) on mouse coordinates").click();
		assertEventMatches(listeners, "MenuDetect [35]: Event {type=35 Tree {} time=0 data=null x=0 y=0 width=0 height=0 detail=0}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Tree {} time=0 data=null item=TreeItem {Node 2.2.1} detail=0 x=0 y=0 width=0 height=0 stateMask=0x80000 text=null doit=true}");
		assertEventMatches(listeners, "getItem(Point(Point {");
	}

	@Test
	public void canFindANode() throws Exception {
		SWTBotTreeItem node = tree.expandNode("Node 2");
		assertText("Node 2.2", node.getNode("Node 2.2"));
		assertText("Node 2.2", node.getNode("Node 2.2", 0));
	}

	@Test
	public void canCallGetItems() throws Exception {
		SWTBotTreeItem node = tree.expandNode("Node 2");
		SWTBotTreeItem[] items = node.getItems();
		assertEquals(2, items.length);
	}

	@Test
	public void cannotFindANodeWithIncorrectNodeIndex() throws Exception {
		SWTBotTreeItem node = tree.expandNode("Node 2");
		try {
			node.getNode("Node 2.2", 1);
			fail("Was expecting an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: The index (1) was more than the number of nodes (1) in the tree.", e.getMessage());
		}
	}

	@Test
	public void checkingATreeThatDoesNotHaveCheckStyleBitsThrowsException() throws Exception {
		try {
			tree.getTreeItem("Node 2").check();
			fail("Expecting an exception");
		} catch (IllegalArgumentException e) {
			assertEquals("The tree does not have the style SWT.CHECK", e.getMessage());
		}
	}

	@Test
	public void canSelectAListOfNodes() throws Exception {
		bot.radio("SWT.MULTI").click();
		tree = bot.tree();
		SWTBotTreeItem node2 = tree.getTreeItem("Node 2").expand();
		bot.button("Clear").click();
		node2.select("Node 2.1", "Node 2.2");
		assertTrue(node2.getNode("Node 2.1").isSelected());
		assertTrue(node2.getNode("Node 2.2").isSelected());
		tree.unselect();
		node2 = tree.getTreeItem("Node 2").expand();
		bot.button("Clear").click();
		node2.select(0, 1);
		assertTrue(node2.getNode("Node 2.1").isSelected());
		assertTrue(node2.getNode("Node 2.2").isSelected());
	}

	@Test
	public void throwsExceptionIfMultipleIndicesOnSingleSelect() throws Exception {
		try {
			tree.getTreeItem("Node 2").expand().select(0, 1);
			fail("Was expecting an exception");
		} catch (Exception e) {
			assertEquals("Tree does not support multi selection.", e.getMessage());
		}

		try {
			tree.getTreeItem("Node 2").expand().select("Node 2.1", "Node 2.2");
			fail("Was expecting an exception");
		} catch (Exception e) {
			assertEquals("Tree does not support multi selection.", e.getMessage());
		}
	}

	@Test
	public void throwsExceptionIfTheRowNumberIsIllegal() throws Exception {
		try {
			tree.getTreeItem("Node 2").expand().select(100);
			fail("Was expecting an exception");
		} catch (Exception e) {
			assertEquals("The row number: 100 does not exist.", e.getMessage());
		}
	}

	@Test
	public void throwsExceptionIfNodeNameIsIllegal() throws Exception {
		try {
			tree.getTreeItem("Node 2").expand().select("NonExisting");
			fail("Was expecting an exception");
		} catch (Exception e) {
			assertEquals("Timed out waiting for tree item NonExisting", e.getMessage());
		}
	}

	@Test
	public void selectsAll() {
		bot.radio("SWT.MULTI").click();
		tree = bot.tree();
		SWTBotTreeItem node2 = tree.getTreeItem("Node 2").expand();
		node2.selectAll();
		node2.getNode("Node 2.2").expand();
		assertEquals(2, tree.selectionCount());
		TableCollection selection = tree.selection();
		assertEquals("Node 2.1", selection.get(0, 0));
		assertEquals("Node 2.2", selection.get(1, 0));
		node2.selectAll();
		assertEquals(3, tree.selectionCount());
		selection = tree.selection();
		assertEquals("Node 2.1", selection.get(0, 0));
		assertEquals("Node 2.2", selection.get(1, 0));
		assertEquals("Node 2.2.1", selection.get(2, 0));
		SWTBotTreeItem node3 = tree.getTreeItem("Node 3");
		node3.selectAll();
		assertEquals(1, tree.selectionCount());
		selection = tree.selection();
		assertEquals("Node 3", selection.get(0, 0));
		SWTBotTreeItem node4 = tree.getTreeItem("Node 4");
		node4.selectAll();
		assertEquals(1, tree.selectionCount());
		selection = tree.selection();
		assertEquals("Node 4", selection.get(0, 0));
	}

	@Test
	public void unselects() {
		bot.radio("SWT.MULTI").click();
		tree = bot.tree();
		tree.select("Node 1", "Node 2");
		assertEquals(2, tree.selectionCount());
		tree.getTreeItem("Node 1").unselect();
		assertEquals(1, tree.selectionCount());
		TableCollection selection = tree.selection();
		assertEquals("Node 2", selection.get(0, 0));
		tree.getTreeItem("Node 2").unselect();
		assertEquals(0, tree.selectionCount());
	}

	@Test
	public void canCheckANode() throws Exception {
		bot.checkBox("SWT.CHECK").select();
		tree = bot.tree();
		SWTBotTreeItem item = tree.getTreeItem("Node 2");
		item.check();
		assertTrue(tree.getTreeItem("Node 2").isChecked());
		assertTextContains("Selection [13]: SelectionEvent{Tree {} ", listeners);
		assertTextContains("data=null item=TreeItem {Node 2} detail=32", listeners);
	}

	@Test
	public void canUnCheckANode() throws Exception {
		bot.checkBox("SWT.CHECK").select();
		tree = bot.tree();
		SWTBotTreeItem item = tree.getTreeItem("Node 2");
		item.uncheck();
		assertFalse(tree.getTreeItem("Node 2").isChecked());
		assertTextContains("Selection [13]: SelectionEvent{Tree {} ", listeners);
		assertTextContains("data=null item=TreeItem {Node 2} detail=32", listeners);
	}

	@Test
	public void canToggleANode() throws Exception {
		bot.checkBox("SWT.CHECK").select();
		tree = bot.tree();
		SWTBotTreeItem item = tree.getTreeItem("Node 2");
		assertFalse(item.isChecked());
		item.toggleCheck();
		assertTrue(item.isChecked());
		item.toggleCheck();
		assertFalse(item.isChecked());
	}

	@Test
	public void getsRowCount() throws Exception {
		assertEquals(2, tree.getTreeItem("Node 2").rowCount());
		assertEquals(1, tree.getTreeItem("Node 3").rowCount());
		assertEquals(0, tree.getTreeItem("Node 4").rowCount());
		assertTrue(tree.hasItems());
	}

	@Test
	public void getsColumnTextBasedOnColumnNumbers() throws Exception {
		bot.checkBox("Multiple Columns").select();
		tree = bot.treeInGroup("Tree");
		SWTBotTreeItem item = tree.getTreeItem("Node 1");

		assertEquals("Node 1", item.cell(0));
		assertEquals("classes", item.cell(1));
		assertEquals("today", item.cell(3));
		assertEquals("0", item.cell(2));
	}

	@Test
	public void getsColumnTextBasedOnRowColumnNumbers() throws Exception {
		bot.checkBox("Multiple Columns").select();
		tree = bot.treeInGroup("Tree");
		SWTBotTreeItem item = tree.getTreeItem("Node 2");

		assertEquals("2556", item.cell(0, 2));
		assertEquals("Node 2.1", item.cell(0, 0));
		assertEquals("tomorrow", item.cell(1, 3));
	}

	@Test
	public void getsRow() throws Exception {
		bot.checkBox("Multiple Columns").select();
		tree = bot.treeInGroup("Tree");
		SWTBotTreeItem item = tree.getTreeItem("Node 3");
		TableRow row = item.row();
		assertEquals(4, row.columnCount());
		assertEquals("Node 3", row.get(0));
		assertEquals("images", row.get(1));
		assertEquals("91571", row.get(2));
		assertEquals("yesterday", row.get(3));
	}

	@Test
	public void getNodeBasedOnIndex() throws Exception {
		bot.checkBox("Multiple Columns").select();
		tree = bot.treeInGroup("Tree");
		SWTBotTreeItem item = tree.getTreeItem("Node 2");
		SWTBotTreeItem testNode = item.getNode(0);
		assertEquals("Node 2.1", testNode.getText());

		testNode = item.getNode(1);
		assertEquals("Node 2.2", testNode.getText());

		testNode = testNode.getNode(0);
		assertEquals("Node 2.2.1", testNode.getText());

		try {
			assertEquals(0, testNode.rowCount());
			testNode.getNode(1);
			fail("Expected IllegalArgumentException since 'Node 2.2.1' does not have row at index (1)");
		} catch (IllegalArgumentException e) {
			String expected = "The row number (1) is more than the number of rows(0) in the tree.";
			assertEquals(expected, e.getMessage());
		}
	}

	@Test
	public void cellOutOfRangeWithMultipleColumnsTree() throws Exception {
		bot.checkBox("Multiple Columns").select();
		tree = bot.treeInGroup("Tree");

		assertEquals(4, tree.columnCount());
		runCellOutOfRangeTest(tree.getTreeItem("Node 1"), 4);
	}

	private void runCellOutOfRangeTest(SWTBotTreeItem testNode, int columnNumber) throws Exception {
		try {
			testNode.cell(columnNumber);
			fail("Expected IllegalArgumentException since '" + testNode.getText() + "' does not have column at index (" + columnNumber
					+ ")");
		} catch (IllegalArgumentException e) {
			String expected = "The column index (" + columnNumber + ") is more than the number of column(" + tree.columnCount()
					+ ") in the tree.";
			assertEquals(expected, e.getMessage());
		}
	}

	@Test
	public void cellOutOfRangeWithSingleColumnsTree() throws Exception {
		bot.checkBox("Multiple Columns").deselect();
		tree = bot.treeInGroup("Tree");

		assertEquals(0, tree.columnCount());
		runCellOutOfRangeTest(tree.getTreeItem("Node 1"), 1);
	}

	@Test
	public void canClickOnANode() throws Exception {
		final String ITEM_TEXT = "Node 2";
		final SWTBotTreeItem treeItem = tree.getTreeItem(ITEM_TEXT);
		treeItem.click();

		assertEventMatches(listeners, "MouseEnter [6]: MouseEvent{Tree {} time=0 data=null button=0 stateMask=0x0 x=0 y=0 count=0}");
		assertEventMatches(listeners, "Activate [26]: ShellEvent{Tree {} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusIn [15]: FocusEvent{Tree {} time=0 data=null}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=1}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Tree {} time=0 data=null item=TreeItem {Node 2} detail=0 x=0 y=0 width=0 height=0 stateMask=0x80000 text=null doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x80000 x=0 y=0 count=1}");
		assertEventMatches(listeners, "MouseExit [7]: MouseEvent{Tree {} time=0 data=null button=0 stateMask=0x0 x=0 y=0 count=0}");
		assertEventMatches(listeners, "Deactivate [27]: ShellEvent{Tree {} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusOut [16]: FocusEvent{Tree {} time=0 data=null}");
		assertEquals(1, tree.selectionCount());
		assertEquals(ITEM_TEXT, tree.selection().get(0).get(0));
	}

	@Test
	public void canClickOnANodeInAColumn() throws Exception {
		bot.checkBox("Multiple Columns").select();
		tree = bot.treeInGroup("Tree");
		final String ITEM_TEXT = "Node 2";
		final SWTBotTreeItem treeItem = tree.getTreeItem(ITEM_TEXT);
		treeItem.click(2);

		assertEventMatches(listeners, "MouseEnter [6]: MouseEvent{Tree {} time=0 data=null button=0 stateMask=0x0 x=0 y=0 count=0}");
		assertEventMatches(listeners, "Activate [26]: ShellEvent{Tree {} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusIn [15]: FocusEvent{Tree {} time=0 data=null}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=1}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Tree {} time=0 data=null item=TreeItem {Node 2} detail=0 x=0 y=0 width=0 height=0 stateMask=0x80000 text=null doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x80000 x=0 y=0 count=1}");
		assertEventMatches(listeners, "MouseExit [7]: MouseEvent{Tree {} time=0 data=null button=0 stateMask=0x0 x=0 y=0 count=0}");
		assertEventMatches(listeners, "Deactivate [27]: ShellEvent{Tree {} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusOut [16]: FocusEvent{Tree {} time=0 data=null}");
		Point p = getCellCenter(treeItem, 2);
		assertTextContains(" x=" + p.x + " y=" + p.y + " ", listeners);
		assertEquals(1, tree.selectionCount());
		assertEquals(ITEM_TEXT, tree.selection().get(0).get(0));
	}

	@Test
	public void canDoubleClickOnANode() throws Exception {
		final String ITEM_TEXT = "Node 2";
		final SWTBotTreeItem treeItem = tree.getTreeItem(ITEM_TEXT);

		treeItem.doubleClick();

		assertEventMatches(listeners, "MouseEnter [6]: MouseEvent{Tree {} time=0 data=null button=0 stateMask=0x0 x=0 y=0 count=0}");
		assertEventMatches(listeners, "Activate [26]: ShellEvent{Tree {} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusIn [15]: FocusEvent{Tree {} time=0 data=null}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=1}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Tree {} time=0 data=null item=TreeItem {Node 2} detail=0 x=0 y=0 width=0 height=0 stateMask=0x80000 text=null doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x80000 x=0 y=0 count=1}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=2}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Tree {} time=0 data=null item=TreeItem {Node 2} detail=0 x=0 y=0 width=0 height=0 stateMask=0x80000 text=null doit=true}");
		assertEventMatches(listeners, "MouseDoubleClick [8]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=2}");
		assertEventMatches(listeners, "DefaultSelection [14]: SelectionEvent{Tree {} time=0 data=null item=TreeItem {Node 2} detail=0 x=0 y=0 width=0 height=0 stateMask=0x80000 text=null doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x80000 x=0 y=0 count=2}");
		assertEventMatches(listeners, "MouseExit [7]: MouseEvent{Tree {} time=0 data=null button=0 stateMask=0x0 x=0 y=0 count=0}");
		assertEventMatches(listeners, "Deactivate [27]: ShellEvent{Tree {} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusOut [16]: FocusEvent{Tree {} time=0 data=null}");
		assertEquals(1, tree.selectionCount());
		assertEquals(ITEM_TEXT, tree.selection().get(0).get(0));
	}

	@Test
	public void canDoubleClickOnANodeInAColumn() throws Exception {
		bot.checkBox("Multiple Columns").select();
		tree = bot.treeInGroup("Tree");
		final String ITEM_TEXT = "Node 2";
		final SWTBotTreeItem treeItem = tree.getTreeItem(ITEM_TEXT);

		treeItem.doubleClick(2);

		assertEventMatches(listeners, "MouseEnter [6]: MouseEvent{Tree {} time=0 data=null button=0 stateMask=0x0 x=0 y=0 count=0}");
		assertEventMatches(listeners, "Activate [26]: ShellEvent{Tree {} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusIn [15]: FocusEvent{Tree {} time=0 data=null}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=1}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Tree {} time=0 data=null item=TreeItem {Node 2} detail=0 x=0 y=0 width=0 height=0 stateMask=0x80000 text=null doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x80000 x=0 y=0 count=1}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=2}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Tree {} time=0 data=null item=TreeItem {Node 2} detail=0 x=0 y=0 width=0 height=0 stateMask=0x80000 text=null doit=true}");
		assertEventMatches(listeners, "MouseDoubleClick [8]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=2}");
		assertEventMatches(listeners, "DefaultSelection [14]: SelectionEvent{Tree {} time=0 data=null item=TreeItem {Node 2} detail=0 x=0 y=0 width=0 height=0 stateMask=0x80000 text=null doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x80000 x=0 y=0 count=2}");
		assertEventMatches(listeners, "MouseExit [7]: MouseEvent{Tree {} time=0 data=null button=0 stateMask=0x0 x=0 y=0 count=0}");
		assertEventMatches(listeners, "Deactivate [27]: ShellEvent{Tree {} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusOut [16]: FocusEvent{Tree {} time=0 data=null}");
		Point p = getCellCenter(treeItem, 2);
		assertTextContains(" x=" + p.x + " y=" + p.y + " ", listeners);
		assertEquals(1, tree.selectionCount());
		assertEquals(ITEM_TEXT, tree.selection().get(0).get(0));
	}

	@Test
	public void canExpandANodeUsingVarArgs() throws Exception {
		SWTBotTreeItem node = tree.getTreeItem("Node 2").expand();
		node = node.expandNode("Node 2.2", "Node 2.2.1");

		assertEquals(7, tree.visibleRowCount());
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("Tree").activate();
		bot.checkBox("Horizontal Fill").select();
		bot.checkBox("Vertical Fill").select();
		bot.checkBox("Popup Menu").select();
		bot.checkBox("Listen").deselect();
		bot.checkBox("SWT.CHECK").deselect();
		bot.radio("SWT.SINGLE").click();
		bot.checkBox("Listen").select();
		tree = bot.tree();
		listeners = bot.textInGroup("Listeners");
		bot.button("Clear").click();
	}

	@Test
	public void isGrayed() throws Exception {
		SWTBotTreeItem itemGrayed = bot.treeInGroup("Tree_Checked").getTreeItem("grayed? true");
		assertTrue(itemGrayed.isGrayed());
		SWTBotTreeItem itemNotGrayed = bot.treeInGroup("Tree_Checked").getTreeItem("grayed? false");
		assertFalse(itemNotGrayed.isGrayed());
	}

	@Test
	public void isGrayedTreeNotChecked() throws Exception {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("does not have the style SWT.CHECK");

		tree.getAllItems()[0].isGrayed();
	}

	private Point getCellCenter(final SWTBotTreeItem node, final int columnIndex) {
		 return UIThreadRunnable.syncExec(new Result<Point>() {
				@Override
				public Point run() {
					Rectangle bounds = node.widget.getBounds(columnIndex);
					return new Point(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
				}
			});
	}
}
