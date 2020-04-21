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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class ModifyStyledTextRule extends GenerationSimpleRule {

	private String newValue;
	private StyledText text;
	private String widgetLocator;

	@Override
	public boolean appliesTo(Event event) {
		if (event.widget instanceof StyledText && event.type == SWT.Modify) {
			StyledText text = (StyledText)event.widget;
			return !text.getText().isEmpty();
		}
		return false;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.text = (StyledText)event.widget;
		this.widgetLocator = WidgetUtils.widgetLocator(this.text);
		this.newValue = this.text.getText();
	}

	@Override
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();
		StringBuilder res = new StringBuilder(this.widgetLocator);
		res.append(".setText(\"" + this.newValue + "\")"); //$NON-NLS-1$ //$NON-NLS-2$
		actions.add(res.toString());
		return actions;
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StyledText getWidget() {
		return this.text;
	}

}