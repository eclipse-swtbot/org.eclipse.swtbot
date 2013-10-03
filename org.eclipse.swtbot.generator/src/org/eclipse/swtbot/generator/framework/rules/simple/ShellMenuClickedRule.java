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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class ShellMenuClickedRule extends GenerationSimpleRule {

	private List<String> path;

	@Override
	public boolean appliesTo(Event event) {
		boolean menu = event.widget instanceof MenuItem;
		int style = 0;
		if(menu){
			MenuItem currentItem = ((MenuItem)event.widget);
			Menu parent = null;
			while (currentItem != null && (parent = currentItem.getParent()) != null) {
				style = parent.getStyle();
				currentItem = parent.getParentItem();
			}
		}
		return event.type == SWT.Selection && menu && (style & SWT.BAR)!=0;
	}

	@Override
	public void initializeForEvent(Event event) {
		MenuItem item = (MenuItem)event.widget;
		path = new ArrayList<String>();
		path.add(item.getText());
		MenuItem currentItem = item;
		Menu parent = null;
		while (currentItem != null && (parent = currentItem.getParent()) != null) {
			currentItem = parent.getParentItem();
			if (currentItem != null && currentItem.getText() != null) {
				path.add(currentItem.getText());
			}
		}
		Collections.reverse(path);
	}

	@Override
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();
		
		StringBuilder code = new StringBuilder();
		code.append("bot");
		for (String text : path) {
			code.append(".menu(\"");
			code.append(WidgetUtils.cleanText(text));
			code.append("\")");
		}
		
		code.append(".click()");
		actions.add(code.toString());
		
		
		return actions;
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

}