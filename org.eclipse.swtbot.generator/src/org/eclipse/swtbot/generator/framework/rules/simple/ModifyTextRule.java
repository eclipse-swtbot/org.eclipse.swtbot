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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class ModifyTextRule extends GenerationSimpleRule {

	private Text text;
	private String newValue;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof Text && event.type == SWT.Modify;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.text = (Text)event.widget;
		this.newValue = this.text.getText();
	}

	@Override
	public String getWidgetAccessor() {
		StringBuilder res = new StringBuilder();
		res.append("bot.text(");
		int index = WidgetUtils.getIndex(this.text);
		if (index != 0) {
			res.append(index);
		}
		res.append(")");
		return res.toString();
	}

	@Override
	public String getAction() {
		return ".setText(\"" + this.newValue + "\")";
	}

}