/*******************************************************************************
 * Copyright (c) 2010 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.keyboard;

import javax.swing.KeyStroke;

/**
 * Maps a character to a {@link KeyStroke}
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @see KeyStroke#getKeyStroke(int, int)
 */
public class KeyStrokeMapping {

	public interface Modifier {

		int	NO_MASK	= 0;
	}

	private final char		character;
	private final KeyStroke	keyStroke;

	public static KeyStrokeMapping mapping(char character, int keyCode, int modifiers) {
		return new KeyStrokeMapping(character, keyCode, modifiers);
	}

	public KeyStrokeMapping(char character, int keyCode, int modifiers) {
		this(character, KeyStroke.getKeyStroke(keyCode, modifiers));
	}

	public KeyStrokeMapping(char character, KeyStroke keyStroke) {
		this.character = character;
		this.keyStroke = keyStroke;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof KeyStrokeMapping))
			return false;
		KeyStrokeMapping other = (KeyStrokeMapping) o;
		if (character != other.character)
			return false;
		if (keyStroke == null)
			return other.keyStroke == null;
		if (other.keyStroke == null)
			return false;
		if (keyStroke.getKeyCode() != other.keyStroke.getKeyCode())
			return false;
		return keyStroke.getModifiers() == other.keyStroke.getModifiers();
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + character;
		if (keyStroke == null)
			return result;
		result = prime * result + keyStroke.getKeyCode();
		result = prime * result + keyStroke.getModifiers();
		return result;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(getClass().getSimpleName()).append("[");
		b.append("Character='").append(character).append("',");
		b.append("KeyStroke=").append(keyStroke).append("]");
		return b.toString();
	}
}
