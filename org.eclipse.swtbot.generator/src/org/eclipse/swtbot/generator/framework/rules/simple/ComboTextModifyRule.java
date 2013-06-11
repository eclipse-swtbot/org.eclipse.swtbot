/*******************************************************************************
 * Copyright (c) 2013 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules.simple;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class ComboTextModifyRule extends GenerationSimpleRule {

	private int textIndex;
	private String newValue;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof Combo && event.type == SWT.Modify &&
				!((Combo)event.widget).getText().isEmpty();
	}

	@Override
	public void initializeForEvent(Event event) {
		this.textIndex = WidgetUtils.getIndex((Combo)event.widget);
		this.newValue = ((Combo)event.widget).getText();
	}

	@Override
	public String getWidgetAccessor() {
		StringBuilder res = new StringBuilder();
		res.append("bot.comboBox(");
		if (textIndex != 0) {
			res.append(textIndex);
		}
		res.append(")");
		return res.toString();
	}

	@Override
	public String getAction() {
		return ".setText(\"" + this.newValue + "\")";
	}

	public int getTextIndex() {
		return textIndex;
	}

	public void setTextIndex(int textIndex) {
		this.textIndex = textIndex;
	}



}