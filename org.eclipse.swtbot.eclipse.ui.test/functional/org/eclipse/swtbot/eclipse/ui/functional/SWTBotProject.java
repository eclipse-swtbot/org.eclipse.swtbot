/*******************************************************************************
 * Copyright (c) 2010, 2015 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Patrick Tasse - Improve SWTBot menu API and implementation (Bug 479091) 
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.ui.functional;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 */
public class SWTBotProject {

	private final SWTWorkbenchBot	bot	= new SWTWorkbenchBot();

	public void create(String projectId) {
		bot.menu().menu("File").menu("New").menu("Project...").click();
		SWTBotShell shell = bot.shell("New Project");
		shell.activate();

		bot.tree().expandNode("SWTBot", "SWTBot Test Plug-in").select();
		bot.button("Next >").click();

		bot.textWithLabel("Plug-in Name:").setText(projectId);
		bot.textWithLabel("Plug-in id:").setText(projectId);
		bot.textWithLabel("Provider:").setText("ACME Corp.");
		bot.button("Finish").click();
		bot.waitUntil(shellCloses(shell));
	}

}
