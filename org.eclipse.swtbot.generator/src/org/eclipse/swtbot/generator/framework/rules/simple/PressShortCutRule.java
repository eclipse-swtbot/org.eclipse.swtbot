/*******************************************************************************
 * Copyright (c) 2014 Red Hat Inc..
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
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class PressShortCutRule extends GenerationSimpleRule {

	String widgetLocator;
	Widget widget;
	private char keyChar;
	private int modifier;

	@Override
	public boolean appliesTo(Event event) {
		return event.type == SWT.KeyDown && event.stateMask != 0 && !(event.stateMask == SWT.SHIFT && Character.isUpperCase(event.character));
	}

	@Override
	public void initializeForEvent(Event event) {
		this.widget = event.widget;
		this.keyChar = event.character;
		this.modifier = event.stateMask;
		this.widgetLocator = WidgetUtils.widgetLocator(this.widget);
	}

	@Override
	public List<String> getActions() {
		List<String> res = new ArrayList<String>();
		StringBuilder line = new StringBuilder();
		line.append(this.widgetLocator);
		line.append(".pressShortcut("); //$NON-NLS-1$
		line.append(computeString(this.modifier));
		if (this.keyChar != '\0') {
			line.append(", "); //$NON-NLS-1$
			line.append('\'');
			line.append(this.keyChar);
			line.append('\'');
		}
		line.append(")"); //$NON-NLS-1$
		res.add(line.toString());
		return res;
	}

	private static String computeString(int modifier) {
		if (modifier == 0) {
			return "SWT.NONE"; //$NON-NLS-1$
		}
		StringBuilder res = new StringBuilder();
		if ((modifier & SWT.CTRL) != 0) {
			res.append("SWT.CTRL"); //$NON-NLS-1$
		}
		if ((modifier & SWT.ALT) != 0) {
			if (res.length() > 0) {
				res.append(" | "); //$NON-NLS-1$
			}
			res.append("SWT.ALT"); //$NON-NLS-1$
		}
		if ((modifier & SWT.SHIFT) != 0) {
			if (res.length() > 0) {
				res.append(" | "); //$NON-NLS-1$
			}
			res.append("SWT.SHIFT"); //$NON-NLS-1$
		}
		return res.toString();
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Widget getWidget() {
		return this.widget;
	}

}