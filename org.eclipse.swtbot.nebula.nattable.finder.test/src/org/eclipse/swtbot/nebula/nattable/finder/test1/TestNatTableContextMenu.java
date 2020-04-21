/*******************************************************************************
 * Copyright (c) 2016 Cadence Design Systems, Inc. and others.
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
package org.eclipse.swtbot.nebula.nattable.finder.test1;

import static org.eclipse.swtbot.swt.finder.utils.SWTUtils.getTextPath;
import static org.junit.Assert.assertArrayEquals;

import org.eclipse.swtbot.nebula.nattable.finder.widgets.SWTBotNatTable;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class TestNatTableContextMenu extends _5015_AutomaticDataSpanningExample {

	@Test
	public void testContextMenu() {
		SWTBotNatTable nattable = bot.nattable();
		SWTBotMenu menuItem;
		menuItem = nattable.contextMenu("Toggle auto spanning").hide();
		assertArrayEquals(new String[] { "POP_UP", "Toggle auto spanning" }, getTextPath(menuItem.widget));
		menuItem = nattable.contextMenu(0, 0).menu("Toggle auto spanning").hide();
		assertArrayEquals(new String[] { "POP_UP", "Toggle auto spanning" }, getTextPath(menuItem.widget));
	}
}
