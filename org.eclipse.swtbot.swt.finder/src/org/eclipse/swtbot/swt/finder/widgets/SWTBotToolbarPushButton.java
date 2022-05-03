/*******************************************************************************
 * Copyright (c) 2009, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *     Patrick Tasse - support click with modifiers
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swtbot.swt.finder.ReferenceBy;
import org.eclipse.swtbot.swt.finder.SWTBotWidget;
import org.eclipse.swtbot.swt.finder.Style;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.utils.internal.Assert;
import org.hamcrest.SelfDescribing;

/**
 * @author Mariot Chauvin &lt;mariot [dot] chauvin [at] obeo [dot] fr&gt;
 */
@SWTBotWidget(clasz = ToolItem.class, style = @Style(name = "SWT.PUSH", value = SWT.PUSH), preferredName = "toolbarButton", referenceBy = {
		ReferenceBy.MNEMONIC, ReferenceBy.TOOLTIP }, returnType = SWTBotToolbarButton.class)
public class SWTBotToolbarPushButton extends SWTBotToolbarButton {

	/**
	 * Constructs an new instance of this item.
	 *
	 * @param w the tool item.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotToolbarPushButton(ToolItem w) throws WidgetNotFoundException {
		this(w, null);
	}

	/**
	 * Constructs an new instance of this item.
	 *
	 * @param w the tool item.
	 * @param description the description of the widget, this will be reported by {@link #toString()}
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotToolbarPushButton(ToolItem w, SelfDescribing description) throws WidgetNotFoundException {
		super(w, description);
		Assert.isTrue(SWTUtils.hasStyle(w, SWT.PUSH), "Expecting a push button."); //$NON-NLS-1$
	}

	/**
	 * @since 2.3
	 */
	@Override
	public SWTBotToolbarPushButton click(int stateMask) {
		log.debug("Clicking on {}" + (stateMask != 0 ? " with stateMask=0x{1}" : ""), this, Integer.toHexString(stateMask)); //$NON-NLS-1$
		waitForEnabled();
		sendNotifications(stateMask);
		log.debug("Clicked on {}" + (stateMask != 0 ? " with stateMask=0x{1}" : ""), this, Integer.toHexString(stateMask)); //$NON-NLS-1$
		return this;
	}
}
