/*******************************************************************************
 * Copyright (c) 2012, 2015 Lorenzo Bettini and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stefan Seelmann (initial)
 *     Stefan Schaefer (extension)
 *     Lorenzo Bettini (extracted method to get the MenuItem)
 *     Patrick Tasse - Improve SWTBot menu API and implementation (Bug 479091) 
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.finders;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;

/**
 * This helper is a workaround for a bug in SWTBot, where the bot can't find a
 * dynamically created context menu, see also
 * http://www.eclipse.org/forums/index.php/t/11863/
 * 
 * @author Stefan Seelmann (initial)
 * @author Stefan Schaefer (extension)
 * @author Lorenzo Bettini (extracted method to get the MenuItem)
 */
public class ContextMenuHelper {

	/**
	 * Gets the menu item widget matching the given text path in the given
	 * control's pop up menu. It will attempt to recursively find the menu items
	 * in sequence in the matching sub-menus that are found.
	 * <p>
	 * This is equivalent to bot.contextMenu().menu(texts).widget.
	 *
	 * @param bot
	 *            the control to get the context menu from.
	 * @param texts
	 *            the texts on the menu items that are to be found.
	 * @return the menu item widget that has the given text.
	 */
	public static MenuItem contextMenu(final AbstractSWTBot<? extends Control> bot, final String... texts) {
		return bot.contextMenu().menu(texts).widget;
	}

	/**
	 * Gets the menu item widget matching the given text path in the given
	 * control's pop up menu. It will attempt to recursively find the menu items
	 * in sequence in the matching sub-menus that are found.
	 * <p>
	 * This is equivalent to bot.contextMenu().menu(texts).widget.
	 *
	 * @param bot
	 *            the control to get the context menu from.
	 * @param widget
	 *            ignored.
	 * @param texts
	 *            the texts on the menu items that are to be found.
	 * @since 2.4
	 * @return the menu item widget that has the given text.
	 */
	public static MenuItem contextMenu(final AbstractSWTBot<? extends Control> bot, final Widget widget,
			final String... texts) {
		return bot.contextMenu().menu(texts).widget;
	}

