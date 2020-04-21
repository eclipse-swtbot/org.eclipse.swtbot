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
package org.eclipse.swtbot.swt.finder.waits;

import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.test.AbstractSWTShellTest;
import org.hamcrest.core.AllOf;
import org.hamcrest.number.OrderingComparison;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class ShellIsActiveTest extends AbstractSWTShellTest {

	@Test
	public void waitsForShellToBecomeActive() throws Exception {

		long start = System.currentTimeMillis();
		new SWTBot().waitUntil(Conditions.shellIsActive(SHELL_TEXT));
		long end = System.currentTimeMillis();

		int time = (int) (end - start);
		assertThat(time, AllOf.allOf(OrderingComparison.lessThan(200), OrderingComparison.greaterThanOrEqualTo(0)));
	}

	@Override
	protected void createUI(Composite parent) {
	}

}
