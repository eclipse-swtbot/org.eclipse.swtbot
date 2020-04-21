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
package org.eclipse.swtbot.generator.rules.workbench;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.ui.IViewReference;

public class ShowViewSimpleRule extends GenerationSimpleRule {

	private IViewReference view;
	private String widgetAccessor;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget == null && event.type == SWT.Selection && event.data instanceof IViewReference && event.detail == 1; 
	}

	@Override
	public void initializeForEvent(Event event) {
		this.view = (IViewReference) event.data;
		this.widgetAccessor = WorkbenchItemsUtil.getWidgetAccessor(this.view);
	}

	@Override
	public Widget getWidget() {
		return null;
	}

	@Override
	public List<String> getActions() {
		return Arrays.asList(new String[] { this.widgetAccessor + ".show()" });
	}

	@Override
	public List<String> getImports() {
		return null;
	}

}
