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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.generator.framework.GenerationRule;

public class MenuClickedRule extends GenerationRule {

	private MenuItem item;

	@Override
	public boolean appliesTo(Event event) {
		return event.type == SWT.Selection && event.widget instanceof MenuItem;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.item = (MenuItem)event.widget;
	}

	@Override
	protected String getWidgetAccessor() {
		StringBuilder code = new StringBuilder();
		List<String> path = new ArrayList<String>();
		path.add(cleanMenuText(this.item.getText()));
		MenuItem currentItem = this.item;
		Menu parent = null;
		while (currentItem != null && (parent = currentItem.getParent()) != null) {
			currentItem = parent.getParentItem();
			if (currentItem != null && currentItem.getText() != null) {
				path.add(cleanMenuText(currentItem.getText()));
			}
		}
		Collections.reverse(path);
		code.append("bot");
		for (String text : path) {
			code.append(".menu(\"");
			code.append(text);
			code.append("\")");
		}
		return code.toString();
	}

	private static String cleanMenuText(String text) {
		return text.replace("&", "").split("\t")[0];
	}

	@Override
	protected String getActon() {
		return ".click()";
	}

}
