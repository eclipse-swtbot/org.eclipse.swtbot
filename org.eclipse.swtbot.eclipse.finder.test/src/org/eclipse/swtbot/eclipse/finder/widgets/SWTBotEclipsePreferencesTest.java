/*******************************************************************************
 * Copyright (c) 2015 Stephane Bouchet (Intel Corporation).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephane Bouchet (Intel Corporation) - initial API and implementation
 *     Patrick Tasse - Speed up SWTBot tests
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.widgets;

import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotRadio;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Stephane Bouchet &lt;stephane [dot] bouchet [at] intel [dot] com &gt;
 * @version $Id$
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class SWTBotEclipsePreferencesTest extends AbstractSWTBotEclipseTest {

	private static SWTWorkbenchBot	bot	= new SWTWorkbenchBot();

	@Test
	public void canSelectMultipleRadioInPreferencesWindow() {
		// this will simply tries to change two radio in the team preferences at same times
		bot.menu("Window").menu("Preferences").click();
		SWTBotShell prefsShell = bot.shell("Preferences");
		prefsShell.activate();
		bot.tree().getTreeItem("Team").select().click();
		bot.radioInGroup("Tree", "Choose the presentation to be used when displaying Workspace projects").click();
		bot.radioInGroup("Never", "Open the associated perspective when a synchronize operation completes").click();
		prefsShell.activate();
		SWTBotButton apply = bot.button("Apply");
		bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.widgetIsEnabled(apply));
		apply.click();
		SWTBotButton ok = getOkButtonInPreferencesDialog();
		bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.widgetIsEnabled(ok));
		ok.click();
		bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses(prefsShell));
		bot.menu("Window").menu("Preferences").click();
		prefsShell = bot.shell("Preferences");
		prefsShell.activate();
		bot.tree().getTreeItem("Team").select().click();
		SWTBotRadio pref1 = bot.radioInGroup("Tree", "Choose the presentation to be used when displaying Workspace projects");
		SWTBotRadio pref2 = bot.radioInGroup("Never", "Open the associated perspective when a synchronize operation completes");
		assertTrue("Radio should be selected", pref1.isSelected());
		assertTrue("Radio should be selected", pref2.isSelected());
	}

	private SWTBotButton getOkButtonInPreferencesDialog() {
		// Button text was "OK" before Oxygen M7, "Apply and Close" since.
		int i = 0;
		SWTBotButton button = bot.button(i++);
		while (!button.getText().equals("Apply and Close") && !button.getText().equals("OK")) {
			button = bot.button(i++);
		}
		return button;
	}
}
