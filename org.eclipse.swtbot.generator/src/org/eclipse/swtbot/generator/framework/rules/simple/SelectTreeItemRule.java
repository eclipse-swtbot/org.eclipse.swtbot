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
package org.eclipse.swtbot.generator.framework.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.SWT;

public class SelectTreeItemRule extends AbstractTreeGenerationRule {

	@Override
	public boolean appliesTo(Event e) {
		return super.appliesTo(e) && e.type == SWT.Selection;
	}

	@Override
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();
		StringBuilder code = new StringBuilder();
		code.append(getWidgetAccessor());
		code.append(".select()");
		actions.add(code.toString());
		return actions;
		
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

}