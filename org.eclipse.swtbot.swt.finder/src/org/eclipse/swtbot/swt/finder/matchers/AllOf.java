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
 *     Ketan Padegaonkar - http://swtbot.org/bugzilla/show_bug.cgi?id=126
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.matchers;

import java.util.Arrays;

import org.eclipse.swt.widgets.Widget;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * A matcher that evaluates to <code>true</code> if and only if all the matchers evaluate to <code>true</code>.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class AllOf<T> extends AbstractMatcher<T> {
	private final Iterable<Matcher<? extends T>>	matchers;

	AllOf(Iterable<Matcher<? extends T>> matchers) {
		this.matchers = matchers;
	}

	@Override
	protected boolean doMatch(Object o) {
		for (Matcher<? extends T> matcher : matchers) {
			if (!matcher.matches(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void describeTo(Description description) {
		description.appendList("(", " and ", ")", matchers); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Evaluates to true only if ALL of the passed in matchers evaluate to true.
	 * 
	 * @return a matcher.
	 */
	public static <T extends Widget> Matcher<T> allOf(Matcher<? extends T>... matchers) {
		return new AllOf<T>(Arrays.asList(matchers));
	}
	
	/**
	 * Evaluates to true only if ALL of the passed in matchers evaluate to true.
	 * 
	 * @return a matcher.
	 */
	public static <T extends Widget> Matcher<T> allOf(Iterable<Matcher<? extends T>> matchers) {
		return new AllOf<T>(matchers);
	}

}
