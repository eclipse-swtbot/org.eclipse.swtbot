/*******************************************************************************
 * Copyright (c) 2008, 2015 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Patrick Tasse - Improve SWTBot menu API and implementation (Bug 479091) 
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.finders;

import static org.eclipse.swtbot.swt.finder.utils.SWTUtils.createEvent;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.results.ArrayResult;
import org.eclipse.swtbot.swt.finder.results.ListResult;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.hamcrest.Matcher;

/**
 * Finds menus matching a particular matcher.
 *
 * @see UIThreadRunnable
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class MenuFinder {

	/**
	 * The logging instance for this class.
	 */
	private static final Logger	log	= Logger.getLogger(MenuFinder.class);

	/** The display */
	protected final Display		display;

	/**
	 * Creates a MenuFinder.
	 */
	public MenuFinder() {
		display = SWTUtils.display();
	}

	/**
	 * Finds the menu item matching the given matcher in the given shell. If
	 * recursive is set, it will attempt to find the menu item recursively
	 * depth-first in each of the sub-menus that are found.
	 *
	 * @param shell the shell to probe for menus.
	 * @param matcher the matcher that can match menu items.
	 * @param recursive if set to true, will find depth-first in sub-menus as well.
	 * @param index the index of the menu item, in case there are multiple matching menu items.
	 * @return the menu item in the specified shell that matches the matcher, or null.
	 * @since 2.4
	 */
	public MenuItem findMenuItem(final Shell shell, Matcher<MenuItem> matcher, boolean recursive, int index) {
		return findMenuItem(menuBar(shell), matcher, recursive, index);
	}

	/**
	 * Finds all menu items matching the given matcher in all available shells.
	 * It will attempt to find the menu items recursively in each of the
	 * sub-menus that are found.
	 *
	 * @param matcher the matcher that can match menus and menu items.
	 * @return all menu items in all shells that match the matcher.
	 */
	public List<MenuItem> findMenus(Matcher<MenuItem> matcher) {
		return findMenus(getShells(), matcher, true);
	}

	/**
	 * Finds all menu items matching the given matcher in the set of shells
	 * provided. If recursive is set, it will attempt to find the menu items
	 * recursively in each of the sub-menus that are found.
	 *
	 * @param shells the shells to probe for menus.
	 * @param matcher the matcher that can match menus and menu items.
	 * @param recursive if set to true, will find depth-first in sub-menus as well.
	 * @return all menu items in the specified shells that match the matcher.
	 */
	public List<MenuItem> findMenus(Shell[] shells, Matcher<MenuItem> matcher, boolean recursive) {
		LinkedHashSet<MenuItem> result = new LinkedHashSet<MenuItem>();
		for (Shell shell : shells) {
			List<MenuItem> findMenus = findMenus(shell, matcher, recursive);
			result.addAll(findMenus);
		}
		return new ArrayList<MenuItem>(result);
	}

	/**
	 * Finds all menu items matching the given matcher in the given shell. If
	 * recursive is set, it will attempt to find the menu items recursively in
	 * each of the sub-menus that are found.
	 *
	 * @param shell the shell to probe for menus.
	 * @param matcher the matcher that can match menus and menu items.
	 * @param recursive if set to true, will find depth-first in sub-menus as well.
	 * @return all menu items in the specified shell that match the matcher.
	 */
	public List<MenuItem> findMenus(final Shell shell, Matcher<MenuItem> matcher, boolean recursive) {
		LinkedHashSet<MenuItem> result = new LinkedHashSet<MenuItem>();
		result.addAll(findMenus(menuBar(shell), matcher, recursive));
		return new ArrayList<MenuItem>(result);
	}

	/**
	 * Gets the menu bar in the given shell.
	 *
	 * @param shell the shell.
	 * @return the menu in the shell.
	 * @see Shell#getMenuBar()
	 */
	protected Menu menuBar(final Shell shell) {
		return UIThreadRunnable.syncExec(display, new WidgetResult<Menu>() {
			public Menu run() {
				return shell.getMenuBar();
			}
		});

	}

	/**
	 * Finds the menu item matching the given matcher in the given menu. If
	 * recursive is set, it will attempt to find the menu item recursively
	 * depth-first in each of the sub-menus that are found.
	 *
	 * @param menu the menu.
	 * @param matcher the matcher that can match menu items.
	 * @param recursive if set to true, will find depth-first in sub-menus as well.
	 * @param index the index of the menu item, in case there are multiple matching menu items.
	 * @return the menu item in the specified shell that matches the matcher, or null.
	 * @since 2.4
	 */
	public MenuItem findMenuItem(final Menu menu, final Matcher<MenuItem> matcher, final boolean recursive, final int index) {
		return UIThreadRunnable.syncExec(new WidgetResult<MenuItem>() {
			public MenuItem run() {
				return findMenuItemInternal(menu, matcher, recursive, new int[] { index } );
			}
		});
	}

	/**
	 * Finds all menu items matching the given matcher in the given menu. If
	 * recursive is set, it will attempt to find the menu items recursively in
	 * each of the sub-menus that are found.
	 *
	 * @param menu the menu.
	 * @param matcher the matcher that can match menu items.
	 * @param recursive if set to true, will find depth-first in sub-menus as well.
	 * @return all menu items in the specified menu that match the matcher.
	 */
	public List<MenuItem> findMenus(final Menu menu, final Matcher<MenuItem> matcher, final boolean recursive) {
		return UIThreadRunnable.syncExec(display, new ListResult<MenuItem>() {
			public List<MenuItem> run() {
				return findMenusInternal(menu, matcher, recursive);
			}
		});
	}

	/**
	 * Gets all of the shells in the current display.
	 *
	 * @return all shells in the display.
	 * @see Display#getShells()
	 */
	protected Shell[] getShells() {
		return UIThreadRunnable.syncExec(display, new ArrayResult<Shell>() {
			public Shell[] run() {
				return display.getShells();
			}
		});
	}

	/**
	 * @param menu
	 * @param matcher
	 * @param recursive
	 * @param index
	 * @return
	 */
	private MenuItem findMenuItemInternal(final Menu menu, final Matcher<MenuItem> matcher, final boolean recursive, final int[] index) {
		if (menu == null) {
			return null;
		}
		MenuItem[] items = menu.getItems();
		for (MenuItem menuItem : items) {
			if (menuItem.isDisposed() || isSeparator(menuItem)) {
				continue;
			}
			if (matcher.matches(menuItem)) {
				if (index[0]-- > 0) {
					continue;
				}
				menuItem.notifyListeners(SWT.Arm, createEvent(menuItem));
				Menu subMenu = menuItem.getMenu();
				if (subMenu != null) {
					subMenu.notifyListeners(SWT.Show, createEvent(subMenu));
				}
				return menuItem;
			}
			if (recursive) {
				Menu subMenu = menuItem.getMenu();
				if (subMenu != null) {
					menuItem.notifyListeners(SWT.Arm, createEvent(menuItem));
					subMenu.notifyListeners(SWT.Show, createEvent(subMenu));
					MenuItem subMenuItem = findMenuItemInternal(subMenu, matcher, recursive, index);
					if (subMenuItem != null) {
						return subMenuItem;
					}
					subMenu.notifyListeners(SWT.Hide, createEvent(subMenu));
				}
			}
		}
		return null;
	}

	/**
	 * @param menu
	 * @param matcher
	 * @param recursive
	 * @return
	 */
	private List<MenuItem> findMenusInternal(final Menu menu, final Matcher<MenuItem> matcher, final boolean recursive) {
		LinkedHashSet<MenuItem> result = new LinkedHashSet<MenuItem>();
		if (menu != null) {
			MenuItem[] items = menu.getItems();
			for (MenuItem menuItem : items) {
				if (menuItem.isDisposed() || isSeparator(menuItem)) {
					continue;
				}
				boolean matches = matcher.matches(menuItem);
				if (matches) {
					menuItem.notifyListeners(SWT.Arm, createEvent(menuItem));
					Menu subMenu = menuItem.getMenu();
					if (subMenu != null) {
						subMenu.notifyListeners(SWT.Show, createEvent(subMenu));
					}
					result.add(menuItem);
				}
				if (recursive) {
					Menu subMenu = menuItem.getMenu();
					if (subMenu != null) {
						if (!matches) {
							menuItem.notifyListeners(SWT.Arm, createEvent(menuItem));
							subMenu.notifyListeners(SWT.Show, createEvent(subMenu));
						}
						List<MenuItem> menuItems = findMenusInternal(subMenu, matcher, recursive);
						if (!menuItems.isEmpty()) {
							// Do not close menus which contain the item we're looking for - this destroys dynamic menu contributions
							// giving us the SWT MenuItem but without a E4 model attached (and therefore cannot be used).
							// @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=469581
							result.addAll(menuItems);
						} else if (!matches) {
							subMenu.notifyListeners(SWT.Hide, createEvent(subMenu));
						}
					}
				}
			}
		}
		return new ArrayList<MenuItem>(result);
	}

	private boolean isSeparator(MenuItem menuItem) {
		// FIXME see https://bugs.eclipse.org/bugs/show_bug.cgi?id=208188
		// FIXED > 20071101 https://bugs.eclipse.org/bugs/show_bug.cgi?id=208188#c2
		return (menuItem.getStyle() & SWT.SEPARATOR) != 0;
	}

}
