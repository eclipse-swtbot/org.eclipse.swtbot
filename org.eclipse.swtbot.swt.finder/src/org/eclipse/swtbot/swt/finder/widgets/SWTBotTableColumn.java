/*******************************************************************************
 * Copyright (c) 2008, 2015 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Patrick Tasse - Support contextMenu() on table column header
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.hamcrest.SelfDescribing;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotTableColumn extends AbstractSWTBot<TableColumn> {

	private final Table	parent;

	/**
	 * Constructs a new instance of this object.
	 * 
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 * @since 2.0
	 */
	public SWTBotTableColumn(final TableColumn w) throws WidgetNotFoundException {
		this(w, UIThreadRunnable.syncExec(new WidgetResult<Table>() {
			public Table run() {
				return w.getParent();
			}
		}));
	}
	
	/**
	 * Constructs a new instance of this object.
	 * 
	 * @param w the widget.
	 * @param parent the parent table.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 * @since 2.0
	 */
	public SWTBotTableColumn(TableColumn w, Table parent) throws WidgetNotFoundException {
		this(w, parent, null);
	}

	/**
	 * Constructs a new instance of this object.
	 * 
	 * @param w the widget.
	 * @param parent the parent table.
	 * @param description the description of the widget, this will be reported by {@link #toString()}
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 * @since 2.0
	 */
	public SWTBotTableColumn(TableColumn w, Table parent, SelfDescribing description) throws WidgetNotFoundException {
		super(w, description);
		this.parent = parent;
	}

	/**
	 * Clicks the item.
	 */
	public SWTBotTableColumn click() {
		waitForEnabled();
		notify(SWT.Selection);
		notify(SWT.MouseUp, createMouseEvent(0, 0, 1, SWT.BUTTON1, 1), parent);
		return this;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public SWTBotMenu contextMenu(String text) throws WidgetNotFoundException {
		new SWTBotTable(parent).waitForEnabled();
		Rectangle bounds = getHeaderBounds();
		Event event = createEvent();
		event.widget = parent;
		event.x = bounds.x + bounds.width / 2;
		event.y = bounds.y + bounds.height / 2;
		notify(SWT.MenuDetect, event, (Widget) parent);
		return super.contextMenu(parent, text);
	}

	/**
	 * Get the bounds of this column's header in display-relative coordinates.
	 *
	 * @return the column header bounds
	 */
	private Rectangle getHeaderBounds() {
		return syncExec(new Result<Rectangle>() {
			public Rectangle run() {
				Point location = parent.getParent().toDisplay(parent.getLocation());
				Rectangle bounds = new Rectangle(location.x, location.y, widget.getWidth(), parent.getHeaderHeight());
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
		});
	}
}
