/*******************************************************************************
 * Copyright (c) 2016 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Aparna Argade(Cadence Design Systems, Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.nebula.nattable.finder.waits;

import static org.eclipse.swtbot.swt.finder.utils.SWTUtils.createEvent;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.ui.action.IMouseAction;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swtbot.nebula.nattable.finder.finders.NatTableContextMenuFinder;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.waits.WaitForPopupMenu;

public class WaitForNatTablePopupMenu extends WaitForPopupMenu {
	private final Control control;
	private final int x, y;

	/**
	 * Constructor.
	 *
	 * @param control
	 *            the control.
	 * @param x
	 *            the x coordinate relative to natTable.
	 * @param y
	 *            the y coordinate relative to natTable.
	 *
	 */
	public WaitForNatTablePopupMenu(Control control, int x, int y) {
		super(control);
		this.control = control;
		this.x = x;
		this.y = y;
	}

	@Override
	protected List<Menu> findMatches() {
		Menu popupMenu = UIThreadRunnable.syncExec(new WidgetResult<Menu>() {
			@Override
			public Menu run() {
				Menu menu = control.getMenu();
				if (menu == null) {
					if (control instanceof NatTable) {
						menu = getNatTableMenu();
					}
				}
				if (menu != null) {
					menu.notifyListeners(SWT.Show, createEvent(menu));
				}
				return menu;
			}
		});
		if (popupMenu != null) {
			return Collections.singletonList(popupMenu);
		}
		return Collections.<Menu> emptyList();
	}

	/**
	 * Get popupMenuAction from ui Binding Registry of NatTable by creating
	 * MouseDown event. The private variable menu is obtained by using Java
	 * reflection.
	 *
	 * @return Menu of NatTable for the given cell
	 */
	private Menu getNatTableMenu() {
		Event event = NatTableContextMenuFinder.createMouseEvent(control, x, y, 3, SWT.NONE, 1);
		MouseEvent mouseevent = new MouseEvent(event);
		IMouseAction action = ((NatTable) control).getUiBindingRegistry().getMouseDownAction(mouseevent);
		if (action instanceof PopupMenuAction) {
			PopupMenuAction popupMenuAction = (PopupMenuAction) action;
			Field privateMenu = null;
			try {
				privateMenu = PopupMenuAction.class.getDeclaredField("menu");
			} catch (NoSuchFieldException e) {
				return null;
			} catch (SecurityException e) {
				return null;
			}
			privateMenu.setAccessible(true);
			try {
				return (Menu) privateMenu.get(popupMenuAction);
			} catch (IllegalArgumentException e) {
				return null;
			} catch (IllegalAccessException e) {
				return null;
			}
		}
		return null;
	}
}