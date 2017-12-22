/*******************************************************************************
 * Copyright (c) 2017 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Aparna Argade - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

public class CheckTableItemRule extends AbstractTableGenerationRule {

	@Override
	public boolean appliesTo(Event e) {
		return super.appliesTo(e) && e.type == SWT.Selection && e.detail == SWT.CHECK;
	}

	@Override
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();
		StringBuilder code = new StringBuilder();
		code.append(getWidgetAccessor());
		code.append(".toggleCheck()");
		actions.add(code.toString());
		return actions;

	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

}
