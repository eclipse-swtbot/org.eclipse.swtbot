/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Rastislav Wagner (Red Hat) - initial API and implementation
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

public class ContextMenuRule extends GenerationSimpleRule{

	private List<String> path;
	private String menu;
	private MenuItem item;

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
	}

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

	@Override
	public List<String> getActions() {
		// TODO Auto-generated method stub
		return null;
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

}
