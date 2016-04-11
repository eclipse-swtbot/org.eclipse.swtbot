/*******************************************************************************
 * Copyright (c) 2015, 2016 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Patrick Tasse - Initial API and implementation
 *                   - SWTBotView does not support dynamic view menus (Bug 489325)
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withId;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withMnemonic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.waits.WaitForObjectCondition;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

/**
 * SWTBot class representing a shell's menu bar, a control's pop up menu, or a
 * view's view menu.
 *
 * @author Patrick Tasse
 * @version $Id$
 * @since 2.4
 */
public class SWTBotRootMenu extends AbstractSWTBot<Menu> {

	/**
	 * @param w the widget.
	 * @param description the description of the widget, this will be reported by {@link #toString()}
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotRootMenu(Menu w, SelfDescribing description) throws WidgetNotFoundException {
		super(w, description);
	}

	/**
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotRootMenu(Menu w) throws WidgetNotFoundException {
		this(w, null);
	}

	/**
	 * Gets the menu item matching the given text.
	 * <p>
	 * This is equivalent to calling menu(text);
	 *
	 * @param text the text on the menu item.
	 * @return the menu item that has the given text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	@Override
	public SWTBotMenu contextMenu(String text) throws WidgetNotFoundException {
		return menu(text);
	}

	/**
	 * Gets the menu item matching the given text path relative to this menu. It
	 * will attempt to recursively find the menu items in sequence in the
	 * matching sub-menus that are found.
	 *
	 * @param texts the texts on the menu items that are to be found.
	 * @return the menu item that has the given text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotMenu menu(final String... texts) {
		if (texts == null || texts.length == 0) {
			throw new WidgetNotFoundException("Could not find menu item for empty text path"); //$NON-NLS-1$
		}
		SWTBotMenu menu = null;
		for (String text : texts) {
			if (menu == null) {
				menu = menu(text, false, 0);
			} else {
				menu = menu.menu(text, false, 0);
			}
		}
		return menu;
	}

	/**
	 * Gets the menu item matching the given text. If recursive is set, it will
	 * attempt to find the menu item recursively depth-first in each of the
	 * sub-menus that are found.
	 *
	 * @param text the text on the menu item that is to be found.
	 * @param recursive if set to true, will find depth-first in sub-menus as well.
	 * @param index the index of the menu item, in case there are multiple matching menu items.
	 * @return the menu item that has the given text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotMenu menu(final String text, final boolean recursive, final int index) throws WidgetNotFoundException {
		final Matcher<MenuItem> matcher = withMnemonic(text);
		return menu(matcher, recursive, index);
	}

	/**
	 * Gets the menu item matching the given matcher. If recursive is set, it will
	 * attempt to find the menu item recursively depth-first in each of the
	 * sub-menus that are found.
	 *
	 * @param matcher the matcher that can match menu items.
	 * @param recursive if set to true, will find depth-first in sub-menus as well.
	 * @param index the index of the menu item, in case there are multiple matching menu items.
	 * @return the menu item that matches the matcher.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotMenu menu(final Matcher<MenuItem> matcher, final boolean recursive, final int index) throws WidgetNotFoundException {
		WaitForObjectCondition<MenuItem> waitForMenuItem = Conditions.waitForMenuItem(this, matcher, recursive, index);
		new SWTBot().waitUntilWidgetAppears(waitForMenuItem);
		return new SWTBotMenu(waitForMenuItem.get(0), matcher);
	}

	/**
	 * Gets the menu item matching the given key/value pair in its widget data.
	 * If recursive is set, it will attempt to find the menu item recursively
	 * depth-first in each of the sub-menus that are found.
	 *
	 * @param key the key of the id.
	 * @param value the value of the id.
	 * @param recursive if set to true, will find depth-first in sub-menus as well.
	 * @param index the index of the menu item, in case there are multiple matching menu items.
	 * @return the menu item that has the given key/value pair.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotMenu menuWithId(final String key, final String value, final boolean recursive, final int index) throws WidgetNotFoundException {
		final Matcher<MenuItem> matcher = withId(key, value);
		return menu(matcher, recursive, index);
	}

	/**
	 * Hide this menu if it is a pop up menu or a view menu. Does nothing if it
	 * is a menu bar.
	 *
	 * @return itself.
	 */
	public SWTBotRootMenu hide() {
		syncExec(new VoidResult() {
			public void run() {
				if ((widget.getStyle() & SWT.POP_UP) != 0) {
					widget.notifyListeners(SWT.Hide, createEvent());
				}
			}
		});
		return this;
	}
}
