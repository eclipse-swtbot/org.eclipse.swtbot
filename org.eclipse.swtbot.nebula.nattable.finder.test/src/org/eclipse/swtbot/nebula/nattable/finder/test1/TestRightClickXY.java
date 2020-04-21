/*******************************************************************************
 * Copyright (c) 2016 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Aparna Argade(Cadence Design Systems, Inc.) - Test case of Bug 489179
 *******************************************************************************/
package org.eclipse.swtbot.nebula.nattable.finder.test1;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swtbot.nebula.nattable.finder.widgets.SWTBotNatTable;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.SWTBotEvents;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class TestRightClickXY extends _5015_AutomaticDataSpanningExample {
	private boolean showEventOccurance = false;
	private int row = 0, column = 0;
	/**
	 * The test adds event handler. After right clicking on a cell of NatTable,
	 * it reads MouseDown, MouseUp and Show events and validates event
	 * parameters.
	 */
	@Test
	public void testRightClickXY() {
		SWTBotNatTable nattable = bot.nattable();
		final Display display = nattable.widget.getDisplay();
		syncExec(new VoidResult() {
			@Override
			public void run() {
				Listener listener = new EventListener();
				for (int event : SWTBotEvents.events()) {
					display.addFilter(event, listener);
				}
			}
		});
		nattable.rightClick(row, column);
		syncExec(new VoidResult() {
			@Override
			public void run() {
				if (!shell.isDisposed()) {
					display.readAndDispatch();
					display.sleep();
				}
			}
		});
		assertTrue(showEventOccurance);
	}

	/**
	 * Event descriptions before fixing Bug 489179
	 * MouseDown: button=1 stateMask=0x200000 count=1
	 * MouseUp: button=0 stateMask=0x0 count=0
	 * Show event was not occurring.
	 *
	 * Event descriptions after fixing Bug 489179
	 * MouseDown : button=3 stateMask=0x0 count=1
	 * MouseUp: button=3 stateMask=0x200000 count=1
	 * Show:MenuEvent{Menu {}}
	 */
	class EventListener implements Listener {
		@Override
		public void handleEvent(Event event) {
			if (!(event.type == SWT.MouseDown || event.type == SWT.MouseUp || event.type == SWT.Show))
				return;
			String description = SWTBotEvents.toString(event);
			switch (event.type) {
			case SWT.MouseDown:
				assertThat(description, containsString("button=3 stateMask=0x0"));
				assertThat(description, containsString("count=1"));
				verifyRowColumnFromXY(description);
				break;
			case SWT.MouseUp:
				assertThat(description, containsString("button=3 stateMask=0x200000"));
				assertThat(description, containsString("count=1"));
				verifyRowColumnFromXY(description);
				break;
			case SWT.Show:
				showEventOccurance = true;
				assertThat(description, containsString("MenuEvent{Menu {}"));
			}
		}
	}
	
	/**
	 * This method derives x, y coordinates from the description string and
	 * obtains row and column positions from x, y using NatTable APIs
	 *
	 * @param description
	 *            description of SWTBotEvent containing x, y coordinates
	 */
	private void verifyRowColumnFromXY(String description)
	{
		/*
		 * split the string, get x or y part out of it, replace all non-digit
		 * with blank: the remaining string contains only digits
		 */
		int x = Integer.parseInt(description.split(" ")[8].replaceAll("[\\D]", ""));
		int obtainedColumn = bot.nattable().widget.getColumnPositionByX(x);

		int y = Integer.parseInt(description.split(" ")[9].replaceAll("[\\D]", ""));;
		int obtainedRow = bot.nattable().widget.getRowPositionByY(y);

		assertEquals("Row number is verified", row, obtainedRow);
		assertEquals("Column number is verified", column, obtainedColumn);
	}

}
