/*******************************************************************************
 * Copyright (c) 2012 Lorenzo Bettini and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stefan Seelmann (initial)
 *     Stefan Schaefer (extension)
 *     Lorenzo Bettini (extracted method to get the MenuItem)
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.finders;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withMnemonic;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;
import org.hamcrest.Matcher;

/**
 * This helper is a workaround for a bug in SWTBot, where the bot can't find a
 * dynamically created context menu, see also
 * http://www.eclipse.org/forums/index.php/t/11863/
 * 
 * @author Stefan Seelmann (initial)
 * @author Stefan Schaefer (extension)
 * @author Lorenzo Bettini (extracted method to get the MenuItem)
 */
public class ContextMenuHelper {

	public static MenuItem contextMenu(
			final AbstractSWTBot<? extends Control> bot, final String... texts) {
		return UIThreadRunnable.syncExec(new WidgetResult<MenuItem>() {
			public MenuItem run() {
				MenuItem menuItem = null;
				Control control = bot.widget;

				// MenuDetectEvent added by Stefan Schaefer
				Event event = new Event();
				control.notifyListeners(SWT.MenuDetect, event);
				if (!event.doit) {
					return null;
				}

				Menu menu = control.getMenu();
				for (String text : texts) {
					@SuppressWarnings("unchecked")
					Matcher<?> matcher = allOf(instanceOf(MenuItem.class),
							withMnemonic(text));
					menuItem = show(menu, matcher);
					if (menuItem != null) {
						menu = menuItem.getMenu();
					} else {
						hide(menu);
						throw new WidgetNotFoundException(
								"Could not find menu: " + text); //$NON-NLS-1$
					}
				}

				return menuItem;
			}
		});
	}

	private static MenuItem show(final Menu menu, final Matcher<?> matcher) {
		if (menu != null) {
			menu.notifyListeners(SWT.Show, new Event());
			MenuItem[] items = menu.getItems();
			for (final MenuItem menuItem : items) {
				if (matcher.matches(menuItem)) {
					return menuItem;
				}
			}
			menu.notifyListeners(SWT.Hide, new Event());
		}
		return null;
	}

	private static void hide(final Menu menu) {
		menu.notifyListeners(SWT.Hide, new Event());
		if (menu.getParentMenu() != null) {
			hide(menu.getParentMenu());
		}
	}
}
