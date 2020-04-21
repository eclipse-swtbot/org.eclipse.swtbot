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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.test.AbstractSWTShellTest;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class ShellClosesTest extends AbstractSWTShellTest {

	@Test
	public void waitsForShellClose() throws Exception {

		long start = System.currentTimeMillis();
		new SWTBot().waitUntil(Conditions.shellCloses(new SWTBotShell(shell)));
		long end = System.currentTimeMillis();

		int time = (int) (end - start);
		assertThat(time, allOf(lessThan(5000), greaterThan(200)));
	}

	@Override
	protected void createUI(Composite parent) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(200);
				} catch (InterruptedException niceTry) {
				}
				new SWTBotShell(shell).close();
			}
		}).start();
	}
}
