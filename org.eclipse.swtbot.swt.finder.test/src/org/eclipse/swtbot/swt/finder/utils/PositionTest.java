/*******************************************************************************
 * Copyright (c) 2008-2010 Ketan Padegaonkar and others.
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
package org.eclipse.swtbot.swt.finder.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class PositionTest {

	@Test
	public void equalsAndHashCode() throws Exception {
		assertEquals(position(), position());
		assertEquals(position().hashCode(), position().hashCode());

		Position position = position();
		assertEquals(position, position);
		assertEquals(position.hashCode(), position.hashCode());

		Object other = "";
		assertFalse(position.equals(other));
	}

	@Test
	public void equalsAndHashCodeNegative() throws Exception {
		assertFalse(new Position(1, 10).equals(new Position(2, 10)));
		assertFalse(new Position(1, 10).equals(new Position(1, 20)));

		assertFalse(new Position(1, 10).hashCode() == new Position(2, 10).hashCode());
		assertFalse(new Position(1, 10).hashCode() == new Position(1, 20).hashCode());
	}

	private Position position() {
		return new Position(1, 10);
	}

}
