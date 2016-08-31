/*******************************************************************************
 * Copyright (c) 2008, 2016 http://www.inria.fr/ and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     http://www.inria.fr/ - initial API and implementation
 *     Cédric Chabanois - bug 269164
 *     Patrick Tasse - Improve SWTBot menu API and implementation (Bug 479091)
 *                   - Add support for click(int) and doubleClick()
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.StringResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.utils.MessageFormat;
import org.eclipse.swtbot.swt.finder.utils.internal.Assert;
import org.hamcrest.SelfDescribing;

/**
 * @author Vincent MAHE &lt;vmahe [at] free [dot]fr&gt;
 * @author Cédric Chabanois
 * @version $Id$
 * @since 1.3
 */
public class SWTBotTableItem extends AbstractSWTBot<TableItem> {

	private Table	table;

	/**
	 * @param tableItem the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotTableItem(final TableItem tableItem) throws WidgetNotFoundException {
		this(tableItem, null);
	}

	/**
	 * @param tableItem the widget.
	 * @param description the description of the widget, this will be reported by {@link #toString()}
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotTableItem(final TableItem tableItem, SelfDescribing description) throws WidgetNotFoundException {
		super(tableItem, description);
		this.table = syncExec(new WidgetResult<Table>() {
			public Table run() {
				return tableItem.getParent();
			}
		});
	}

	/**
	 * Selects the current table item.
	 *
	 * @return the current node.
	 */
	public SWTBotTableItem select() {
		assertEnabled();
		syncExec(new VoidResult() {
			public void run() {
				table.setFocus();
				table.setSelection(widget);
			}
		});
		notifySelect();
		return this;
	}

	/**
	 * Click on the table at given coordinates
	 *
	 * @param x the x co-ordinate of the click
	 * @param y the y co-ordinate of the click
	 */
	protected void clickXY(int x, int y) {
		log.debug(MessageFormat.format("Clicking on {0}", this)); //$NON-NLS-1$
		notifyTable(SWT.MouseEnter, createMouseEvent(x, y, 0, SWT.NONE, 0));
		notifyTable(SWT.Activate, super.createEvent());
		syncExec(new VoidResult() {
			public void run() {
				if (table.getSelectionCount() != 1 || !table.getSelection()[0].equals(widget)) {
					table.setSelection(widget);
				}
				if (!table.isFocusControl()) {
					table.setFocus();
				}
			}
		});
		notifyTable(SWT.FocusIn, super.createEvent());
		notifyTable(SWT.MouseDown, createMouseEvent(x, y, 1, SWT.NONE, 1));
		notifyTable(SWT.Selection);
		notifyTable(SWT.MouseUp, createMouseEvent(x, y, 1, SWT.BUTTON1, 1));
		notifyTable(SWT.MouseExit, createMouseEvent(x, y, 0, SWT.NONE, 0));
		notifyTable(SWT.Deactivate, super.createEvent());
		notifyTable(SWT.FocusOut, super.createEvent());
		log.debug(MessageFormat.format("Clicked on {0}", this)); //$NON-NLS-1$
	}

	private void notifyTable(int eventType, Event event) {
		notify(eventType, event, table);
	}

	private void notifyTable(int event) {
		notifyTable(event, createEvent());
	}

	/**
	 * Clicks on this node.
	 *
	 * @return the current node.
	 */
	public SWTBotTableItem click() {
		assertEnabled();
		Point center = getCenter(getCellBounds());
		clickXY(center.x, center.y);
		return this;
	}

	/**
	 * Clicks on this node at the given column index.
	 *
	 * @return the current node.
	 * @since 2.5
	 */
	public SWTBotTableItem click(final int column) {
		assertEnabled();
		Point center = getCenter(getCellBounds(column));
		clickXY(center.x, center.y);
		return this;
	}

	/**
	 * Double clicks on this node.
	 *
	 * @return the current node.
	 * @since 2.5
	 */
	public SWTBotTableItem doubleClick() {
		assertEnabled();

		final Point center = getCenter(getCellBounds());

		log.debug(MessageFormat.format("Double-clicking on {0}", this)); //$NON-NLS-1$
		notifyTable(SWT.MouseEnter, createMouseEvent(center.x, center.y, 0, SWT.NONE, 0));
		notifyTable(SWT.Activate, super.createEvent());
		syncExec(new VoidResult() {
			public void run() {
				if (table.getSelectionCount() != 1 || !table.getSelection()[0].equals(widget)) {
					table.setSelection(widget);
				}
				if (!table.isFocusControl()) {
					table.setFocus();
				}
			}
		});
		notifyTable(SWT.FocusIn, super.createEvent());
		notifyTable(SWT.MouseDown, createMouseEvent(center.x, center.y, 1, SWT.NONE, 1));
		notifyTable(SWT.Selection);
		notifyTable(SWT.MouseUp, createMouseEvent(center.x, center.y, 1, SWT.BUTTON1, 1));
		notifyTable(SWT.MouseDown, createMouseEvent(center.x, center.y, 1, SWT.NONE, 2));
		notifyTable(SWT.Selection);
		notifyTable(SWT.MouseDoubleClick, createMouseEvent(center.x, center.y, 1, SWT.NONE, 2));
		notifyTable(SWT.DefaultSelection);
		notifyTable(SWT.MouseUp, createMouseEvent(center.x, center.y, 1, SWT.BUTTON1, 2));
		notifyTable(SWT.MouseExit, createMouseEvent(center.x, center.y, 0, SWT.NONE, 0));
		notifyTable(SWT.Deactivate, super.createEvent());
		notifyTable(SWT.FocusOut, super.createEvent());
		log.debug(MessageFormat.format("Double-clicked on {0}", this)); //$NON-NLS-1$
		return this;
	}

