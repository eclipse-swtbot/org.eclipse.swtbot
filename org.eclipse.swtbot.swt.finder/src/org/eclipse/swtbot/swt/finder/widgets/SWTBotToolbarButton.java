/*******************************************************************************
 * Copyright (c) 2008, 2015 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Patrick Tasse - support click with modifiers
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.hamcrest.SelfDescribing;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public abstract class SWTBotToolbarButton extends AbstractSWTBot<ToolItem> {

	/**
	 * Constructs an new instance of this item.
	 *
	 * @param w the tool item.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotToolbarButton(ToolItem w) throws WidgetNotFoundException {
		this(w, null);
	}

	/**
	 * Constructs an new instance of this item.
	 *
	 * @param w the tool item.
	 * @param description the description of the widget, this will be reported by {@link #toString()}
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotToolbarButton(ToolItem w, SelfDescribing description) throws WidgetNotFoundException {
		super(w, description);
	}

	/**
	 * Click on the tool item.
	 *
	 * @since 1.0
	 */
	@Override
	public SWTBotToolbarButton click() {
		return click(0);
	}

	/**
	 * Click on the tool item with a particular state mask.
	 *
	 * @param stateMask the state of the keyboard modifier keys.
	 *
	 * @since 2.3
	 */
	public abstract SWTBotToolbarButton click(int stateMask);

	protected void sendNotifications() {
		sendNotifications(0);
	}

	/**
	 * @since 2.3
	 */
	protected void sendNotifications(int stateMask) {
		notify(SWT.MouseEnter);
		notify(SWT.MouseMove);
		notify(SWT.Activate);
		notify(SWT.MouseDown);
		notify(SWT.MouseUp);
		notify(SWT.Selection, createSelectionEvent(stateMask));
		notify(SWT.MouseHover);
		notify(SWT.MouseMove);
		notify(SWT.MouseExit);
		notify(SWT.Deactivate);
		notify(SWT.FocusOut);
	}

	@Override
	public boolean isEnabled() {
		return syncExec(new BoolResult() {
			@Override
			public Boolean run() {
				return widget.isEnabled();
			}
		});
	}
}
