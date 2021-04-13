/*******************************************************************************
 * Copyright (c) 2021 Cadence Design Systems, Inc. and others.
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
package org.eclipse.swtbot.nebula.rangeslider.finder.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.nebula.widgets.opal.rangeslider.RangeSlider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.nebula.rangeslider.finder.SWTRangeSliderBot;
import org.eclipse.swtbot.nebula.rangeslider.finder.widgets.SWTBotRangeSlider;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class SWTBotRangeSliderTest {
	public SWTRangeSliderBot bot;
	public static RangeSlider rangeslider;
	public Shell shell;

	private int initialLowerValue = 0, initialUpperValue = 60;

	@Before
	public void setUp() {
		bot = new SWTRangeSliderBot();
		runInUIThread();
	}

	private void runInUIThread() {
		final Display display = Display.getDefault();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				shell = createShell(display, "Nebula RangeSlider Test");
				rangeslider = createRangeSlider(shell);
				shell.open();
			}
		});
	}

	protected Shell createShell(final Display display, final String text) {
		Shell shell = new Shell(display);
		shell.setText(text);
		shell.setLayout(new FillLayout());
		return shell;
	}

	/*
	 * Example taken from
	 * https://github.com/eclipse/nebula/blob/master/widgets/opal/rangeslider/org.
	 * eclipse.nebula.widgets.opal.rangeslider.snippets/src/org/eclipse/nebula/
	 * widgets/opal/rangeslider/snippets/RangeSliderSnippet.java
	 */
	private RangeSlider createRangeSlider(final Shell shell) {
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		Group group = new Group(shell, SWT.NONE);
		group.setLayout(new GridLayout(3, false));
		final RangeSlider hRangeSlider = new RangeSlider(group, SWT.HORIZONTAL);
		final GridData gd = new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 2);
		gd.widthHint = 250;

		hRangeSlider.setLayoutData(gd);
		hRangeSlider.setMinimum(0);
		hRangeSlider.setMaximum(100);
		hRangeSlider.setLowerValue(initialLowerValue);
		hRangeSlider.setUpperValue(initialUpperValue);

		final Label hLabelLower = new Label(group, SWT.NONE);
		hLabelLower.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false, 1, 1));
		hLabelLower.setText("Lower Value:");

		final Text hTextLower = new Text(group, SWT.BORDER);
		hTextLower.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false, 1, 1));
		hTextLower.setText(hRangeSlider.getLowerValue() + "   ");
		hTextLower.setEnabled(false);

		final Label hLabelUpper = new Label(group, SWT.NONE);
		hLabelUpper.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false, 1, 1));
		hLabelUpper.setText("Upper Value:");

		final Text hTextUpper = new Text(group, SWT.BORDER);
		hTextUpper.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false, 1, 1));
		hTextUpper.setText(hRangeSlider.getUpperValue() + "   ");
		hTextUpper.setEnabled(false);

		hRangeSlider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				hTextLower.setText(hRangeSlider.getLowerValue() + "   ");
				hTextUpper.setText(hRangeSlider.getUpperValue() + "   ");
			}
		});
		return hRangeSlider;
	}

	@Test
	public void testSetSelection() {
		SWTBotRangeSlider swtbotRangeSlider = bot.rangeSlider();
		SWTBotText lowerValueTxt = bot.text(0);
		SWTBotText upperValueTxt = bot.text(1);
		// check initial values
		assertEquals(swtbotRangeSlider.getLowerValue(), initialLowerValue);
		assertEquals(swtbotRangeSlider.getLowerSelection(), initialLowerValue);
		assertEquals(swtbotRangeSlider.getUpperValue(), initialUpperValue);
		assertEquals(swtbotRangeSlider.getUpperSelection(), initialUpperValue);
		assertEquals(Integer.parseInt(lowerValueTxt.getText().trim()), initialLowerValue);
		assertEquals(Integer.parseInt(upperValueTxt.getText().trim()), initialUpperValue);
		// change lower and upper values both at the same time
		int lowerVal = 10, upperVal = 50;
		swtbotRangeSlider.setSelection(lowerVal, upperVal);
		assertEquals("Lower value changed", lowerVal, swtbotRangeSlider.getLowerValue());
		assertEquals("Upper value changed", upperVal, swtbotRangeSlider.getUpperValue());
		assertEquals(Integer.parseInt(lowerValueTxt.getText().trim()), lowerVal);
		assertEquals(Integer.parseInt(upperValueTxt.getText().trim()), upperVal);
	}

	@Test
	public void testSetLowerValue() {
		SWTBotRangeSlider swtbotRangeSlider = bot.rangeSlider();
		SWTBotText lowerValueTxt = bot.text(0);
		SWTBotText upperValueTxt = bot.text(1);
		assertEquals("Initial lower value", initialLowerValue, swtbotRangeSlider.getLowerValue());
		// change only lower value
		int lowerVal = 20;
		swtbotRangeSlider.setLowerValue(lowerVal);
		assertEquals("Lower value changed", lowerVal, swtbotRangeSlider.getLowerValue());
		assertEquals("Upper value", initialUpperValue, swtbotRangeSlider.getUpperValue());
		assertEquals(Integer.parseInt(lowerValueTxt.getText().trim()), lowerVal);
		assertEquals(Integer.parseInt(upperValueTxt.getText().trim()), initialUpperValue);
	}

	@Test
	public void testSetUpperValue() {
		SWTBotRangeSlider swtbotRangeSlider = bot.rangeSlider();
		SWTBotText lowerValueTxt = bot.text(0);
		SWTBotText upperValueTxt = bot.text(1);
		assertEquals("Initial upper value", initialUpperValue, swtbotRangeSlider.getUpperValue());
		// change only upper value
		int upperVal = 90;
		swtbotRangeSlider.setUpperValue(upperVal);
		assertEquals("Lower value", initialLowerValue, swtbotRangeSlider.getLowerValue());
		assertEquals("Upper value changed", upperVal, swtbotRangeSlider.getUpperValue());
		assertEquals(Integer.parseInt(lowerValueTxt.getText().trim()), initialLowerValue);
		assertEquals(Integer.parseInt(upperValueTxt.getText().trim()), upperVal);
	}

	@After
	public void tearDown() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				if (shell != null) {
					shell.dispose();
				}
			}
		});
	}

}
