/*******************************************************************************
 * Copyright (c) 2008, 2020 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Ralf Ebert www.ralfebert.de - (bug 271630) SWTBot Improved RCP / Workbench support
 *     Patrick Tasse - SWTBotView does not support dynamic view menus (Bug 489325)
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.widgets;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import javax.swing.text.View;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.utils.PartLabelDescription;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.WaitForObjectCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotRootMenu;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.hamcrest.SelfDescribing;

/**
 * This represents the eclipse {@link View} item.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @author Ralf Ebert www.ralfebert.de (bug 271630)
 * @version $Id$
 */
public class SWTBotView extends SWTBotWorkbenchPart<IViewReference> {

	/**
	 * Creates an instance of a view part.
	 * 
	 * @param partReference the view reference representing this view.
	 * @param bot the bot that's used to find controls within this view.
	 * @since 2.0
	 */
	public SWTBotView(IViewReference partReference, SWTWorkbenchBot bot) {
		this(partReference, bot, new PartLabelDescription<IViewReference>(partReference));
	}

	/**
	 * Creates an instance of a view part.
	 * 
	 * @param partReference the part reference.
	 * @param bot the helper bot.
	 * @param description the description of the workbench part.
	 */
	public SWTBotView(IViewReference partReference, SWTWorkbenchBot bot, SelfDescribing description) {
		super(partReference, bot, description);
	}

	@Override
	public void setFocus() {
		syncExec(new VoidResult() {
			@Override
			public void run() {
				((Control) getWidget()).setFocus();
			}
		});
	}

	/**
	 * @return the view reference for this view.
	 */
	public IViewReference getViewReference() {
		return partReference;
	}

	@Override
	public boolean isActive() {
		return partReference.getPage().getActivePartReference() == partReference;
	}

	/**
	 * Close the partReference.
	 */
	@Override
	public void close() {
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
				partReference.getPage().hideView(partReference);
			}
		});
	}

	/**
	 * Gets the view menu of this view.
	 *
	 * @return the view menu.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 2.4
	 */
	public SWTBotRootMenu viewMenu() {
		WaitForObjectCondition<Menu> waitForMenu = Conditions.waitForViewMenu(partReference);
		new SWTBot().waitUntilWidgetAppears(waitForMenu);
		return new SWTBotRootMenu(waitForMenu.get(0));
	}

	/**
	 * Gets the view menu item matching the given text. It will attempt to
	 * find the menu item recursively in each of the sub-menus that are found.
	 * <p>
	 * This is equivalent to calling viewMenu().menu(text, true, 0);
	 *
	 * @param text the text on the view menu item.
	 * @return the view menu item that has the given text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 2.4
	 */
	public SWTBotMenu viewMenu(final String text) throws WidgetNotFoundException {
		return viewMenu().menu(text, true, 0);
	}

	@Override
	public void show() {
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
				try {
					partReference.getPage().activate(partReference.getPart(true));
					partReference.getPage().showView(partReference.getId(), partReference.getSecondaryId(), IWorkbenchPage.VIEW_ACTIVATE);
				} catch (PartInitException e) {
					throw new RuntimeException("Could not show partReference - " + partReference.getPartName(), e); //$NON-NLS-1$
				}
			}
		});
	}
}
