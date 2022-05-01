/*******************************************************************************
 * Copyright (c) 2015, 2022 Stephane Bouchet (Intel Corporation).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
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
		assertTrue("Team node is selected", selectTeamNode());
		/*
		 * Indices are used to identify radio buttons because group name and
		 * mnemonicText can change across releases
		 */
		bot.radio(1).click();
		bot.radio(4).click();
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
		assertTrue("Team node is selected", selectTeamNode());
		SWTBotRadio pref1 = bot.radio(1);
		SWTBotRadio pref2 = bot.radio(4);
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

	private boolean selectTeamNode() {
		// eclipse 4.18 has "Version Control(Team)" node, earlier versions have "Team"
		SWTBotTreeItem[] items = bot.tree().getAllItems();
		for (SWTBotTreeItem item : items) {
			if (item.getText().contains("Team")) {
				item.select();
				return true;
			}
		}
		return false;
	}

}
