/*******************************************************************************
 * Copyright (c) 2008, 2017 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Paulin - http://swtbot.org/bugzilla/show_bug.cgi?id=36
 *     Aparna Argade - Bug 509723
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertText;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertTextContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotTextTest extends AbstractControlExampleTest {

	@Test
	public void findsTextBoxInGroup() throws Exception {
		try {
			bot.checkBox("Listen").click();
			SWTBotText text = bot.textInGroup("Text");
			assertTextContains("The quick brown fox", text.widget);
			text.setText("hello world");
			assertText("hello world", text.widget);
			assertTextContains("VerifyEvent", bot.textInGroup("Listeners").widget);
			assertTextContains("hello world", bot.textInGroup("Listeners").widget);
		} finally {
			bot.checkBox("Listen").click();
			bot.button("Clear").click();
		}
	}

	@Test
	public void findsTextBoxWithText() throws Exception {
		assertText("The quick brown fox jumps over the lazy dog.\n" + "One Two Three", bot
				.text("The quick brown fox jumps over the lazy dog.\r\n" + "One Two Three").widget);
	}

	@Test
	public void typesText() throws Exception {
		final SWTBotText text = bot.textInGroup("Text");

		/*
		 * Temporary fix for Bug 516674. Pressing the SPACE key gives proper
		 * keyboard focus to the text widget.
		 */
		text.pressShortcut(Keystrokes.SPACE);

		text.setText("");

		text.typeText("Type This 123");
		assertTextContains("Type This 123", text.widget);
	}

	@Test
	public void setsTextInReadOnly() throws Exception {
		bot.checkBox("SWT.READ_ONLY").select();
		final SWTBotText text = bot.textInGroup("Text");
		assertTrue(text.isReadOnly());
		try
		{
			text.setText("");
			fail("Expecting an exception");
		} catch (Exception e) {
			assertEquals("TextBox is read-only", e.getMessage());
		}
	}

	@Test
	public void typesTextInReadOnly() throws Exception {
		bot.checkBox("SWT.READ_ONLY").select();
		final SWTBotText text = bot.textInGroup("Text");
		assertTrue(text.isReadOnly());
		try {
			text.typeText("");
			fail("Expecting an exception");
		} catch (Exception e) {
			assertEquals("TextBox is read-only", e.getMessage());
		}
	}

	@Before
	public void prepareExample() throws Exception {
		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		bot.tabItem("Text").activate();
	}

}
