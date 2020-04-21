/*******************************************************************************
 * Copyright (c) 2012, 2017 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *    Aparna Argade - Bug 527571
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class ModifyTextRule extends GenerationSimpleRule {

	private String newValue;
	private Text text;
	private String locator;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof Text && event.type == SWT.Modify &&
				!((Text)event.widget).getText().isEmpty() &&
				!((Text)event.widget).getText().equals(((Text)event.widget).getMessage()) &&
				!(((Text)event.widget).getParent() instanceof CCombo);
	}

	@Override
	public void initializeForEvent(Event event) {
		this.text = (Text) event.widget;
		this.locator = WidgetUtils.widgetLocator(this.text);
		this.newValue = this.text.getText();
	}

	@Override
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();
		StringBuilder res = new StringBuilder(this.locator);
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
	public Widget getWidget() {
		return this.text;
	}

}