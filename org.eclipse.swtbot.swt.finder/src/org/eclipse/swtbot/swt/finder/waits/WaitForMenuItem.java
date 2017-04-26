/*******************************************************************************
 * Copyright (c) 2015 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Patrick Tasse - Initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.waits;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.finders.MenuFinder;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotRootMenu;
import org.hamcrest.Matcher;

/**
 * Condition that waits for a menu's matching menu item.
 *
 * @see Conditions
 * @author Patrick Tasse
 * @version $Id$
 * @since 2.4
 */
public class WaitForMenuItem extends WaitForObjectCondition<MenuItem> {

	private final MenuFinder menuFinder = new MenuFinder();
	private final Widget widget;
	private final boolean recursive;
	private final int index;

	/**
	 * Constructor.
	 *
	 * @param menu the menu.
	 * @param matcher the matcher that can match menu items.
	 * @param recursive if set to true, will find depth-first in sub-menus as well.
	 * @param index the index of the menu item, in case there are multiple matching menu items.
	 */
	public WaitForMenuItem(SWTBotRootMenu menu, Matcher<MenuItem> matcher, boolean recursive, int index) {
		super(matcher);
		this.widget = menu.widget;
		this.recursive = recursive;
		this.index = index;
	}

	/**
	 * Constructor.
	 *
	 * @param menu the menu.
	 * @param matcher the matcher that can match menu items.
	 * @param recursive if set to true, will find depth-first in sub-menus as well.
	 * @param index the index of the menu item, in case there are multiple matching menu items.
	 */
	public WaitForMenuItem(SWTBotMenu menu, Matcher<MenuItem> matcher, boolean recursive, int index) {
		super(matcher);
		this.widget = menu.widget;
		this.recursive = recursive;
		this.index = index;
	}

	public String getFailureMessage() {
		return "Could not find menu item matching: " + matcher; //$NON-NLS-1$
	}

	@Override
	protected List<MenuItem> findMatches() {
		MenuItem menuItem = UIThreadRunnable.syncExec(new WidgetResult<MenuItem>() {
			public MenuItem run() {
				if (widget instanceof Menu) {
					return menuFinder.findMenuItem((Menu) widget, matcher, recursive, index);
				} else if (widget instanceof MenuItem) {
					return menuFinder.findMenuItem(((MenuItem) widget).getMenu(), matcher, recursive, index);
				}
				return null;
			}
		});
		if (menuItem != null) {
			return Collections.singletonList(menuItem);
		}
		return Collections.<MenuItem>emptyList();
	}
}
