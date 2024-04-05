/*******************************************************************************
 * Copyright (c) 2024 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Tasse - Initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.waits;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;

class WidgetBoundsNotEmptyCondition extends DefaultCondition {

	private final AbstractSWTBot<? extends Widget>	widget;

	WidgetBoundsNotEmptyCondition(AbstractSWTBot<? extends Widget> widget) {
		this.widget = widget;
	}

	@Override
	public boolean test() throws Exception {
		return !widget.areBoundsEmpty();
	}

	@Override
	public String getFailureMessage() {
		return "The widget " + widget + " bounds were empty.";
	}

}
