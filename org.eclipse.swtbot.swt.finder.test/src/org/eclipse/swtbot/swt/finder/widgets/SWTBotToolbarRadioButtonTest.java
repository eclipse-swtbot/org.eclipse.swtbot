/*******************************************************************************
 * Copyright (c) 2009, 2015 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Patrick Tasse - fix click behavior and support click with modifiers
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertNotSameWidget;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotToolbarRadioButtonTest extends AbstractControlExampleTest {

	@Test
	public void findsToolBarButtonWithIndex() throws Exception {
		SWTBotToolbarRadioButton button0 = bot.toolbarRadioButton("Radio", 0);
		SWTBotToolbarRadioButton button1 = bot.toolbarRadioButton("Radio", 1);
		assertNotSameWidget(button0.widget, button1.widget);
	}

	@Test
	public void clicksRadioButton() throws Exception {
		SWTBotToolbarRadioButton button = bot.toolbarRadioButton("Radio");
		button.click();
		assertEventMatches(bot.textInGroup("Listeners"), "Selection [13]: SelectionEvent{ToolItem {Radio} time=0 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, button.widget) + " text=null doit=true}");
	}

	@Test
	public void clicksRadioButtonWithModifier() throws Exception {
		SWTBotToolbarRadioButton button = bot.toolbarRadioButton("Radio");
		button.click(SWT.SHIFT);
		assertEventMatches(bot.textInGroup("Listeners"), "Selection [13]: SelectionEvent{ToolItem {Radio} time=0 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(SWT.SHIFT, button.widget) + " text=null doit=true}");
	}

	@Test
	public void clickingRadioButtonDeselectsOther() throws Exception {
		SWTBotToolbarRadioButton button0 = bot.toolbarRadioButton("Radio", 0);
		SWTBotToolbarRadioButton button1 = bot.toolbarRadioButton("Radio", 1);
		button0.click();
		assertTrue(button0.isChecked());
		assertTrue(!button1.isChecked());
		button0.click();
		assertTrue(button0.isChecked());
		assertTrue(!button1.isChecked());
		button1.click();
		assertTrue(!button0.isChecked());
		assertTrue(button1.isChecked());
	}

	@Test
	public void togglesRadioButton() throws Exception {
		SWTBotToolbarRadioButton button = bot.toolbarRadioButton("Radio");
		boolean checked = button.isChecked();
		button.toggle();
		assertTrue(checked != button.isChecked());
		button.toggle();
		assertTrue(checked == button.isChecked());
	}

	@Test
	public void togglingRadioButtonDeselectsOtherConditionally() throws Exception {
		SWTBotToolbarRadioButton button0 = bot.toolbarRadioButton("Radio", 0);
		SWTBotToolbarRadioButton button1 = bot.toolbarRadioButton("Radio", 1);
		button0.deselect();
		button1.select();
		assertTrue(!button0.isChecked());
		assertTrue(button1.isChecked());
		button0.toggle();
		assertTrue(button0.isChecked());
		assertTrue(!button1.isChecked());
		button0.toggle();
		assertTrue(!button0.isChecked());
		assertTrue(!button1.isChecked());
		button0.toggle();
		assertTrue(button0.isChecked());
		assertTrue(!button1.isChecked());
	}

	@Test
	public void selectsRadioButton() throws Exception {
		SWTBotToolbarRadioButton button = bot.toolbarRadioButton("Radio");
		button.deselect();
		assertTrue(!button.isChecked());
		button.select();
		assertTrue(button.isChecked());
		button.select();
		assertTrue(button.isChecked());
	}

	@Test
	public void deselectsRadioButton() throws Exception {
		SWTBotToolbarRadioButton button = bot.toolbarRadioButton("Radio");
		button.select();
		assertTrue(button.isChecked());
		button.deselect();
		assertTrue(!button.isChecked());
		button.deselect();
		assertTrue(!button.isChecked());
	}

	@Before
	public void setUp() throws Exception {
		bot.tabItem("ToolBar").activate();
		bot.checkBox("Listen").select();
	}

}