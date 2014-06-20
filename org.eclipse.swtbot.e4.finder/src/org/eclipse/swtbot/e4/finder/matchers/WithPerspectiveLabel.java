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

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.swtbot.swt.finder.matchers.AbstractMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

/**
 * @author Ralf Ebert www.ralfebert.de (bug 271630)
 * @author Matt biggs - Converted to E4
 * @version $Id$
 * @since 2.2.2
 */
public class WithPerspectiveLabel extends AbstractMatcher<MPerspective> {

	private final Matcher<String>	labelMatcher;

	/**
	 * @param labelMatcher the perspective label matcher.
	 */
	WithPerspectiveLabel(final Matcher<String> labelMatcher) {
		this.labelMatcher = labelMatcher;
	}

	@Override
	public boolean doMatch(final Object item) {
		if (item instanceof MPerspective) {
			final MPerspective perspective = (MPerspective) item;
			return labelMatcher.matches(perspective.getLabel());
		}
		return false;
	}

	public void describeTo(final Description description) {
		description.appendText("with label '").appendDescriptionOf(labelMatcher).appendText("'");
	}

	/**
	 * Matches a perspective with the specified label.
	 *
	 * @param label the label of the perspective.
	 * @return a matcher.
	 * @since 2.2.2
	 */
	@Factory
	public static WithPerspectiveLabel withPerspectiveLabel(final String label) {
		return withPerspectiveLabel(equalTo(label));
	}

	/**
	 * Matches a perspective with the specified label.
	 *
	 * @param labelMatcher the matcher that matches the perspective label.
	 * @return a matcher.
	 * @since 2.2.2
	 */
	@Factory
	public static WithPerspectiveLabel withPerspectiveLabel(final Matcher<String> labelMatcher) {
		return new WithPerspectiveLabel(labelMatcher);
	}
}

