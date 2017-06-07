/*******************************************************************************
 * Copyright (c) 2016 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Patrick Tasse - Initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swtbot.swt.finder.ReferenceBy;
import org.eclipse.swtbot.swt.finder.SWTBotWidget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.utils.MessageFormat;
import org.hamcrest.SelfDescribing;

/**
 * SWTBot class representing a canvas.
 *
 * @author Patrick Tasse
 * @version $Id$
 * @since 2.4
 */
@SWTBotWidget(clasz = Canvas.class, preferredName = "canvas", referenceBy = { ReferenceBy.TOOLTIP })
public class SWTBotCanvas extends AbstractSWTBotControl<Canvas> {

	/**
	 * @param w the widget.
	 * @param description the description of the widget, this will be reported by {@link #toString()}
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotCanvas(Canvas w, SelfDescribing description) throws WidgetNotFoundException {
		super(w, description);
	}

	/**
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotCanvas(Canvas w) throws WidgetNotFoundException {
		this(w, null);
	}

	@Override
	public AbstractSWTBot<Canvas> click() {
		Point size = syncExec(new Result<Point>() {
			@Override
			public Point run() {
				return widget.getSize();
			}
		});
		click(size.x / 2, size.y / 2);
		return this;
	}

	/**
	 * Click on the widget at the given position in the widget's coordinates
	 *
	 * @param x the x-coordinate of the click
	 * @param y the y-coordinate of the click
	 */
	public void click(int x, int y) {
		log.debug(MessageFormat.format("Clicking on {0}", this)); //$NON-NLS-1$
		notify(SWT.MouseEnter);
		notify(SWT.MouseMove);
		notify(SWT.Activate);
		notify(SWT.FocusIn);
		notify(SWT.MouseDown, createMouseEvent(x, y, 1, SWT.NONE, 1));
		notify(SWT.MouseUp, createMouseEvent(x, y, 1, SWT.BUTTON1, 1));
		log.debug(MessageFormat.format("Clicked on {0}", this)); //$NON-NLS-1$
	}

	/**
	 * Double-clicks on this widget.
	 *
	 * @since 2.6
	 */
	public void doubleClick() {
		Point size = syncExec(new Result<Point>() {
			@Override
			public Point run() {
				return widget.getSize();
			}
		});
		doubleClick(size.x / 2, size.y / 2);
	}

	/**
	 * Double-Click on the widget at the given position in the widget's coordinates
	 *
	 * @param x the x-coordinate of the double-click
	 * @param y the y-coordinate of the double-click
	 *
	 * @since 2.6
	 */
	public void doubleClick(int x, int y) {
		log.debug(MessageFormat.format("Double-clicking on {0}", this)); //$NON-NLS-1$
		notify(SWT.MouseEnter, createMouseEvent(x, y, 0, SWT.NONE, 0));
		notify(SWT.MouseMove, createMouseEvent(x, y, 0, SWT.NONE, 0));
		notify(SWT.Activate);
		notify(SWT.FocusIn);
		notify(SWT.MouseDown, createMouseEvent(x, y, 1, SWT.NONE, 1));
		notify(SWT.MouseUp, createMouseEvent(x, y, 1, SWT.BUTTON1, 1));
		notify(SWT.MouseDown, createMouseEvent(x, y, 1, SWT.NONE, 2));
		notify(SWT.MouseDoubleClick, createMouseEvent(x, y, 1, SWT.NONE, 2));
		notify(SWT.MouseUp, createMouseEvent(x, y, 1, SWT.BUTTON1, 2));
		log.debug(MessageFormat.format("Double-clicked on {0}", this)); //$NON-NLS-1$
	}
}
