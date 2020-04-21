/*******************************************************************************
 * Copyright (c) 2011, 2017 Ketan Padegaonkar and others.
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
package org.eclipse.swtbot.swt.finder.matchers;

import org.eclipse.swt.widgets.Widget;
import org.hamcrest.Description;

final class MyMatcher extends AbstractMatcher<Widget> {

	private final boolean	toAnswer;
	boolean					matched;

	public MyMatcher(boolean toAnswer) {
		this.toAnswer = toAnswer;
	}

	@Override
	public void describeTo(Description description) {

	}

	@Override
	protected boolean doMatch(Object item) {
		this.matched = true;
		return toAnswer;
	}

}
