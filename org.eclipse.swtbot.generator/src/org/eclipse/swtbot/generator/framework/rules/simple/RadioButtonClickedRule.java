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
package org.eclipse.swtbot.generator.framework.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class RadioButtonClickedRule extends GenerationSimpleRule {

	private String buttonText;
	private int index;
	private Button button;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof Button &&
				(((Button)event.widget).getStyle() & SWT.CHECK) != 0
				&& event.type == SWT.Selection;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.button = (Button)event.widget;
		this.buttonText = this.button.getText();
		if (this.buttonText != null) {
			this.buttonText = this.buttonText.replace("&", "");
		} else {
			this.index = WidgetUtils.getIndex((Button)event.widget);
		}
	}

	@Override
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();

		StringBuilder code = new StringBuilder();
		if (this.buttonText != null) {
			code.append("bot.checkBox(\"" + this.buttonText + "\")");
		} else {
			code.append("bot.checkBox(" + this.index + ")");
		}

		code.append(".click()");
		actions.add(code.toString());

		return actions;
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Button getWidget() {
		return this.button;
	}

}