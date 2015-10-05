/*******************************************************************************
 * Copyright (c) 2009, 2015 SWTBot Committers and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Toby Weston - initial API and implementation (Bug 259860)
 *     Mickael Istria (Red Hat Inc.) - Bug 422458
 *     Patrick Tasse - Improve SWTBot menu API and implementation (Bug 479091) 
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withMnemonic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.EventContextMenuFinder;
import org.hamcrest.Matcher;

/**
 * Represents a tray item.
 *
 * @author Toby Weston (Bug 259860)
 * @version $Id$
 */
public class SWTBotTrayItem extends AbstractSWTBot<TrayItem> {

	/**
	 * Constructs a new instance with the given widget.
	 *
	 * @param widget the tray item.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotTrayItem(TrayItem widget) throws WidgetNotFoundException {
		super(widget);
	}

	@Override
	public SWTBotMenu contextMenu(String label) throws WidgetNotFoundException {
		EventContextMenuFinder finder = new EventContextMenuFinder();
		try {
			finder.register();
			notify(SWT.MenuDetect);
			Matcher<MenuItem> withMnemonic = withMnemonic(label);
			MenuItem menuItem = finder.findMenuItem((Shell) null, withMnemonic, true, 0);
			if (menuItem == null) {
				throw new WidgetNotFoundException("Could not find a menu item with label: " + label);
			}
			return new SWTBotMenu(menuItem, withMnemonic);
		} finally {
			finder.unregister();
		}
	}

	/**
	 * Convenience API for {@link #contextMenu(String)}
	 */
	public SWTBotMenu menu(String label) {
		return contextMenu(label);
	}
}
