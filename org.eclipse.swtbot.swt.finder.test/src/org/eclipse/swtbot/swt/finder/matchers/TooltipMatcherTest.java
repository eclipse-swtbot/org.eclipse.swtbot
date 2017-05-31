/*******************************************************************************
 * Copyright (c) 2016, 2017 Stephane Bouchet (Intel Corporation) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephane Bouchet (Intel Corporation) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.matchers;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withTooltip;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withTooltipIgnoringCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.widgets.Widget;
import org.hamcrest.Matcher;
import org.junit.Test;

public class TooltipMatcherTest {

	@Test
	public void doesNotMatchObjectsWithNoGetTooltipMethod() throws Exception {
		Matcher<Widget> matcher = withTooltip("Some Tooltip");
		assertFalse(matcher.matches(new Object()));
	}

	@Test
	public void doesNotMatchObjectsWithNullGetTooltip() throws Exception {
		Matcher<Widget> matcher = withTooltip("Some Tooltip");
		assertFalse(matcher.matches(new ObjectWithGetTooltip(null)));
	}

	@Test
	public void doesNotMatchTooltip() throws Exception {
		Matcher<Widget> matcher = withTooltip("Some Tooltip");
		assertFalse(matcher.matches(new Object()));
	}

	@Test
	public void matchTooltip() throws Exception {
		Matcher<Widget> matcher = withTooltip("Some Tooltip");
		assertTrue(matcher.matches(new ObjectWithGetTooltip("Some Tooltip")));
	}

	@Test
	public void matchTooltipIgnoreCase() throws Exception {
		Matcher<Widget> matcher = withTooltipIgnoringCase("Some Tooltip");
		assertTrue(matcher.matches(new ObjectWithGetTooltip("some tooltip")));
	}

	@Test
	public void getsToString() throws Exception {
		Matcher<Widget> matcher = withTooltip("Some Tooltip");
		assertEquals("with tooltip 'Some Tooltip'", matcher.toString());
	}

}
