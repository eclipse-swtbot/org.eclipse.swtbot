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

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.utils.SWTUtils.createEvent;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;

/**
 * Condition that waits for a control's pop up menu.
 *
 * @see Conditions
 * @author Patrick Tasse
 * @version $Id$
 * @since 2.4
 */
public class WaitForPopupMenu extends WaitForObjectCondition<Menu> {

	private final Control	control;

	/**
	 * Constructor.
	 *
	 * @param control the control.
	 */
	public WaitForPopupMenu(Control control) {
		super(widgetOfType(Menu.class));
		this.control = control;
	}

	@Override
	public String getFailureMessage() {
		return "Could not find pop up menu for control: " + control; //$NON-NLS-1$
	}

	@Override
	protected List<Menu> findMatches() {
		Menu popupMenu = UIThreadRunnable.syncExec(new WidgetResult<Menu>() {
			@Override
			public Menu run() {
				Menu menu = control.getMenu();
				if (menu != null) {
					menu.notifyListeners(SWT.Show, createEvent(menu));
				}
				return menu;
			}
		});
		if (popupMenu != null) {
			return Collections.singletonList(popupMenu);
		}
		return Collections.<Menu>emptyList();
	}
}
