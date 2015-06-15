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
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.finders;

import java.util.List;

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

	@Override
	public List<MenuItem> findMenus(Matcher<MenuItem> matcher) {
		return findMenus(menuBar(null), matcher, true);
	}

	@Override
	protected Menu menuBar(final Shell shell) {
		return UIThreadRunnable.syncExec(display, new WidgetResult<Menu>() {
			public Menu run() {
				return control.getMenu();
			}
		});
	}
}
