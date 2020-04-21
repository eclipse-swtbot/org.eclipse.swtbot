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
 *     Patrick Tasse - Speed up SWTBot tests
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.finders;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withMnemonic;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.AbstractSWTBotEclipseTest;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotCommand;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the command finder.
 *
 * @author @author Stephen Paulin &lt;paulin [at] spextreme [dot] com&gt;
 * @version $Id$
 */
@SuppressWarnings("deprecation")
@RunWith(SWTBotJunit4ClassRunner.class)
public class CommandFinderTest extends AbstractSWTBotEclipseTest {

	private static SWTWorkbenchBot bot = new SWTWorkbenchBot();

	@Test
	public void findCommandIMatcher() throws Exception {
		CommandFinder finder = new CommandFinder();

		List<SWTBotCommand> l = finder.findCommand(equalTo("Contribution Item Command"));

		assertNotNull(l);
		assertEquals(1, l.size());

		SWTBotCommand command = l.get(0);
		assertEquals("Contribution Item Command", command.getText());

		command.click();
		
		bot.shell("Simple Dialog").activate();

		bot.button("OK").click();
	}

	@Test
	public void findCommandNoMatch() throws Exception {
		CommandFinder finder = new CommandFinder();

		List<SWTBotCommand> l = finder.findCommand(withMnemonic("BadMatchName"));

		assertNotNull(l);
		assertEquals(0, l.size());
	}
}
