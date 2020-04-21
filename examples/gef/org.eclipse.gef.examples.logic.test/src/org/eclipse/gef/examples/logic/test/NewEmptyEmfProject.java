/*******************************************************************************
 * Copyright (c) 2009 Obeo
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Mariot Chauvin <mariot.chauvin@obeo.fr> - initial API and implementation
 *     Steve Monnier <steve.monnier@obeo.fr> - fix and evolution
 *******************************************************************************/

package org.eclipse.gef.examples.logic.test;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;

public class NewEmptyEmfProject {

	private SWTWorkbenchBot	bot	= new SWTWorkbenchBot();

	public void createProject(String projectName) throws Exception {
		bot.menu("File").menu("New").menu("Other...").click();
		
		SWTBotShell shell = bot.shell("New");
		shell.activate();
		
	    SWTBotTree projectSelectionTree = bot.tree();
	    projectSelectionTree.expandNode("Eclipse Modeling Framework").expandNode("Empty EMF Project").select();
	    bot.waitUntil(new DefaultCondition() {
			@Override
			public String getFailureMessage() {
				return "unable to select";
			}
			
			@Override
			public boolean test() throws Exception {
				return bot.button("Next >").isEnabled();
			}	
	    	
	    });
	    bot.button("Next >").click();
		bot.textWithLabel("Project name:").setText(projectName);
		bot.button("Finish").click();
		bot.waitUntil(Conditions.shellCloses(shell));
	}	
}
