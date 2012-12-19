/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.generator.framework.GenerationRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class CheckboxClickedRule extends GenerationRule {

	private String buttonText;
	private int index;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof Button &&
				(((Button)event.widget).getStyle() & SWT.RADIO) != 0
				&& event.type == SWT.Selection;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.buttonText = ((Button)event.widget).getText().replace("&", "");
		if (this.buttonText == null) {
			this.index = WidgetUtils.getIndex((Button)event.widget);
		}
	}

	@Override
	protected String getWidgetAccessor() {
		if (this.buttonText != null) {
			return "bot.radio(\"" + this.buttonText + "\")";
		} else {
			return "bot.radio(" + this.index + ")";
		}
	}

	@Override
	protected String getActon() {
		return ".click()";
	}

}