	/**
	 * Notify the control of SWT.MenuDetect when a context menu occurs on a
	 * widget. The event coordinates are set to the center of the widget.
	 *
	 * @param control
	 *            the control that should be notified
	 * @param widget
	 *            the widget on which the context menu was triggered on
	 * @since 2.4
	 */
	public static boolean notifyMenuDetect(final Control control, final Widget widget) {
		Rectangle bounds = getBounds(widget);
		if (bounds == null) {
			return false;
		}

		final Event event = new Event();
		event.time = (int) System.currentTimeMillis();
		event.display = control.getDisplay();
		event.widget = control;
		event.x = bounds.x + bounds.width / 2;
		event.y = bounds.y + bounds.height / 2;
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				control.notifyListeners(SWT.MenuDetect, event);
			}
		});
		return event.doit;
	}

	/**
	 * Get the bounds of the widget in display-relative coordinates.
	 *
	 * @return the widget bounds or null
	 */
	private static Rectangle getBounds(final Widget widget) {

		return UIThreadRunnable.syncExec(widget.getDisplay(), new Result<Rectangle>() {
			public Rectangle run() {
				Control parent;
				Rectangle widgetBounds;
				// Control, TableItem, TreeItem, etc don't have a common interface for this
				if (widget instanceof Control) {
					parent = ((Control) widget).getParent();
					widgetBounds = ((Control) widget).getBounds();
				} else if (widget instanceof TableItem) {
					TableItem tableItem = (TableItem) widget;
					parent = tableItem.getParent();
					widgetBounds = getTableItemBounds(tableItem);
				} else if (widget instanceof TreeItem) {
					TreeItem treeItem = (TreeItem) widget;
					parent = treeItem.getParent();
					widgetBounds = getTreeItemBounds(treeItem);
				} else if (widget instanceof TableColumn) {
					TableColumn tableColumn = (TableColumn) widget;
					parent = tableColumn.getParent();
					widgetBounds = getTableColumnBounds(tableColumn);
					// We use the Table's parent coordinate system as it is a
					// sure way of getting the very top left corner of the Table
					// widget. The (0, 0) location in Table coordinates is not
					// consistent across windowing system; sometimes it includes
					// the header, sometimes not.
					Rectangle grandParentWidgetBounds = toGrandParentBounds(parent, widgetBounds);
					return toDisplayBounds(parent.getParent(), grandParentWidgetBounds);
				} else if (widget instanceof TreeColumn) {
					TreeColumn treeColumn = (TreeColumn) widget;
					parent = treeColumn.getParent();
					widgetBounds = getTreeColumnBounds(treeColumn);
					Rectangle grandParentWidgetBounds = toGrandParentBounds(parent, widgetBounds);
					return toDisplayBounds(parent.getParent(), grandParentWidgetBounds);
				} else {
					return null;
				}

				return toDisplayBounds(parent, widgetBounds);
			}

			/**
			 * Convert the bounds (parent-relative) to the grand-parent-relative
			 * bounds by using the parent location.
			 */
			private Rectangle toGrandParentBounds(Control parent, Rectangle bounds) {
				Point parentLocation = parent.getLocation();
				return new Rectangle(parentLocation.x + bounds.x, parentLocation.y + bounds.y, bounds.width, bounds.height);
			}

			/**
			 * Convert the bounds (parent-relative) to display-relative bounds.
			 */
			private Rectangle toDisplayBounds(Control parent, Rectangle bounds) {
				Point location = new Point(bounds.x, bounds.y);
				if (parent != null) {
					location = parent.toDisplay(location);
				}

				return new Rectangle(location.x, location.y, bounds.width, bounds.height);
			}

			/**
			 * For both table and tree items, we consider the bounds to be from
			 * the start of the table to the end of the last column. On some
			 * platforms, there can be empty space between the last column and
			 * the end of the table.
			 */
			private Rectangle getTableItemBounds(TableItem tableItem) {
				Table table = tableItem.getParent();
				int[] columnOrder = table.getColumnOrder();
				Rectangle itemBounds;
				if (columnOrder.length > 0) {
					// Use the bounds of the last column to know where the item really ends
					int lastColumnIndex = columnOrder[columnOrder.length - 1];
					itemBounds = tableItem.getBounds(lastColumnIndex);
					itemBounds.width = itemBounds.x + itemBounds.width;
				} else {
					Rectangle tableBounds = table.getBounds();
					itemBounds = tableItem.getBounds();
					itemBounds.width = tableBounds.width;
				}
				itemBounds.x = 0;
				return itemBounds;
			}

			/**
			 * See {@link #getTableItemBounds}
			 */
			private Rectangle getTreeItemBounds(TreeItem treeItem) {
				Tree tree = treeItem.getParent();
				int[] columnOrder = tree.getColumnOrder();
				Rectangle itemBounds;
				if (columnOrder.length > 0) {
					// Use the bounds of the last column to know where the item really ends
					int lastColumnIndex = columnOrder[columnOrder.length - 1];
					itemBounds = treeItem.getBounds(lastColumnIndex);
					itemBounds.width = itemBounds.x + itemBounds.width;
				} else {
					Rectangle treeBounds = tree.getBounds();
					itemBounds = treeItem.getBounds();
					itemBounds.width = treeBounds.width;
				}
				itemBounds.x = 0;
				return itemBounds;
			}

			private Rectangle getTableColumnBounds(TableColumn tablecolumn) {
				Table parent = tablecolumn.getParent();
				Rectangle bounds = new Rectangle(0, 0, tablecolumn.getWidth(), parent.getHeaderHeight());
				for (int i : parent.getColumnOrder()) {
					TableColumn column = parent.getColumn(i);
					if (column.equals(widget)) {
						break;
					} else {
						bounds.x += column.getWidth();
					}
				}
				return bounds;
			}

			private Rectangle getTreeColumnBounds(TreeColumn treecolumn) {
				Tree parent = treecolumn.getParent();
				Rectangle bounds = new Rectangle(0, 0, treecolumn.getWidth(), parent.getHeaderHeight());
				for (int i : parent.getColumnOrder()) {
					TreeColumn column = parent.getColumn(i);
					if (column.equals(widget)) {
						break;
					} else {
						bounds.x += column.getWidth();
					}
				}
				return bounds;
			}
		});
	}
}
