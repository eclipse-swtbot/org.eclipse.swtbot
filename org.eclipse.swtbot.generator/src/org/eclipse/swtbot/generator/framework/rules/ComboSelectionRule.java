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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.generator.framework.GenerationRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class ComboSelectionRule extends GenerationRule {

	private CCombo combo;
	private String newSelection;
	private int newSelectionIndex;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof CCombo && event.type == SWT.Selection;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.combo = (CCombo)event.widget;
		this.newSelection = this.combo.getText();
		this.newSelectionIndex = this.combo.getSelectionIndex();
	}

	@Override
	protected String getWidgetAccessor() {
		int index = WidgetUtils.getIndex(this.combo);
		if (index != 0) {
			return "bot.combo(" + index + ")";
		} else {
			return "bot.combo()";
		}
	}

	@Override
	protected String getActon() {
		StringBuilder res = new StringBuilder();
		res.append(".select(");
		if (this.newSelection != null) {
			res.append('"');
			res.append(this.newSelection);
			res.append("\")");
		} else {
			res.append(this.newSelectionIndex);
			res.append(")");
		}
		return res.toString();
	}

}
