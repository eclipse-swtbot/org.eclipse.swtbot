/*******************************************************************************
 * Copyright (c) 2016 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Patrick Tasse - SWTBotView does not support dynamic view menus (Bug 489325)
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.waits;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.utils.SWTUtils.createEvent;

import java.util.Collections;
import java.util.List;

import org.eclipse.e4.ui.model.application.ui.basic.impl.PartImpl;
import org.eclipse.e4.ui.workbench.renderers.swt.StackRenderer;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.waits.WaitForObjectCondition;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;

/**
 * Condition that waits for a view's view menu.
 *
 * @see Conditions
 * @author Patrick Tasse
 * @version $Id$
 * @since 2.4
 */
public class WaitForViewMenu extends WaitForObjectCondition<Menu> {

	/* this is a private constant from StackRenderer */
	private static final String THE_PART_KEY = "thePart";

	private final IViewReference viewReference;

	/**
	 * Constructor.
	 *
	 * @param viewReference the view reference.
	 */
	public WaitForViewMenu(IViewReference viewReference) {
		super(widgetOfType(Menu.class));
		this.viewReference = viewReference;
	}

	@Override
	public String getFailureMessage() {
		return "Could not find view menu for view: " + viewReference; //$NON-NLS-1$
	}

	@Override
	protected List<Menu> findMatches() {
		List<Menu> emptyList = Collections.<Menu>emptyList();

		final IViewPart viewPart = viewReference.getView(false);
		if (viewPart == null) {
			return emptyList;
		}
		final ToolItem toolItem = UIThreadRunnable.syncExec(new WidgetResult<ToolItem>() {
			@Override
			public ToolItem run() {
				String elementId = viewReference.getSecondaryId() == null ? viewReference.getId() :
					viewReference.getId() + ':' + viewReference.getSecondaryId();
				return findViewMenuToolItem(viewPart.getViewSite().getShell(), elementId);
			}
		});
		if (toolItem == null) {
			return emptyList;
		}
		UIThreadRunnable.asyncExec(new VoidResult() {
			@Override
			public void run() {
				toolItem.notifyListeners(SWT.Selection, createEvent(toolItem));
			}
		});
		Menu viewMenu = UIThreadRunnable.syncExec(new WidgetResult<Menu>() {
			@Override
			public Menu run() {
				IMenuManager viewMenuManager = viewPart.getViewSite().getActionBars().getMenuManager();
				if (viewMenuManager instanceof MenuManager) {
					MenuManager menuManager = (MenuManager) viewMenuManager;
					Menu menu = menuManager.getMenu();
					if (menu != null) {
						menu.setVisible(false);
						menu.notifyListeners(SWT.Show, createEvent(menu));
						return menu;
					}
				}
				return null;
			}
		});
		if (viewMenu != null) {
			return Collections.singletonList(viewMenu);
		}
		return emptyList;
	}

	private ToolItem findViewMenuToolItem(Composite composite, String elementId) {
		for (Control control : composite.getChildren()) {
			if (control instanceof ToolBar) {
				ToolBar toolBar = (ToolBar) control;
				if (StackRenderer.TAG_VIEW_MENU.equals(toolBar.getData())) {
					for (ToolItem toolItem : toolBar.getItems()) {
						Object thePart = toolItem.getData(THE_PART_KEY);
						if (thePart instanceof PartImpl) {
							PartImpl partImpl = (PartImpl) thePart;
							if (partImpl.getElementId().equals(elementId)) {
								return toolItem;
							}
						}
					}
				}
			} else if (control instanceof Composite) {
				ToolItem toolItem = findViewMenuToolItem((Composite) control, elementId);
				if (toolItem != null) {
					return toolItem;
				}
			}
		}
		return null;
	}
}
