/*******************************************************************************
 * Copyright (c) 2013 Marcel Hoetter
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Marcel Hoetter - initial implementation
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.widgets;

import org.eclipse.swtbot.eclipse.finder.test.ui.menucontributions.ToolbarTextContribution;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Marcel Hoetter &lt;Marcel.Hoetter [at] genuinesoftware [dot] de&gt;
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class SWTBotToolbarContributionTest extends AbstractSWTBotEclipseTest {
	
	@Test
	public void canFindControlContributionWithId() {
		bot.textWithId(ToolbarTextContribution.ID);
	}
	
	@Test
	public void canFindControlContributionWithText() {
		bot.text(ToolbarTextContribution.TEXT);
	}
	
	@Test
	public void canFindControlContributionWithTooltip() {
		bot.textWithTooltip(ToolbarTextContribution.TOOLTIP);
	}
}