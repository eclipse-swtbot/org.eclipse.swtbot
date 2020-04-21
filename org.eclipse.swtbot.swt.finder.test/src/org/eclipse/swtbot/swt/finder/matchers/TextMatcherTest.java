/*******************************************************************************
 * Copyright (c) 2008, 2017 Ketan Padegaonkar and others.
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

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withText;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withTextIgnoringCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.widgets.Widget;
import org.hamcrest.Matcher;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class TextMatcherTest {

	@Test
	public void doesNotMatchObjectsWithNoGetTextMethod() throws Exception {
		Matcher<Widget> matcher = withText("Some Text");
		assertFalse(matcher.matches(new Object()));
	}

	@Test
	public void doesNotMatchObjectsWithNullGetText() throws Exception {
		Matcher<Widget> matcher = withText("Some Text");
		assertFalse(matcher.matches(new ObjectWithGetText(null)));
	}

	@Test
	public void doesNotMatchText() throws Exception {
		Matcher<Widget> matcher = withText("Some Text");
		assertFalse(matcher.matches(new Object()));
	}

	@Test
	public void matchText() throws Exception {
		Matcher<Widget> matcher = withText("Some Text");
		assertTrue(matcher.matches(new ObjectWithGetText("Some Text")));
	}

	@Test
	public void matchTextIgnoreCase() throws Exception {
		Matcher<Widget> matcher = withTextIgnoringCase("Some Text");
		assertTrue(matcher.matches(new ObjectWithGetText("some text")));
	}

	@Test
	public void getsToString() throws Exception {
		Matcher<Widget> matcher = withText("Some Text");
		assertEquals("with text 'Some Text'", matcher.toString());
	}

}
