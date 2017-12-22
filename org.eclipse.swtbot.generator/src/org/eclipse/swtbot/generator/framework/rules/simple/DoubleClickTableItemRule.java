/*******************************************************************************
 * Copyright (c) 2018 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Aparna Argade - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class DoubleClickTableItemRule extends GenerationSimpleRule {

	private int index;
	private Table table;
	private TableItem currentItem;
	boolean useIndex = false;

	/**
	 * MouseDoubleClick event does not set event.item, so only event.widget should
	 * be checked. Also, single selection is necessary before MouseDoubleClick.
	 */
	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof Table && event.type == SWT.MouseDoubleClick
				&& ((Table) event.widget).getSelectionCount() == 1;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.table = (Table) event.widget;
		this.currentItem = (TableItem) event.item;
		this.index = WidgetUtils.getIndex(this.table);
		currentItem = table.getSelection()[0];
		int nbOccurrences = 0;
		TableItem[] tableitems = this.table.getItems();
		for (TableItem item : tableitems) {
			if (item.getText().equals(currentItem.getText())) {
				nbOccurrences++;
				if (nbOccurrences > 1) {
					this.useIndex = true;
					break;
				}
			}
		}
	}

	public String getWidgetAccessor() {
		StringBuilder res = new StringBuilder();
		res.append("bot.table(");
		if (index != 0) {
			res.append(index);
		}
		res.append(")");
		res.append(".getTableItem(");
		if (this.useIndex) {
			res.append(table.indexOf(currentItem));
		} else {
			res.append("\"" + currentItem.getText() + "\"");
		}
		res.append(")");
		return res.toString();
	}

	@Override
	public Table getWidget() {
		return this.table;
	}

	@Override
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();
		StringBuilder code = new StringBuilder();
		code.append(getWidgetAccessor());
		code.append(".doubleClick()");
		actions.add(code.toString());
		return actions;

	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

}
