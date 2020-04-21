/*******************************************************************************
 * Copyright (c) 2016 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Aparna Argade(Cadence Design Systems, Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.nebula.nattable.finder.finders;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swtbot.nebula.nattable.finder.waits.WaitForNatTablePopupMenu;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.WaitForObjectCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotRootMenu;

public class NatTableContextMenuFinder {
	/**
	 * Gets the context menu item matching the given text on the given control.
	 * It will attempt to find the menu item recursively in each of the
	 * sub-menus that are found.
	 * <p>
	 * This is equivalent to calling contextMenu(control).menu(text, true, 0);
	 *
	 * @param control
	 *            the control.
	 * @param x
	 *            the x coordinate relative to NatTable
	 * @param y
	 *            the y coordinate relative to NatTable
	 * @param text
	 *            the text on the context menu item.
	 * @return the context menu item that has the given text.
	 * @throws WidgetNotFoundException
	 *             if the widget is not found.
	 */
	public static SWTBotMenu contextMenu(final Control control, final int x, final int y, final String text)
			throws WidgetNotFoundException {
		return contextMenu(control, x, y).menu(text, true, 0);
	}

	/**
	 * Gets the context menu of the given control.
	 *
	 * @param control
	 *            the control.
	 * @param x
	 *            the x coordinate relative to NatTable
	 * @param y
	 *            the y coordinate relative to NatTable
	 * @return the context menu.
	 * @throws WidgetNotFoundException
	 *             if the widget is not found.
	 */
	public static SWTBotRootMenu contextMenu(final Control control, final int x, final int y)
			throws WidgetNotFoundException {
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
				control.notifyListeners(SWT.MouseDown, createMouseEvent(control, x, y, 3, SWT.NONE, 1));
			}
		});
		notifyMenuDetect(control, x, y);

		WaitForObjectCondition<Menu> waitForMenu = waitForNatTablePopupMenu(control, x, y);
		new SWTBot().waitUntilWidgetAppears(waitForMenu);
		return new SWTBotRootMenu(waitForMenu.get(0));
	}

	/**
	 * Notify the control of SWT.MenuDetect when a context menu occurs on a
	 * widget at given coordinates.
	 *
	 * @param control
	 *            the control that should be notified
	 * @param x
	 *            the x coordinate relative to NatTable
	 * @param y
	 *            the y coordinate relative to NatTable
	 */
	public static boolean notifyMenuDetect(final Control control, final int x, final int y) {
		final Event event = new Event();
		event.time = (int) System.currentTimeMillis();
		event.display = control.getDisplay();
		event.widget = control;
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
				Point mappedPt = control.toDisplay(new Point(x, y));
				event.x = mappedPt.x;
				event.y = mappedPt.y;
				control.notifyListeners(SWT.MenuDetect, event);
			}
		});
		return event.doit;
	}

	/**
	 * Create a mouse event
	 *
	 * @param x
	 *            the x coordinate of the mouse event.
	 * @param y
	 *            the y coordinate of the mouse event.
	 * @param button
	 *            the mouse button that was clicked.
	 * @param stateMask
	 *            the state of the keyboard modifier keys.
	 * @param count
	 *            the number of times the mouse was clicked.
	 * @return an event that encapsulates {@link #widget} and {@link #display}
	 */
	public static Event createMouseEvent(final Control control, int x, int y, int button, int stateMask, int count) {
		Event event = new Event();
		event.time = (int) System.currentTimeMillis();
		event.widget = control;
		event.display = control.getDisplay();
		event.x = x;
		event.y = y;
		event.button = button;
		event.stateMask = stateMask;
		event.count = count;
		return event;
	}

	/**
	 * Gets the condition to wait for a control's pop up menu.
	 *
	 * @param control
	 *            the control.
	 * @param x
	 *            the x coordinate relative to NatTable
	 * @param y
	 *            the y coordinate relative to NatTable
	 * @return a condition that waits for the control's pop up menu.
	 */
	public static WaitForObjectCondition<Menu> waitForNatTablePopupMenu(Control control, final int x, final int y) {
		return new WaitForNatTablePopupMenu(control, x, y);
	}
}
