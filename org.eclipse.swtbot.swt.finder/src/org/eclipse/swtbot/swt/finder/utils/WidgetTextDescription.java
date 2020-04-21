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
 *     Patrick Tasse - use tooltip text if text is empty
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.utils;

import org.eclipse.swt.widgets.Widget;
import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

/**
 * Describes the widget, by invoking {@link SWTUtils#getText(Object)} or
 * {@link SWTUtils#getToolTipText(Object)} on the widget.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class WidgetTextDescription implements SelfDescribing {

	private final Widget	widget;

	public WidgetTextDescription(Widget widget) {
		this.widget = widget;
	}

	@Override
	public void describeTo(Description description) {
		String text = SWTUtils.getText(widget);
		if (text.isEmpty()) {
			String toolTipText = SWTUtils.getToolTipText(widget);
			if (!toolTipText.isEmpty()) {
				description.appendText(ClassUtils.simpleClassName(widget) + " with tooltip text {" + toolTipText + "}"); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			}
		}
		description.appendText(ClassUtils.simpleClassName(widget) + " with text {" + text + "}"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Override
	public String toString() {
		return StringDescription.asString(this);
	}

}
