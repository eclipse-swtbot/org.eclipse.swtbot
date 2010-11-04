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

import static java.awt.event.InputEvent.SHIFT_MASK;
import static java.awt.event.KeyEvent.VK_0;
import static java.awt.event.KeyEvent.VK_1;
import static java.awt.event.KeyEvent.VK_A;
import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertNotEquals;
import static org.eclipse.swtbot.swt.finder.keyboard.KeyStrokeMapping.mapping;
import static org.junit.Assert.assertEquals;

import org.eclipse.swtbot.swt.finder.keyboard.KeyStrokeMapping.Modifier;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 */
public class KeyStrokeMappingTest {

	@Test
	public void equalObjectsShouldBeEqual() throws Exception {
		assertEquals(mapping('0', VK_0, Modifier.NO_MASK), mapping('0', VK_0, Modifier.NO_MASK));
		assertNotEquals(mapping('1', VK_0, Modifier.NO_MASK), mapping('0', VK_0, Modifier.NO_MASK));
		assertNotEquals(mapping('0', VK_1, Modifier.NO_MASK), mapping('0', VK_0, Modifier.NO_MASK));
		assertNotEquals(mapping('0', VK_0, SHIFT_MASK), mapping('0', VK_0, Modifier.NO_MASK));
	}

	@Test
	public void equalObjectsShouldHaveSameHashCode() throws Exception {
		assertEquals(mapping('0', VK_0, Modifier.NO_MASK).hashCode(), mapping('0', VK_0, Modifier.NO_MASK).hashCode());
		assertNotEquals(mapping('1', VK_0, Modifier.NO_MASK).hashCode(), mapping('0', VK_0, Modifier.NO_MASK).hashCode());
		assertNotEquals(mapping('0', VK_1, Modifier.NO_MASK).hashCode(), mapping('0', VK_0, Modifier.NO_MASK).hashCode());
		assertNotEquals(mapping('0', VK_0, SHIFT_MASK).hashCode(), mapping('0', VK_0, Modifier.NO_MASK).hashCode());
	}

	@Test
	public void stringDescription() throws Exception {
		assertEquals("KeyStrokeMapping[Character='0',KeyStroke=pressed 0]", mapping('0', VK_0, Modifier.NO_MASK).toString());
		assertEquals("KeyStrokeMapping[Character='A',KeyStroke=shift pressed A]", mapping('A', VK_A, SHIFT_MASK).toString());
	}
}
