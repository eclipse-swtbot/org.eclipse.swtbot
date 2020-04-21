/*******************************************************************************
 * Copyright (c) 2010, 2017 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.test;

import static org.hamcrest.Matchers.containsString;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.UIThread;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public abstract class AbstractSWTShellTest extends AbstractSWTTest {

	protected static final String SHELL_TEXT = "Test shell";
	protected Display display;
	protected Shell shell;

	@UIThread
	public final void runUIThread() {
		display = Display.getDefault();

		shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());
		shell.setText(SHELL_TEXT);

		createUI(shell);
		shell.open();
	}

	protected abstract void createUI(Composite parent);

	//TODO: move into some Assert class
	protected void assertEventMatches(final SWTBotText listeners, String expected) {
		expected = expected.replaceAll("time=-?\\d+", "time=SOME_TIME_AGO").replaceAll("x=-?\\d+", "x=X_CO_ORDINATE").replaceAll("y=-?\\d+", "y=Y_CO_ORDINATE");
		final Matcher<String> matcher = containsString(expected);
		bot.waitUntil(new DefaultCondition() {

			private String	text;

			@Override
			public boolean test() throws Exception {
				text = listeners.getText();
				// keyLocation was added in 3.6, we don't care about it for the tests
				String listenersText = text.replaceAll("time=-?\\d+", "time=SOME_TIME_AGO").replaceAll("x=-?\\d+", "x=X_CO_ORDINATE").replaceAll("y=-?\\d+", "y=Y_CO_ORDINATE").replaceAll("keyLocation=(0x)?[0-9a-f]+ ", "");
				return matcher.matches(listenersText);
			}

			@Override
			public String getFailureMessage() {
				Description description = new StringDescription();
				description.appendText("\nExpected:\n").appendDescriptionOf(matcher).appendText("\ngot:\n").appendValue(text).appendText("\n");
				return description.toString();
			}
		});
	}

	// State mask written in Hexa for SWT 4.2 and decimal for SWT 3.x.
	// This method create a stateMask according the SWT version 
	public static String toStateMask(int n, Widget widget) {
		// looks into how SelectionEvent.toString is implemented
		Event e = new Event();
		e.widget = widget;
		String toStringTreeEvent = new SelectionEvent(e).toString();
		if (toStringTreeEvent.contains("stateMask=0x")) {
			// SWT 4.2 uses hexa
			return "0x" + Integer.toHexString(n);
		} else {
			// SWT 3.8 uses decimal
			return Integer.toString(n);
		}
	}
	
	// KeyCode written in Hexa for SWT 4.2 and decimal for SWT 3.x.
	// This method create a stateMask according the SWT version 
	public static String toKeyCode(int n, Widget widget) {
		// looks into how KeyEvent.toString is implemented
		Event e = new Event();
		e.widget = widget;
		String toStringTreeEvent = new KeyEvent(e).toString();
		if (toStringTreeEvent.contains("keyCode=0x")) {
			// SWT 4.2 uses hexa
			return "0x" + Integer.toHexString(n);
		} else {
			// SWT 3.8 uses decimal
			return Integer.toString(n);
		}
	}
	
	// character='t'=0x74 for SWT 4.2 and character='t' for SWT 3.x.
	// This method create a stateMask according the SWT version 
	public static String toCharacter(char c, Widget widget) {
		// looks into how KeyEvent.toString is implemented
		Event e = new Event();
		e.widget = widget;
		String toStringTreeEvent = new KeyEvent(e).toString();
		if (toStringTreeEvent.contains("character='\\0'=0x0")) {
			// SWT 4.2 uses both notation
			return "'" + ((c == 0) ? "\\0" : String.valueOf(c)) + "'=0x" + Integer.toHexString(c);
		} else {
			// SWT 3.8 uses only char
			return "'" + ((c == 0) ? "\\0" : String.valueOf(c)) + "'";
		}
	}
}