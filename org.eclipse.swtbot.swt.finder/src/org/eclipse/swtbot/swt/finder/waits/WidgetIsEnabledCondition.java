/*******************************************************************************
 * Copyright (c) 2010 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public boolean test() throws Exception {
		return widget.isEnabled();
	}

	public String getFailureMessage() {
		return "The widget " + widget + " was not enabled.";
	}

}
