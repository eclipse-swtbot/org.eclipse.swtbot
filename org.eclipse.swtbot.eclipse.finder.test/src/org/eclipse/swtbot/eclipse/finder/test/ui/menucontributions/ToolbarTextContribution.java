/*******************************************************************************
 * Copyright (c) 2013 Marcel Hoetter
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Marcel Hoetter - initial implementation
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.test.ui.menucontributions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

/**
 * A control contribution to the main toolbar.
 *  
 * @author Marcel Hoetter &lt;Marcel.Hoetter [at] genuinesoftware [dot] de&gt;
 */
public class ToolbarTextContribution extends
		WorkbenchWindowControlContribution {

	public static final String ID = "toolbar.text.contribution";
	public static final String TEXT = "Text contribution";
	public static final String TOOLTIP = "Text contribution tooltip";

	public ToolbarTextContribution() {
	}

	public ToolbarTextContribution(String id) {
		super(id);
	}

	@Override
	protected Control createControl(Composite parent) {
		Text txt = new Text(parent, SWT.BORDER);
		txt.setData(SWTBotPreferences.DEFAULT_KEY, ID);
		txt.setText(TEXT);
		txt.setToolTipText(TOOLTIP);
		return txt;
	}
}