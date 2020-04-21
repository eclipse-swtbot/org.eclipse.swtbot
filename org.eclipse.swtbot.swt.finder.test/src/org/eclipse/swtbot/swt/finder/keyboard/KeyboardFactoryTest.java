/*******************************************************************************
 * Copyright (c) 2008, 2017 Ketan Padegaonkar and others.
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

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtbot.swt.finder.test.AbstractSWTShellTest;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class KeyboardFactoryTest extends AbstractSWTShellTest {

	@Test
	public void createsKeyboardForAWTKeyboardStrategy() throws Exception {
		assertEquals(AWTKeyboardStrategy.class, new KeyboardFactory(AWTKeyboardStrategy.class).strategyClass);
	}

	@Test
	public void createsKeyboardForSWTKeyboardStrategy() throws Exception {
		assertEquals(SWTKeyboardStrategy.class, new KeyboardFactory(SWTKeyboardStrategy.class).strategyClass);
	}

	@Test
	public void createsKeyboardForMockKeyboardStrategy() throws Exception {
		assertEquals(MockKeyboardStrategy.class, new KeyboardFactory(MockKeyboardStrategy.class).strategyClass);
	}

	@Override
	protected void createUI(Composite parent) {
		
	}

}
