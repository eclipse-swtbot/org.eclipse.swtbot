/*******************************************************************************
 * Copyright (c) 2015 Ericsson and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.swtbot.swt.finder.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swtbot.swt.finder.test.AbstractSWTShellTest;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.Test;

public class MenuDetectTest extends AbstractSWTShellTest {

	private static final String MENU_ITEM_TEXT = "item";
	private static final String EXPECTED_TEXT = "menu detected";

	@Override
	protected void createUI(Composite parent) {
		final Label label = new Label(shell, SWT.NONE);
		label.setText("Label");

		Menu menu = new Menu(label);
		MenuItem menuItem = new MenuItem(menu, 0);
		menuItem.setText(MENU_ITEM_TEXT);
		label.setMenu(menu);
		label.addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				checkInsideBounds(label.getParent().toControl(new Point(event.x, event.y)), label.getBounds());
				label.setText(EXPECTED_TEXT);
			}
		});

		createTestTable(false);
		createTestTable(true);

		createTestTree(false);
		createTestTree(true);
	}

	private void createTestTree(boolean useColumns) {
		final Tree tree = new Tree(shell, SWT.NONE);
		if (useColumns) {
			TreeColumn column = new TreeColumn(tree, SWT.NONE);
			column.setText("Column");
			column.setWidth(100);
			column = new TreeColumn(tree, SWT.NONE);
			column.setText("Column 2");
			column.setWidth(100);
			tree.setHeaderVisible(true);
			tree.setColumnOrder(new int[] { 1, 0 });
		}
		final TreeItem treeItem = new TreeItem(tree, SWT.NONE);
		treeItem.setText("Test tree item");
		Menu menu = new Menu(tree);
		MenuItem menuItem = new MenuItem(menu, 0);
		menuItem.setText(MENU_ITEM_TEXT);
		tree.setMenu(menu);
		tree.addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Point point = new Point(event.x, event.y);
				checkInsideBounds(tree.getParent().toControl(point), tree.getBounds());
				treeItem.setText(EXPECTED_TEXT);
			}
		});
	}

	private void createTestTable(boolean useColumns) {
		final Table table = new Table(shell, SWT.NONE);
		if (useColumns) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText("Column");
			column.setWidth(100);
			column = new TableColumn(table, SWT.NONE);
			column.setText("Column 2");
			column.setWidth(100);
			table.setHeaderVisible(true);
			table.setColumnOrder(new int[] { 1, 0 });
		}
		final TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText("Test table item");
		Menu menu = new Menu(table);
		MenuItem menuItem = new MenuItem(menu, 0);
		menuItem.setText(MENU_ITEM_TEXT);
		table.setMenu(menu);
		table.addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Point point = new Point(event.x, event.y);
				checkInsideBounds(table.getParent().toControl(point), table.getBounds());
				tableItem.setText(EXPECTED_TEXT);
			}
		});
	}

	/**
	 * Make sure that widgets get the SWT.MenuDetect event and that the event
	 * has reasonable X and Y values.
	 */
	@Test
	public void testMenuDetect() {
		SWTBotLabel label = bot.label();
		label.contextMenu(MENU_ITEM_TEXT);
		assertEquals(EXPECTED_TEXT, label.getText());

		SWTBotTableItem tableItem = bot.table(0).getTableItem(0);
		tableItem.contextMenu(MENU_ITEM_TEXT);
		assertEquals(EXPECTED_TEXT, tableItem.getText());

		tableItem = bot.table(1).getTableItem(0);
		tableItem.contextMenu(MENU_ITEM_TEXT);
		assertEquals(EXPECTED_TEXT, tableItem.getText());

		SWTBotTreeItem treeItem = bot.tree(0).getTreeItem("Test tree item");
		treeItem.contextMenu(MENU_ITEM_TEXT);
		assertEquals(EXPECTED_TEXT, treeItem.getText());

		treeItem = bot.tree(1).getTreeItem("Test tree item");
		treeItem.contextMenu(MENU_ITEM_TEXT);
		assertEquals(EXPECTED_TEXT, treeItem.getText());
	}

	private static void checkInsideBounds(Point position, Rectangle bounds) {
		assertTrue("SWT.MenuDetect not inside bounds. Position: " + position + " bounds: " + bounds, bounds.contains(position));
	}
}
