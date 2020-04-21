/*******************************************************************************
 * Copyright (c) 2009, 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.hamcrest.SelfDescribing;

/**
 * Helper to find SWT {@link Control}s and perform operations on them.
 * 
 * @author Joshua Gosse &lt;jlgosse [at] ca [dot] ibm [dot] com&gt;
 * @version $Id$
 */
public class AbstractSWTBotControl<T extends Control> extends AbstractSWTBot<T> {

	/**
	 * Constructs a new instance with the given widget.
	 * 
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public AbstractSWTBotControl(T w) throws WidgetNotFoundException {
		super(w);
	}

	/**
	 * Constructs a new instance with the given widget.
	 * 
	 * @param w the widget.
	 * @param description the description of the widget, this will be reported by {@link #toString()}
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public AbstractSWTBotControl(T w, SelfDescribing description) throws WidgetNotFoundException {
		super(w, description);
	}

	@Override
	protected Rectangle getBounds() {
		return syncExec(new Result<Rectangle>() {
			@Override
			public Rectangle run() {
				if (widget.isDisposed()) {
					return new Rectangle(0, 0, 0, 0);
				}
				return widget.getBounds();
			}
		});
	}

	@Override
	protected Control getDNDControl() {
		return widget;
	}

	@Override
	protected void dragStart() {
		setFocus();
		notify(SWT.Activate);
		notify(SWT.FocusIn);
		notify(SWT.MouseDown, createMouseEvent(1, SWT.NONE, 1));
	}

	/**
	 * Get the bounds of the Widget in relation to Display
	 * 
	 * @return the bounds of the Widget in relation to Display
	 */
	@Override
	protected Rectangle absoluteLocation() {
		return syncExec(new Result<Rectangle>() {
			@Override
			public Rectangle run() {
				return display.map(widget.getParent(), null, widget.getBounds());
			}
		});
	}

	/**
	 * Click on the center of the widget.
	 * 
	 * @param post Whether or not {@link Display#post} should be used
	 * @return itself.
	 */
	protected AbstractSWTBotControl<T> click(final boolean post) {
		setFocus();
		if (post) {
			Rectangle location = absoluteLocation();
			click(location.x + location.width / 2, location.y + location.height / 2, true);
		} else
			click();
		return this;
	}

	/**
	 * Right click on the center of the widget.
	 * 
	 * @param post Whether or not {@link Display#post} should be used
	 * @return itself
	 */
	protected AbstractSWTBotControl<T> rightClick(final boolean post) {
		if (post) {
			Rectangle location = absoluteLocation();
			rightClick(location.x + location.width / 2, location.y + location.height / 2, true);
		} else
			rightClick();
		return this;
	}

	/**
	 * Moves the cursor to the center of the widget
	 * 
	 * @return itself
	 */
	protected AbstractSWTBotControl<T> moveMouseToWidget() {
		syncExec(new VoidResult() {
			@Override
			public void run() {
				Rectangle location = absoluteLocation();
				moveMouse(location.x + location.width / 2, location.y + location.height / 2);
			}
		});
		return this;
	}
}
