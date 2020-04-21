/*******************************************************************************
 * Copyright (c) 2018 Cadence Design Systems, Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Aparna Argade(Cadence Design Systems, Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.swt.SWT;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.junit.Before;
import org.junit.Test;

public class SWTBotProgressBarTest extends AbstractControlExampleTest {

	private int initialSelection = 50;

	@Test
	public void findsProgressBar() throws Exception {
		assertNotNull(bot.progressbar().widget);
	}

	@Test
	public void findsProgressBarInGroup() throws Exception {
		assertNotNull(bot.progressbarInGroup("ProgressBar").widget);
		assertNotNull(bot.progressbarInGroup("ProgressBar", 0).widget);
	}

	@Test
	public void testsInitialValues() throws Exception {
		assertEquals(0, bot.progressbar().getMinimum());
		assertEquals(100, bot.progressbar().getMaximum());
		assertEquals(initialSelection, bot.progressbar().getProgress());
		assertEquals(initialSelection, bot.progressbar().getProgressPercentage());
		assertEquals(SWT.NORMAL, bot.progressbar().getState());
	}

	@Test
	public void testsProgressPercentage() throws Exception {
		int min = 20, max = 40, selection = 30, percentage = 50;
		bot.spinnerInGroup("Minimum").setSelection(min);
		bot.spinnerInGroup("Maximum").setSelection(max);
		bot.spinnerInGroup("Selection").setSelection(selection);
		assertEquals(min, bot.progressbar().getMinimum());
		assertEquals(max, bot.progressbar().getMaximum());
		assertEquals(selection, bot.progressbar().getProgress());
		assertEquals(percentage, bot.progressbar().getProgressPercentage());
	}

	@Test
	public void testsGetStyle() throws Exception {
		bot.checkBox(SWT.INDETERMINATE).select();
		assertEquals(SWT.H_SCROLL & bot.progressbar().getStyle(), SWT.H_SCROLL);
		assertEquals(SWT.INDETERMINATE & bot.progressbar().getStyle(), SWT.INDETERMINATE);
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("ProgressBar").activate();
		bot.spinnerInGroup("Selection").setSelection(initialSelection);
	}

}
