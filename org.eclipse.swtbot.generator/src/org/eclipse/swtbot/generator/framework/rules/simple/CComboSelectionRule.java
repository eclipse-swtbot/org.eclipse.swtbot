/*******************************************************************************
 * Copyright (c) 2012, 2017 Red Hat Inc. and others.
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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class CComboSelectionRule extends GenerationSimpleRule {

	private String newSelection;
	private int newSelectionIndex;
	private int index;
	private CCombo combo;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof CCombo && event.type == SWT.Selection;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.combo = (CCombo) event.widget;
		this.newSelection = this.combo.getText();
		this.newSelectionIndex = this.combo.getSelectionIndex();
		index = WidgetUtils.getIndex(this.combo);
	}

	@Override
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();
		StringBuilder res = new StringBuilder();

		if (index != 0) {
			res.append("bot.ccomboBox(" + index + ")");
		} else {
			res.append("bot.ccomboBox()");
		}

		res.append(".setSelection(");
		if (this.newSelection != null) {
			res.append('"');
			res.append(this.newSelection);
			res.append("\")");
		} else {
			res.append(this.newSelectionIndex);
			res.append(")");
		}

		actions.add(res.toString());
		return actions;
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CCombo getWidget() {
		return this.combo;
	}

}