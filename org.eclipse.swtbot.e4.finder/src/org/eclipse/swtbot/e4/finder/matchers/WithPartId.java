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
 * @author Ralf Ebert www.ralfebert.de (bug 271630)
 * @author Matt biggs - Converted to E4
 * @version $Id$
 * @since 2.3.0
 */
public class WithPartId<T extends MPart> extends AbstractMatcher<T> {

	private final Matcher<String>	idMatcher;

	WithPartId(final Matcher<String> idMatcher) {
		this.idMatcher = idMatcher;
	}

	@Override
	public boolean doMatch(final Object item) {
		if (item instanceof MPart) {
			final MPart part = (MPart) item;
			return idMatcher.matches(part.getElementId());
		}
		return false;
	}

	public void describeTo(final Description description) {
		description.appendText("with id '").appendDescriptionOf(idMatcher).appendText("'");
	}

	/**
	 * Matches a workbench part (view/editor) with the specified id.
	 *
	 * @param id the id of the part.
	 * @return a matcher.
	 * @since 2.3.0
	 */
	@Factory
	public static <T extends MPart> Matcher<T> withPartId(final String id) {
		return withPartId(equalTo(id));
	}

	/**
	 * Matches a workbench part (view/editor) with the specified id.
	 *
	 * @param idMatcher the part id matcher.
	 * @return a matcher.
	 * @since 2.3.0
	 */
	@Factory
	public static <T extends MPart> Matcher<T> withPartId(final Matcher<String> idMatcher) {
		return new WithPartId<T>(idMatcher);
	}
}
