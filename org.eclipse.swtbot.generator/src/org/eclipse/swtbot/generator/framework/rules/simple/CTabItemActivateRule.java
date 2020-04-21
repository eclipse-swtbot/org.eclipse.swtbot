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
 *    Mickael Istria (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;

public class CTabItemActivateRule extends GenerationSimpleRule {

	private String newSelection;
	private CTabItem cTabItem;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof CTabFolder && event.type == SWT.Selection;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.cTabItem = (CTabItem) event.item;
		this.newSelection = this.cTabItem.getText();
	}

	@Override
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();
		StringBuilder res = new StringBuilder();

		res.append("bot.cTabItem(\""); //$NON-NLS-1$
		res.append(this.newSelection);
		res.append("\").activate()"); //$NON-NLS-1$

		actions.add(res.toString());
		return actions;
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CTabItem getWidget() {
		return this.cTabItem;
	}

}