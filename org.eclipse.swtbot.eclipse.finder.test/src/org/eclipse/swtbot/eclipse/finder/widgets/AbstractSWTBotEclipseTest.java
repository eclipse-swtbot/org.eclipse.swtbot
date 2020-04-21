/*******************************************************************************
 * Copyright (c) 2010, 2017 Ketan Padegaonkar and others.
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
package org.eclipse.swtbot.eclipse.finder.widgets;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.junit.After;
import org.junit.BeforeClass;

public abstract class AbstractSWTBotEclipseTest {

	protected static SWTWorkbenchBot	bot	= new SWTWorkbenchBot();

	@BeforeClass
	public static void beforeClass() {
		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		closeWelcomePage();
	}

	private static void closeWelcomePage() {
		for (SWTBotView view : bot.views()) {
			if (view.getTitle().equals("Welcome")) {
				view.close();
			}
		}
	}

	@After
	public void tearDown() throws Exception {
		closeWelcomePage();
		bot.resetWorkbench();
	}
}