/*******************************************************************************
 * Copyright (c) 2008, 2016 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Patrick Tasse - Fix radio menu item click behavior (Bug 451126 & Bug 397649)
 *                   - Improve SWTBot menu API and implementation (Bug 479091)
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withId;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withMnemonic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.results.ListResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.utils.MessageFormat;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.waits.WaitForObjectCondition;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

/**
 * SWTBot class representing a menu item.
 *
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
	 * Clicks on this menu item. Does nothing if this menu item has a menu.
	 */
	public SWTBotMenu click() {
		if (hasMenu()) {
			return this;
		}
		log.debug(MessageFormat.format("Clicking on {0}", this)); //$NON-NLS-1$
		waitForEnabled();
		asyncExec(new VoidResult() {
			/*
			 * Send SWT.Hide and SWT.Selection events from the same UI runnable
			 * to ensure that the UI runnable triggered from the E4 model to
			 * dispose hidden menus is queued after the SWT.Selection event.
			 */
			public void run() {
				hide();
				if (SWTUtils.hasStyle(widget, SWT.CHECK)) {
					toggleCheckSelection();
				} else if (SWTUtils.hasStyle(widget, SWT.RADIO)) {
					setRadioSelection();
				}
				SWTBotMenu.this.notify(SWT.Selection);
			};
		});
		syncExec(new VoidResult() {
			public void run() {
				// do nothing, just wait for sync.
			}
		});
		log.debug(MessageFormat.format("Clicked on {0}", this)); //$NON-NLS-1$
		return this;
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
	 * Recursively hide this menu item's parent menus until the root menu,
	 * including drop down menus and the pop up menu but excluding the menu bar.
	 *
	 * @return itself.
	 * @since 2.4
	 */
	public SWTBotMenu hide() {
		syncExec(new VoidResult() {
			public void run() {
				Menu menu = widget.getParent();
				/* the menu bar of a shell does not get hidden */
				while (menu instanceof Menu && ((menu.getStyle() & SWT.BAR) == 0)) {
					Event event = createEvent();
					event.widget = menu;
					menu.notifyListeners(SWT.Hide, event);
					menu = menu.getParentMenu();
				}
			}
		});
		return this;
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
	 * Gets the menu item matching the given text.
	 *
	 * @param text the text on the menu item that is to be found.
	 * @return the menu item that has the given text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotMenu menu(final String text) {
		return menu(text, false, 0);
	}

	/**
	 * Gets the menu item matching the given text path relative to this menu. It
	 * will attempt to recursively find the menu items in sequence in the
	 * matching sub-menus that are found.
	 *
	 * @param texts the texts on the menu items that are to be found.
	 * @return the menu item that has the given text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 2.4
	 */
	public SWTBotMenu menu(final String... texts) {
		if (texts == null || texts.length == 0) {
			return this;
		}
		SWTBotMenu menu = this;
		for (String text : texts) {
			menu = menu.menu(text, false, 0);
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
	 * @since 2.4
	 */
	public SWTBotMenu menu(final String text, final boolean recursive, final int index) throws WidgetNotFoundException {
		final Matcher<MenuItem> matcher = withMnemonic(text);
		return menu(matcher, recursive, index);
	}

	/**
	 * Gets the menu item matching the given matcher. If recursive is set, it
	 * will attempt to find the menu item recursively depth-first in each of the
	 * sub-menus that are found.
	 *
	 * @param matcher the matcher that can match menu items.
	 * @param recursive if set to true, will find depth-first in sub-menus as well.
	 * @param index the index of the menu item, in case there are multiple matching menu items.
	 * @return the menu item that matches the matcher.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 2.4
	 */
	public SWTBotMenu menu(Matcher<MenuItem> matcher, final boolean recursive, final int index) throws WidgetNotFoundException {
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
	 * @since 2.4
	 */
	public SWTBotMenu menuWithId(final String key, final String value, final boolean recursive, final int index) throws WidgetNotFoundException {
		final Matcher<MenuItem> matcher = withId(key, value);
		return menu(matcher, recursive, index);
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
	 * Returns true if this menu item has a menu, false otherwise.
	 *
	 * @return true if this menu item has a menu, false otherwise.
	 * @since 2.4
	 */
	public boolean hasMenu() {
		return syncExec(new BoolResult() {
			public Boolean run() {
				return widget.getMenu() != null;
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

	/**
	 * Returns the list of texts of this menu item's menu items, or an empty
	 * list if this menu item does not have a menu. The mnemonic character
	 * '&amp' and accelerator text are removed from each menu item's text.
	 * Separators are represented by empty strings.
	 *
	 * @return the list of menu item texts
	 * @since 2.5
	 */
	public List<String> menuItems() {
		return syncExec(new ListResult<String>() {
			public List<String> run() {
				List<String> items = new ArrayList<String>();
				Menu menu = widget.getMenu();
				if (menu != null) {
					for (MenuItem menuItem : menu.getItems()) {
						items.add(menuItem.getText().replaceAll("&", "").split("\t")[0]);
					}
				}
				return items;
			}
		});
	}
}
