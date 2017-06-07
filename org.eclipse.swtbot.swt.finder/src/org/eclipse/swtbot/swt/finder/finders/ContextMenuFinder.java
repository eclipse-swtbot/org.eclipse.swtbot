/*******************************************************************************
 * Copyright (c) 2008, 2015 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Patrick Tasse - Fix ContextMenuFinder returns disposed menu items (Bug 458975)
 *                   - Improve SWTBot menu API and implementation (Bug 479091) 
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.finders;

import static org.eclipse.swtbot.swt.finder.utils.SWTUtils.createEvent;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.utils.internal.Assert;
import org.hamcrest.Matcher;

/**
 * Finds context menus for a given control.
 *
 * @see UIThreadRunnable
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class ContextMenuFinder extends MenuFinder {

	/**
	 * The control to find context menus.
	 */
	private final Control	control;

	/**
	 * Constructs the context menu finder for the given control to be searched.
	 *
	 * @param control the control that has a context menu.
	 */
	public ContextMenuFinder(Control control) {
		super();
		Assert.isNotNull(control, "The control cannot be null"); //$NON-NLS-1$
		this.control = control;
	}

	/**
	 * Finds the menu item matching the given matcher in this control's pop up
	 * menu. If recursive is set, it will attempt to find the menu item
	 * recursively depth-first in each of the sub-menus that are found.
	 *
	 * @param menu the menu.
	 * @param matcher the matcher that can match menus and menu items.
	 * @param recursive if set to true, will find depth-first in sub-menus as well.
	 * @param index the index of the menu item, in case there are multiple matching menu items.
	 * @return the menu item in the specified shell that matches the matcher, or null.
	 * @since 2.4
	 */
	public MenuItem findMenuItem(Matcher<MenuItem> matcher, boolean recursive, int index) {
		return findMenuItem(menuBar(null), matcher, recursive, index);
	}

	/**
	 * Finds all menu items matching the given matcher in this control's pop up
	 * menu. It will attempt to find the menu items recursively in each of the
	 * sub-menus that are found.
	 *
	 * @param matcher the matcher that can match menus and menu items.
	 * @return all menu items in this control's pop up menu that match the matcher.
	 */
	@Override
	public List<MenuItem> findMenus(Matcher<MenuItem> matcher) {
		return findMenus(menuBar(null), matcher, true);
	}

	@Override
	protected Menu menuBar(final Shell shell) {
		return UIThreadRunnable.syncExec(display, new WidgetResult<Menu>() {
			@Override
			public Menu run() {
				Menu popupMenu = control.getMenu();
				if (popupMenu != null) {
					popupMenu.notifyListeners(SWT.Show, createEvent(popupMenu));
				}
				return popupMenu;
			}
		});
	}
}
