/*******************************************************************************
 * Copyright (c) 2013, 2017 Robin Stocker and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Robin Stocker - initial API and implementation
 *     Patrick Tasse - Support contextMenu() on tree column header
 *                   - Improve SWTBot menu API and implementation (Bug 479091) 
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.hamcrest.SelfDescribing;

/**
 * Bot for a {@link TreeColumn} widget.
 * <p>
 * See {@link SWTBotTree#header(String)} for getting an instance from a tree
 * bot.
 * 
 * @since 2.1.2
 */
public class SWTBotTreeColumn extends AbstractSWTBot<TreeColumn> {

	private final Tree	parent;

	/**
	 * Constructs a new instance of this object.
	 * 
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotTreeColumn(final TreeColumn w) throws WidgetNotFoundException {
		this(w, UIThreadRunnable.syncExec(new WidgetResult<Tree>() {
			@Override
			public Tree run() {
				return w.getParent();
			}
		}));
	}

	@Override
	protected Rectangle getBounds() {
		return syncExec(new Result<Rectangle>() {
			@Override
			public Rectangle run() {
				if (widget.isDisposed()) {
					return new Rectangle(0, 0, 0, 0);
				}
				Tree tree = widget.getParent();
				Point location = widget.getDisplay().map(tree.getParent(), tree, tree.getLocation());
				Rectangle bounds = new Rectangle(location.x, location.y, widget.getWidth(), tree.getHeaderHeight());
				for (int i : tree.getColumnOrder()) {
					TreeColumn column = tree.getColumn(i);
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

	/**
	 * Constructs a new instance of this object.
	 * 
	 * @param w the widget.
	 * @param parent the parent tree.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotTreeColumn(TreeColumn w, Tree parent) throws WidgetNotFoundException {
		this(w, parent, null);
	}

	/**
	 * Constructs a new instance of this object.
	 * 
	 * @param w the widget.
	 * @param parent the parent tree.
	 * @param description the description of the widget, this will be reported by {@link #toString()}
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotTreeColumn(TreeColumn w, Tree parent, SelfDescribing description) throws WidgetNotFoundException {
		super(w, description);
		this.parent = parent;
	}

	/**
	 * Clicks the item.
	 */
	@Override
	public SWTBotTreeColumn click() {
		waitForEnabled();
		notify(SWT.Selection);
		// Mouse event doesn't seem to be sent by the real SWT?
		notify(SWT.MouseUp, createMouseEvent(1, SWT.BUTTON1, 1), parent);
		return this;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public SWTBotRootMenu contextMenu() throws WidgetNotFoundException {
		new SWTBotTree(parent).waitForEnabled();
		return contextMenu(parent);
	}
}
