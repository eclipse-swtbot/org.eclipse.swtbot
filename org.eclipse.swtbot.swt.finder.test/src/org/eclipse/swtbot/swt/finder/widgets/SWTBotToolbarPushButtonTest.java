/*******************************************************************************
 * Copyright (c) 2008, 2015 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Patrick Tasse - support click with modifiers
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertNotSameWidget;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.pass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotToolbarPushButtonTest extends AbstractControlExampleTest {

	private long	oldTimeout;

	@Before
	public void lowerTimeout() {
		this.oldTimeout = SWTBotPreferences.TIMEOUT;
		SWTBotPreferences.TIMEOUT = 2000;
	}

	@After
	public void resetTimeout() {
		SWTBotPreferences.TIMEOUT = oldTimeout;
	}

	@Test
	public void findsToolBarPushButtons() throws Exception {
		bot.toolbarButton("Push");
	}

	@Test
	public void findsToolBarButtonWithIndex() throws Exception {
		SWTBotToolbarButton button0 = bot.toolbarButton("Push");
		SWTBotToolbarButton button1 = bot.toolbarButton("Push", 1);
		assertNotSameWidget(button0.widget, button1.widget);
	}

	@Test
	public void doesNotFindToolBarRadioButtons() throws Exception {
		try {
			bot.toolbarButton("Radio");
			fail("Expecting an exception");
		} catch (WidgetNotFoundException e) {
			pass();
		}
	}

	@Test
	public void doesNotFindToolBarCheckButtons() throws Exception {
		try {
			bot.toolbarButton("Check");
			fail("Expecting an exception");
		} catch (WidgetNotFoundException e) {
			pass();
		}
	}

	@Test
	public void clicksToolBarButton() throws Exception {
		try {
			bot.checkBox("Listen").select();
			final SWTBotToolbarButton button = bot.toolbarButton("Push");
			button.click();
			assertEventMatches(bot.textInGroup("Listeners"), "Selection [13]: SelectionEvent{ToolItem {Push} time=0 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, button.widget) + " text=null doit=true}");
		} finally {
			bot.checkBox("Listen").deselect();
		}
	}

	@Test
	public void clicksToolBarButtonWithModifier() throws Exception {
		try {
			bot.checkBox("Listen").select();
			final SWTBotToolbarButton button = bot.toolbarButton("Push");
			button.click(SWT.SHIFT);
			assertEventMatches(bot.textInGroup("Listeners"), "Selection [13]: SelectionEvent{ToolItem {Push} time=0 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(SWT.SHIFT, button.widget) + " text=null doit=true}");
		} finally {
			bot.checkBox("Listen").deselect();
		}
	}

	@Test
	public void isEnabled() throws Exception {
		final SWTBotToolbarButton button = bot.toolbarButton("Push");
		assertTrue(button.isEnabled());
		try {
			UIThreadRunnable.syncExec(display, new VoidResult() {
				@Override
				public void run() {
					button.widget.setEnabled(false);
				}
			});
			assertFalse(button.isEnabled());
		} finally {
			UIThreadRunnable.syncExec(display, new VoidResult() {
				@Override
				public void run() {
					button.widget.setEnabled(true);
				}
			});
		}
	}

	@Before
	public void activateTab() throws Exception {
		bot.tabItem("ToolBar").activate();
	}
}
