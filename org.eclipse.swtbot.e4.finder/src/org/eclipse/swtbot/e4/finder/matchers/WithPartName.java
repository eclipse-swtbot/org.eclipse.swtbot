/*******************************************************************************
 * Copyright (c) 2014 SWTBot Committers and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Matt Biggs - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.e4.finder.matchers;

import static org.hamcrest.Matchers.equalTo;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swtbot.swt.finder.matchers.AbstractMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @author Ketan Patel
 * @author Matt biggs - Converted to E4
 * @version $Id$
 * @since 2.3.0
 */
public class WithPartName<T extends MPart> extends AbstractMatcher<T> {

	private final Matcher<String> nameMatcher;

	/**
	 * @param nameMatcher the part name matcher.
	 */
	public WithPartName(final Matcher<String> nameMatcher) {
		this.nameMatcher = nameMatcher;
	}

	@Override
	public boolean doMatch(final Object item) {
		if (item instanceof MPart) {
			final MPart part = (MPart) item;
			return nameMatcher.matches(part.getLabel());
		}
		return false;
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("with name '").appendDescriptionOf(nameMatcher).appendText("'");
	}

	/**
	 * Matches a workbench part with the specified name.
	 *
	 * @param text the label of the part.
	 * @return a matcher.
	 * @since 2.3.0
	 */
	@Factory
	public static <T extends MPart> Matcher<T> withPartName(final String text) {
		return withPartName(equalTo(text));
	}

	/**
	 * Matches a workbench part with the specified name.
	 *
	 * @param nameMatcher the part name matcher.
	 * @return a matcher.
	 * @since 2.3.0
	 */
	@Factory
	public static <T extends MPart> Matcher<T> withPartName(final Matcher<String> nameMatcher) {
		return new WithPartName<T>(nameMatcher);
	}
}
