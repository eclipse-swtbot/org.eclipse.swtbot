/*******************************************************************************
 * Copyright (c) 2018 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Aparna Argade - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules.simple;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public abstract class AbstractTableGenerationRule extends GenerationSimpleRule {

	private int index;
	private Table table;
	private TableItem currentItem;
	boolean useIndex = false;

	/**
	 * Subclasses should call super.appliesTo first, and then
	 * verify their conditions
	 * @param event
	 * @return
	 */
	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof Table && event.item instanceof TableItem;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.table = (Table)event.widget;
		index = WidgetUtils.getIndex(table);
		this.currentItem = (TableItem) event.item;
		int nbOccurrences = 0;
		for (TableItem item : this.table.getItems()) {
			if (item.getText().equals(((TableItem) event.item).getText())) {
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

}