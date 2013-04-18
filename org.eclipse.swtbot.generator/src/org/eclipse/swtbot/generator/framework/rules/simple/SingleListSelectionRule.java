/*******************************************************************************
 * Copyright (c) 2013 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *    Vlado Pakan (Red Hat)
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules.simple;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class SingleListSelectionRule extends GenerationSimpleRule {

	private List list;
	private String newSelectionText = null;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof List && event.type == SWT.Selection &&
			(((List)event.widget).getStyle() & SWT.SINGLE) != 0;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.list = (List)event.widget;
		if (this.list.getSelectionCount() > 0){
			this.newSelectionText = this.list.getSelection()[0];
		}
	}

	@Override
	public String getWidgetAccessor() {
		int index = WidgetUtils.getIndex(this.list);
		if (index != 0) {
			return "bot.list(" + index + ")";
		} else {
			return "bot.list()";
		}
	}

	@Override
	public String getAction() {
		StringBuilder res = new StringBuilder();
		if (this.newSelectionText != null){
			res.append(".select(\"");
			res.append(this.newSelectionText);
			res.append("\")");
		}
		else {
			res.append(".unselect()");
		}
		return res.toString();
	}

}
