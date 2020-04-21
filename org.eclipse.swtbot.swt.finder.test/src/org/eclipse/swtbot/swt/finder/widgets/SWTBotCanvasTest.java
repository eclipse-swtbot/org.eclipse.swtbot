/*******************************************************************************
 * Copyright (c) 2016 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Tasse - Initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;

import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Patrick Tasse
 * @version $Id$
 */
public class SWTBotCanvasTest extends AbstractControlExampleTest {

	private SWTBotText	listeners;

	@Test
	public void findsCanvas() throws Exception {
		assertNotNull(bot.canvas().widget); // the Shell
		assertNotNull(bot.canvas(1).widget); // the Canvas
	}

	@Test
	public void findsCanvasInGroup() throws Exception {
		assertNotNull(bot.canvasInGroup("Canvas").widget);
		assertNotNull(bot.canvasInGroup("Canvas", 0).widget);
	}

	@Test
	public void click() throws Exception {
		final SWTBotCanvas canvas = bot.canvasInGroup("Canvas");
		canvas.click();
		assertThat(listeners.getText(), containsString("Clicked on Canvas: button=1 "));
	}

	@Test
	public void clickXY() throws Exception {
		final SWTBotCanvas canvas = bot.canvasInGroup("Canvas");
		canvas.click(10, 20);
		assertThat(listeners.getText(), containsString("Clicked on Canvas: button=1 x=10 y=20"));
	}

	@Test
	public void doubleClick() throws Exception {
		final SWTBotCanvas canvas = bot.canvasInGroup("Canvas");
		canvas.doubleClick();
		assertThat(listeners.getText(), containsString("Double-clicked on Canvas: button=1 "));
	}

	@Test
	public void doubleClickXY() throws Exception {
		final SWTBotCanvas canvas = bot.canvasInGroup("Canvas");
		canvas.doubleClick(10, 20);
		assertThat(listeners.getText(), containsString("Double-clicked on Canvas: button=1 x=10 y=20"));
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("Canvas").activate();
		listeners = bot.textInGroup("Listeners");
	}

}
