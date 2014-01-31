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

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class SingleListSelectionRule extends GenerationSimpleRule {

	private String newSelectionText = null;
	private int index;
	private List widget;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof List && event.type == SWT.Selection &&
			(((List)event.widget).getStyle() & SWT.SINGLE) != 0;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.widget = (List)event.widget;
		if (this.widget.getSelectionCount() > 0){
			this.newSelectionText = this.widget.getSelection()[0];
		}
		this.index = WidgetUtils.getIndex(this.widget);
	}

	@Override
	public java.util.List<String> getActions() {
		java.util.List<String> actions = new ArrayList<String>();

		StringBuilder res = new StringBuilder();
		if (index != 0) {
			res.append("bot.list(" + index + ")");
		} else {
			res.append("bot.list()");
		}

		if (this.newSelectionText != null){
			res.append(".select(\"");
			res.append(this.newSelectionText);
			res.append("\")");
		}
		else {
			res.append(".unselect()");
		}

		actions.add(res.toString());

		return actions;
	}

	@Override
	public java.util.List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getWidget() {
		return this.widget;
	}

}
