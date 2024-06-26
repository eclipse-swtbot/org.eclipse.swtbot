/*******************************************************************************
 * Copyright (c) 2009 Ketan Padegaonkar and others.
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
package org.eclipse.swtbot.swt.finder.keyboard;

import static org.eclipse.swtbot.swt.finder.utils.SWTUtils.sleep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.swt.SWT;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a Keyboard. Allows for typing keys and pressing shortcuts. Pressing shortcuts is different from pressing
 * normal characters and 'special characters'.
 * <p>
 * <b>NOTE:</b> This class needs that a {@link KeyStroke} be split. This means that a single {@link KeyStroke}
 * representing a SHIFT+T needs to be split into two {@link KeyStroke}s, one representing a SHIFT and another
 * representing a 'T'.
 * </p>
 * <p>
 * <b>Shortcut:</b> CTRL+SHIFT+T for e.g. needs to press CTRL, SHIFT, T in that order while holding them down, and
 * release them in the order T, SHIFT, CTRL.
 * </p>
 * <p>
 * <b>Normal characters:</b> 't' requires that you type 'T'. 'T' requires that you type the shortcut SHIFT+T.
 * </p>
 * <p>
 * <b>Special characters:</b> On a US keyboard '#' requires that you type SHIFT+3. ':' requires you to type SHIFT+;.
 * </p>
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 * @see Keystrokes#toKeys(int, char)
 */
public class Keyboard {

	private static final Logger		log	= LoggerFactory.getLogger(Keyboard.class);

	private final KeyboardStrategy	strategy;

	/**
	 * Creates a new keyboard.
	 */
	Keyboard() {
		this(new AWTKeyboardStrategy());
	}

	/**
	 * Creates a new keyboard that uses the specified strategy to type on the keyboard.
	 *
	 * @param strategy the keyboard strategy.
	 */
	public Keyboard(KeyboardStrategy strategy) {
		this.strategy = strategy;
	}

	/**
	 * Types the string on the keyboard.
	 *
	 * @param text the text to type on the keyboard.
	 */
	public void typeText(String text) {
		typeText(text, SWTBotPreferences.TYPE_INTERVAL);
	}

	/**
	 * Types the string on the keyboard.
	 *
	 * @param text the text to type on the keyboard.
	 * @param interval the interval between the keystrokes.
	 */
	public void typeText(String text, int interval) {
		log.debug("Typing text ''{}'' with an interval of {1}ms. between characters.", text, interval);
		for (int i = 0; i < text.length(); i++) {
			typeCharacter(text.charAt(i));
			sleep(interval);
		}
	}

	/**
	 * Types the character on the keyboard. Note that the character may refer to multiple keystrokes.
	 *
	 * @param ch the character to type on the keyboard.
	 */
	public void typeCharacter(char ch) {
		pressShortcut(Keystrokes.create(ch));
	}

	/**
	 * Presses the shortcut specified by the given keys.
	 *
	 * @param modificationKeys the combination of {@link SWT#ALT} | {@link SWT#CTRL} | {@link SWT#SHIFT} |
	 *            {@link SWT#COMMAND}.
	 * @param c the character
	 * @see Keystrokes#toKeys(int, char)
	 */
	public void pressShortcut(int modificationKeys, char c) {
		pressShortcut(Keystrokes.toKeys(modificationKeys, c));
	}

	/**
	 * Presses the shortcut specified by the given keys.
	 *
	 * @param keys the keys to press
	 * @see Keystrokes#toKeys(int, char)
	 */
	public void pressShortcut(KeyStroke... keys) {
		log.trace("Pressing shortcut {}", Arrays.asList(keys));
		pressKeys(keys);
		releaseKeys(reverse(keys));
	}

	/**
	 * Presses the shortcut specified by the given keys.
	 *
	 * @param modificationKeys the combination of {@link SWT#ALT} | {@link SWT#CTRL} | {@link SWT#SHIFT} |
	 *            {@link SWT#COMMAND}.
	 * @param keyCode the keyCode, these may be special keys like F1-F12, or navigation keys like HOME, PAGE_UP
	 * @param c the character
	 * @see Keystrokes#toKeys(int, char)
	 */
	public void pressShortcut(int modificationKeys, int keyCode, char c) {

		List<KeyStroke> keys = new ArrayList<KeyStroke>(Arrays.asList(Keystrokes.toKeys(modificationKeys, c)));
		if (keyCode != 0)
			addKeyCode(keyCode, c, keys);

		pressShortcut(keys.toArray(new KeyStroke[keys.size()]));

	}

	private KeyStroke[] reverse(KeyStroke... keys) {
		ArrayList<KeyStroke> copy = new ArrayList<KeyStroke>(Arrays.asList(keys));
		Collections.reverse(copy);
		return copy.toArray(new KeyStroke[copy.size()]);
	}

	private void releaseKeys(KeyStroke... keys) {
		strategy.releaseKeys(keys);
	}

	private void pressKeys(KeyStroke... keys) {
		strategy.pressKeys(keys);
	}

	private void addKeyCode(int keyCode, char c, List<KeyStroke> keys) {
		if (c == 0)
			keys.add(KeyStroke.getInstance(0, keyCode));
		else
			keys.add(keys.size() - 1, KeyStroke.getInstance(0, keyCode));
	}

}