	/**
	 * Get the cell bounds. widget should be enabled before calling this method.
	 *
	 * @param column the table column index
	 * @return the cell bounds
	 */
	private Rectangle getCellBounds(final int column) {
		return syncExec(new Result<Rectangle>() {
			public Rectangle run() {
				return widget.getBounds(column);
			}
		});
	}

	/**
	 * Get the cell bounds. widget should be enabled before calling this method.
	 *
	 * @return the cell bounds
	 */
	private Rectangle getCellBounds() {
		return syncExec(new Result<Rectangle>() {
			public Rectangle run() {
				return widget.getBounds();
			}
		});
	}

	/**
	 * Get the center of the given rectangle.
	 *
	 * @param bounds the rectangle
	 * @return the center.
	 */
	private Point getCenter(Rectangle bounds) {
		return new Point(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
	}

	public String getText() {
		return syncExec(new StringResult() {
			public String run() {
				return widget.getText();
			}
		});
	}

	public String getText(final int index) {
		return syncExec(new StringResult() {
			public String run() {
				return widget.getText(index);
			}
		});
	}

	@Override
	public SWTBotRootMenu contextMenu() throws WidgetNotFoundException {
		new SWTBotTable(table).waitForEnabled();
		select();
		return contextMenu(table);
	}

	/**
	 * Toggle the table item.
	 */
	public void toggleCheck() {
		setChecked(!isChecked());
	}

	/**
	 * Check the table item.
	 */
	public void check() {
		setChecked(true);
	}

	/**
	 * Uncheck the table item.
	 */
	public void uncheck() {
		setChecked(false);
	}

	/**
	 * Gets if the checkbox button is checked.
	 *
	 * @return <code>true</code> if the checkbox is checked. Otherwise <code>false</code>.
	 */
	public boolean isChecked() {
		assertIsCheck();
		return syncExec(new BoolResult() {
			public Boolean run() {
				return widget.getChecked();
			}
		});
	}

	/**
	 * Gets if the checkbox button is grayed.
	 *
	 * @return <code>true</code> if the checkbox is grayed, <code>false</code> otherwise.
	 */
	public boolean isGrayed() {
		assertIsCheck();
		return syncExec(new BoolResult() {
			public Boolean run() {
				return widget.getGrayed();
			}
		});
	}

	/**
	 * Creates an event for CheckboxTableItem case.
	 *
	 * @return an event that encapsulates {@link #widget} and {@link #display}.
	 */
	private Event createCheckEvent() {
		Event event = createEvent();
		event.detail = SWT.CHECK;
		return event;
	}

	protected Event createEvent() {
		Event event = super.createEvent();
		event.widget = table;
		event.item = widget;
		return event;
	}

	private void setChecked(final boolean checked) {
		assertEnabled();
		assertIsCheck();
		syncExec(new VoidResult() {
			public void run() {
				TableItem item = widget;
				log.debug(MessageFormat.format("Setting state to {0} on: {1}", (checked ? "checked" : "unchecked"), item.getText())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				item.setChecked(checked);
			}
		});
		notifyCheck();
	}

	private void assertIsCheck() {
		Assert.isLegal(hasStyle(table, SWT.CHECK), "The table does not have the style SWT.CHECK"); //$NON-NLS-1$
	}

	/**
	 * notify listeners about checkbox state change.
	 *
	 * @since 1.3
	 */
	private void notifyCheck() {
		syncExec(new VoidResult() {
			public void run() {
				table.notifyListeners(SWT.Selection, createCheckEvent());
			}
		});
	}

	private void notifySelect() {
		syncExec(new VoidResult() {
			public void run() {
				table.notifyListeners(SWT.Selection, createSelectionEvent());
			}
		});
	}

	protected void assertEnabled() {
		new SWTBotTable(table).assertEnabled();
	}

	private Event createSelectionEvent() {
		Event event = createEvent();
		event.item = widget;
		event.widget = table;
		return event;
	}
	
	public boolean isEnabled() {
		return syncExec(new BoolResult() {
			public Boolean run() {
				return table.isEnabled();
			}
		});
	}

}
