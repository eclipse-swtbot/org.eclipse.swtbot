/*******************************************************************************
 * Copyright (c) 2010 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.waits;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;

class WidgetIsEnabledCondition extends DefaultCondition {

	private final AbstractSWTBot<? extends Widget>	widget;

	WidgetIsEnabledCondition(AbstractSWTBot<? extends Widget> widget) {
		this.widget = widget;
	}

	@Override
	public boolean test() throws Exception {
		return widget.isEnabled();
	}

	@Override
	public String getFailureMessage() {
		return "The widget " + widget + " was not enabled.";
	}

}
