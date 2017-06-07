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
 * @since 2.3.0
 */
public class WithPerspectiveId extends AbstractMatcher<MPerspective> {

	private final Matcher<String>	idMatcher;

	/**
	 * @param idMatcher the perspective id matcher.
	 */
	WithPerspectiveId(final Matcher<String> idMatcher) {
		this.idMatcher = idMatcher;
	}

	@Override
	public boolean doMatch(final Object item) {
		if (item instanceof MPerspective) {
			final MPerspective perspective = (MPerspective) item;
			return idMatcher.matches(perspective.getElementId());
		}
		return false;
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("with id '").appendDescriptionOf(idMatcher).appendText("'");
	}

	/**
	 * Matches a perspective with the specified id.
	 *
	 * @param id the id of the perspective.
	 * @return a matcher.
	 * @since 2.3.0
	 */
	@Factory
	public static WithPerspectiveId withPerspectiveId(final String id) {
		return withPerspectiveId(equalTo(id));
	}

	/**
	 * Matches a perspective with the specified id.
	 *
	 * @param idMatcher the matcher that matches the id of the perspective.
	 * @return a matcher.
	 * @since 2.3.0
	 */
	@Factory
	public static WithPerspectiveId withPerspectiveId(final Matcher<String> idMatcher) {
		return new WithPerspectiveId(idMatcher);
	}
}

