/*******************************************************************************
 * Copyright (c) 2008 http://www.inria.fr/ and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     http://www.inria.fr/ - initial API and implementation
 *******************************************************************************/

package org.eclipse.swtbot.swt.finder.waits;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.utils.StringUtils;
import org.eclipse.swtbot.swt.finder.utils.internal.Assert;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

/**
 * A condition that waits until the specified shell is the active shell.
 *
 * @see Conditions
 * @since 1.3
 * @author Vincent MAHE &lt;vmahe [at] free[dot]fr&gt;
 * @version $Id$
 */
class ShellIsActive extends DefaultCondition {

	private String	text;

	ShellIsActive(String text) {
		Assert.isNotNull(text, "The shell text was null"); //$NON-NLS-1$
		Assert.isLegal(!StringUtils.isEmpty(text), "The shell text was empty"); //$NON-NLS-1$
		this.text = text;
	}

	@Override
	public String getFailureMessage() {
		return "The shell '" + text + "' did not activate"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public boolean test() throws Exception {
		try {
			final SWTBotShell shell = bot.shell(text);
			return UIThreadRunnable.syncExec(new BoolResult() {
				@Override
				public Boolean run() {
					return shell.widget.isVisible() || shell.widget.isFocusControl();
				}
			});
		} catch (WidgetNotFoundException e) {
		}
		return false;
	}

}
