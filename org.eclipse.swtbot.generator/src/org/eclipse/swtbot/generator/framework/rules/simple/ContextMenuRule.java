/*******************************************************************************
 * Copyright (c) 2014 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Rastislav Wagner (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class ContextMenuRule extends GenerationSimpleRule{

	private List<String> path;
	private String menu;
	private MenuItem item;
	private String widgetAccessor;

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
		return event.type == SWT.Selection && menu && (style & SWT.POP_UP)!=0;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.item = (MenuItem) event.widget;
		menu = WidgetUtils.cleanText(this.item.getText());
		path = new ArrayList<String>();
		MenuItem currentItem = this.item;
		Menu parent = null;
		while (currentItem != null && (parent = currentItem.getParent()) != null) {
			currentItem = parent.getParentItem();
			if (currentItem != null && currentItem.getText() != null) {
				path.add(WidgetUtils.cleanText(currentItem.getText()));
			}
		}
		Collections.reverse(path);
		this.widgetAccessor = WidgetUtils.widgetLocator(this.item);
	}

	@Override
	public List<String> getActions() {
		StringBuilder res = new StringBuilder();
		res.append(this.widgetAccessor);
		res.append(".click()");
		return Arrays.asList( new String[] { res.toString() });
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem getWidget() {
		return this.item;
	}
	
	// Methods used by ToolBarMenuComplexRule
	public List<String> getPath() {
		return path;
	}

	public void setPath(List<String> path) {
		this.path = path;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

}
