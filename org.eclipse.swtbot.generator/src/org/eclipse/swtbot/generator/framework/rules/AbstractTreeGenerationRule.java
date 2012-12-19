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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swtbot.generator.framework.GenerationRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public abstract class AbstractTreeGenerationRule extends GenerationRule {

	private Tree tree;
	private TreeItem item;

	/**
	 * Subclasses should call super.appliesTo first, and then
	 * verify their conditions
	 * @param event
	 * @return
	 */
	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof Tree && event.item instanceof TreeItem;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.tree = (Tree)event.widget;
		this.item = (TreeItem)event.item;
	}

	@Override
	protected String getWidgetAccessor() {
		StringBuilder res = new StringBuilder();
		res.append("bot.tree(");
		int index = WidgetUtils.getIndex(this.tree);
		if (index != 0) {
			res.append(index);
		}
		res.append(")");
		List<String> path = new ArrayList<String>();
		TreeItem currentItem = this.item;
		while (currentItem != null) {
			if (currentItem != null && currentItem.getText() != null) {
				path.add(currentItem.getText());
			}
			currentItem = currentItem.getParentItem();
		}
		Collections.reverse(path);
		boolean first = true;
		for (String text : path) {
			if (first) {
				res.append(".getTreeItem(\"");
				first = false;
			} else {
				res.append(".getNode(\"");
			}
			res.append(text);
			res.append("\")");
		}
		return res.toString();
	}
}
