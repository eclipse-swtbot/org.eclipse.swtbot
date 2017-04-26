/*******************************************************************************
 * Copyright (c) 2015 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Patrick Tasse - Initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.waits;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

/**
 * Condition that waits for a shell's menu bar.
 *
 * @see Conditions
 * @author Patrick Tasse
 * @version $Id$
 * @since 2.4
 */
public class WaitForMenuBar extends WaitForObjectCondition<Menu> {

	private final SWTBotShell	shell;

	/**
	 * Constructor.
	 *
	 * @param shell the shell.
	 */
	public WaitForMenuBar(SWTBotShell shell) {
		super(widgetOfType(Menu.class));
		this.shell = shell;
	}

	public String getFailureMessage() {
		return "Could not find menu bar for shell: " + shell; //$NON-NLS-1$
	}

	@Override
	protected List<Menu> findMatches() {
		Menu menuBar = UIThreadRunnable.syncExec(new WidgetResult<Menu>() {
			public Menu run() {
				return shell.widget.getMenuBar();
			}
		});
		if (menuBar != null) {
			return Collections.singletonList(menuBar);
		}
		return Collections.<Menu>emptyList();
	}
}
