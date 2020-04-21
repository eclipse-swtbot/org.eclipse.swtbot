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
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;

public class ToolBarItemClickedRule extends GenerationSimpleRule{

	private String toolTipText;
	private String text;
	private ToolItem widget;

	@Override
	public boolean appliesTo(Event event) {
		if (event.widget instanceof ToolItem && event.type == SWT.Selection) {
			ToolItem item = (ToolItem)event.widget;
			int style = item.getStyle();
			return (style & SWT.DROP_DOWN) == 0 && (style & SWT.PUSH) != 0;
		}
		return false;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.widget = (ToolItem)event.widget;
		this.text = this.widget.getText();
		this.toolTipText = this.widget.getToolTipText();
	}

	@Override
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();
		StringBuilder res = new StringBuilder("bot.");
		if (this.text != null && this.text.length() > 0) {
			res.append("toolbarButton(\""); //$NON-NLS-1$
			res.append(this.text);
			res.append("\")"); //$NON-NLS-1$
		} else if (this.toolTipText != null && this.toolTipText.length() > 0) {
			res.append("toolbarButtonWithTooltip(\""); //$NON-NLS-1$
			res.append(this.toolTipText);
			res.append("\")"); //$NON-NLS-1$
		} else {
			res.append("toolbarButton("); //$NON-NLS-1$
			res.append("TODO index");
			res.append(')');
		}
		res.append(".click()"); //$NON-NLS-1%
		actions.add(res.toString());
		return actions;
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ToolItem getWidget() {
		return this.widget;
	}

}

