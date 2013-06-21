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
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;

public class ToolBarDropDownRule extends GenerationSimpleRule{
	
	private String toolTipText;
	

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof ToolItem && event.type == SWT.Selection &&
				(((ToolItem)event.widget).getStyle() & SWT.DROP_DOWN)!= 0;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.toolTipText = ((ToolItem)event.widget).getToolTipText();
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}

	@Override
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();
		actions.add("bot.toolbarDropDownButtonWithToolTip(\""+toolTipText+"\").click()");
		return actions;
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

}

