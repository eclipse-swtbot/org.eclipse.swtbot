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

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withMnemonic;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.widgets.Widget;
import org.hamcrest.Matcher;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class MenuMnemonicTextMatcherTest {

	@Test
	public void matchesMenuWithMnemonic() throws Exception {
		Matcher<Widget> matcher = withMnemonic("New Mnemonic");
		assertTrue(matcher.matches(new ObjectWithGetText("New &Mnemonic\tCTRL_M")));
	}

	@Test
	public void matchesMenuWithoutMnemonic() throws Exception {
		Matcher<Widget> matcher = withMnemonic("New Mnemonic");
		assertTrue(matcher.matches(new ObjectWithGetText("New Mnemonic\tCTRL_M")));
	}

	@Test
	public void matchesMenuWithoutAccesor() throws Exception {
		Matcher<Widget> matcher = withMnemonic("New Mnemonic");
		assertTrue(matcher.matches(new ObjectWithGetText("New Mnemonic")));
	}

	@Test
	public void matchesMnemonic() throws Exception {
		Object object = new ObjectWithGetText("&New Mnemonic");
		assertTrue(withMnemonic("New Mnemonic").matches(object));
	}

	@Test
	public void matchesWithoutMnemonic() throws Exception {
		Object object = new ObjectWithGetText("New Mnemonic");
		assertTrue(withMnemonic("New Mnemonic").matches(object));
	}

}
