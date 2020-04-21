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

import java.util.List;

import org.eclipse.swt.widgets.Widget;
import org.hamcrest.Matcher;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @see Conditions
 * @version $Id$
 * @since 2.0
 */
class WaitForWidget<T extends Widget> extends WaitForObjectCondition<T> {

	WaitForWidget(Matcher<T> matcher) {
		super(matcher);
	}

	@Override
	public String getFailureMessage() {
		return "Could not find widget matching: " + matcher; //$NON-NLS-1$
	}

	@Override
	protected List<T> findMatches() {
		return bot.getFinder().findControls(matcher);
	}

}
