/*******************************************************************************
 * Copyright (c) 2008, 2015 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Patrick Tasse - Fix radio menu item click behavior (Bug 451126 & Bug 397649)
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withMnemonic;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.MenuFinder;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.utils.MessageFormat;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotMenu extends AbstractSWTBot<MenuItem> {

	/**
	 * @param w the widget.
	 * @param description the description of the widget, this will be reported by {@link #toString()}
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotMenu(MenuItem w, SelfDescribing description) throws WidgetNotFoundException {
		super(w, description);
	}

	/**
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotMenu(MenuItem w) throws WidgetNotFoundException {
		this(w, null);
	}

	/**
	 * Clicks on the menu item
	 */
	public SWTBotMenu click() {
		log.debug(MessageFormat.format("Clicking on {0}", this)); //$NON-NLS-1$
		waitForEnabled();
		if (SWTUtils.hasStyle(widget, SWT.CHECK)) {
			toggleCheckSelection();
		} else if (SWTUtils.hasStyle(widget, SWT.RADIO)) {
			setRadioSelection();
		}
		notify(SWT.Selection);
		log.debug(MessageFormat.format("Clicked on {0}", this)); //$NON-NLS-1$
		return this;
	}

	@Override
	public SWTBotMenu contextMenu(String text) throws WidgetNotFoundException {
		return menu(text);
	}

	/**
	 * Toggle the selection of the check menu item.
	 */
	private void toggleCheckSelection() {
		syncExec(new VoidResult() {
			public void run() {
				widget.setSelection(!widget.getSelection());
			}
		});
	}

	/**
	 * Set the selection of the radio menu item and clear the selection of any
	 * other radio menu item in the same group.
	 */
	private void setRadioSelection() {
		final SWTBotMenu otherSelectedRadioItem = otherSelectedRadioItem();
		if (otherSelectedRadioItem != null) {
			otherSelectedRadioItem.notify(SWT.Deactivate);
			asyncExec(new VoidResult() {
				public void run() {
					otherSelectedRadioItem.widget.setSelection(false);
				}
			});
			otherSelectedRadioItem.notify(SWT.Selection);
		}
		syncExec(new VoidResult() {
			public void run() {
				widget.setSelection(true);
			}
		});
	}

	private SWTBotMenu otherSelectedRadioItem() {
		MenuItem other = syncExec(new WidgetResult<MenuItem>() {
			public MenuItem run() {
				if (hasStyle(widget.getParent(), SWT.NO_RADIO_GROUP))
					return null;
				Widget[] siblings = SWTUtils.siblings(widget);
				boolean ownGroup = false;
				MenuItem selected = null;
				for (Widget sibling : siblings) {
					if (sibling == widget) {
						ownGroup = true;
					} else if (((sibling instanceof MenuItem) && hasStyle(sibling, SWT.RADIO))) {
						if (((MenuItem) sibling).getSelection()) {
							selected = (MenuItem) sibling;
						}
					} else if ((sibling instanceof MenuItem) && hasStyle(sibling, SWT.SEPARATOR)) {
						ownGroup = false;
						selected = null;
					}
					if (ownGroup && selected != null) {
						return selected;
					}
				}
				return null;
			}
		});

		if (other != null)
			return new SWTBotMenu(other);
		return null;
	}

	/**
	 * Gets the menu item matching the given name.
	 *
	 * @param menuName the name of the menu item that is to be found
	 * @return the first menu that matches the menuName
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotMenu menu(final String menuName) throws WidgetNotFoundException {
		final Matcher<? extends Widget> matcher = withMnemonic(menuName);
		MenuItem menuItem = syncExec(new WidgetResult<MenuItem>() {
			public MenuItem run() {
				Menu bar = widget.getMenu();
				Matcher<MenuItem> withMnemonic = withMnemonic(menuName);
				List<MenuItem> menus = new MenuFinder().findMenus(bar, withMnemonic, true);
				if (!menus.isEmpty())
					return menus.get(0);
				return null;
			}
		});
		return new SWTBotMenu(menuItem, matcher);
	}

	@Override
	public boolean isEnabled() {
		return syncExec(new BoolResult() {
			public Boolean run() {
				return widget.isEnabled();
			}
		});
	}

	/**
	 * Gets if this menu item is checked.
	 *
	 * @return <code>true</code> if the menu is checked, <code>false</code> otherwise.
	 * @see MenuItem#getSelection()
	 * @since 1.2
	 */
	public boolean isChecked() {
		return syncExec(new BoolResult() {
			public Boolean run() {
				return widget.getSelection();
			}
		});
	}
}
