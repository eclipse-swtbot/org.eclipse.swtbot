/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.widgets.helpers;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

/**
 * Screen object to create a new java project
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class NewJavaProject {

	private SWTWorkbenchBot	bot	= new SWTWorkbenchBot();

	public void createProject(String projectName) throws Exception {
		try {
			SWTBotMenu windowMenu = bot.menu("Window");
			SWTBotMenu perspectiveMenu = windowMenu.menu("Open Perspective");
			SWTBotMenu javaPerspectiveMenu = perspectiveMenu.menu("Java");
			if (javaPerspectiveMenu.isVisible() && javaPerspectiveMenu.isEnabled()) { 
				javaPerspectiveMenu.click();
			}
		} catch (Exception ex) {
			// Java menu not available: already selected
		}
		bot.menu("File").menu("New").menu("Java Project").click();
	

		SWTBotShell shell = bot.shell("New Java Project");
		shell.activate();
		bot.textWithLabel("Project name:").setText(projectName);
		bot.button("Finish").click();

		bot.waitUntil(Conditions.shellCloses(shell));

	}

}
