/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/

package org.eclipse.swtbot.swt.finder.waits;

import org.eclipse.swtbot.swt.finder.SWTBot;

/**
 * This is an abstract implementation of the condition interface to simplify the implementing classes.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public abstract class DefaultCondition implements ICondition {

	/** the SWTBot instance that this instance may use to evaluate the test. */
	protected SWTBot	bot;

	/**
	 * Initializes the condition with the given {@link SWTBot}.
	 * 
	 * @see org.eclipse.swtbot.swt.finder.waits.ICondition#init(org.eclipse.swtbot.swt.finder.SWTBot)
	 * @param bot The bot to use. This should never be <code>null</code>.
	 */
	@Override
	public void init(SWTBot bot) {
		this.bot = bot;
	}

}
