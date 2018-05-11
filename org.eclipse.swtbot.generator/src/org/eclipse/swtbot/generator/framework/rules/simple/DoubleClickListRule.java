/*******************************************************************************
 * Copyright (c) 2018 Cadence Design Systems, Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Aparna Argade - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class DoubleClickListRule extends GenerationSimpleRule {

	private int widgetIndex;
	private int selectionIndex;
	boolean useIndex = false;
	private org.eclipse.swt.widgets.List list;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof org.eclipse.swt.widgets.List && event.type == SWT.MouseDoubleClick
				&& ((org.eclipse.swt.widgets.List) event.widget).getSelectionCount() == 1;
	}

	@Override
	public void initializeForEvent(Event event) {
		list = (org.eclipse.swt.widgets.List) event.widget;
		widgetIndex = WidgetUtils.getIndex(list);
		selectionIndex = list.getSelectionIndex();
		String[] selectedItems = list.getSelection();
		for (String selectedItem : selectedItems) {
			int nbOccurrences = 0;
			for (String item : list.getItems()) {
				if (item.equals(selectedItem)) {
					nbOccurrences++;
				}
			}
			if (nbOccurrences > 1) {
				useIndex = true;
			}
		}
	}

	public String getWidgetAccessor() {
		StringBuilder res = new StringBuilder();
		res.append("bot.list(");
		if (widgetIndex != 0) {
			res.append(widgetIndex);
		}
		res.append(")");
		return res.toString();
	}

	@Override
	public Widget getWidget() {
		return list;
	}

	@Override
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();
		StringBuilder code = new StringBuilder();
		code.append(getWidgetAccessor());
		code.append(".doubleClick(");
		if (useIndex) {
			code.append(Integer.toString(selectionIndex));
		} else {
			code.append("\"" + list.getItem(selectionIndex) + "\"");
		}
		code.append(")");
		actions.add(code.toString());
		return actions;
	}

	@Override
	public List<String> getImports() {
		return null;
	}

}
