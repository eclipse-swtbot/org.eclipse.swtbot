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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public abstract class AbstractTreeGenerationRule extends GenerationSimpleRule {

	private int index;
	private List<String> path;
	private Tree tree;

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
		path = new ArrayList<String>();
		index = WidgetUtils.getIndex(tree);
		TreeItem currentItem = (TreeItem)event.item;
		while (currentItem != null) {
			if (currentItem.getText() != null) {
				path.add(currentItem.getText());
			}
			currentItem = currentItem.getParentItem();
		}
		Collections.reverse(path);
	}

	public String getWidgetAccessor() {
		StringBuilder res = new StringBuilder();
		res.append("bot.tree(");
		if (index != 0) {
			res.append(index);
		}
		res.append(")");
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

	@Override
	public Tree getWidget() {
		return this.tree;
	}
}