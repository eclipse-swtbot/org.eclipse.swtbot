/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
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

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.utils.internal.Assert;
import org.hamcrest.SelfDescribing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementors must have a default no-args constructor.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public abstract class AbstractKeyboardStrategy implements KeyboardStrategy {

	protected final Logger	log;

	protected AbstractKeyboardStrategy() {
		this.log = LoggerFactory.getLogger(getClass());
	}

	@Override
	public void init(Widget widget, SelfDescribing description) {
		// do nothing
	}

	/**
	 * Presses the specified key.
	 * 
	 * @param key the keystroke to press down
	 */
	protected abstract void pressKey(KeyStroke key);

	/**
	 * Releases the specified key.
	 * 
	 * @param key the keystroke to release.
	 */
	protected abstract void releaseKey(KeyStroke key);

	@Override
	public void pressKeys(KeyStroke... keys) {
		assertKeys(keys);
		for (KeyStroke key : keys) {
			log.trace("Pressing down key {}", key);
			pressKey(key);
		}
	}

	@Override
	public void releaseKeys(KeyStroke... keys) {
		assertKeys(keys);
		for (KeyStroke key : keys) {
			log.trace("Releasing key {}", key);
			releaseKey(key);
		}
	}

	private void assertKeys(KeyStroke... keys) {
		for (KeyStroke key : keys) {
			assertKey(key);
		}
	}

	private void assertKey(KeyStroke key) {
		boolean hasNaturalKey = key.getNaturalKey() != KeyStroke.NO_KEY;
		boolean hasModifiers = key.getModifierKeys() != KeyStroke.NO_KEY;

		Assert.isTrue(hasNaturalKey ^ hasModifiers, "You just gave me a complex keystroke. Please split the keystroke into multiple keystrokes.");
	}

}
