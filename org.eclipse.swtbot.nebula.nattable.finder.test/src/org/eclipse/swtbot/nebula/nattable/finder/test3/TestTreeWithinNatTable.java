/*******************************************************************************
 * Copyright (c) 2017 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Aparna Argade(Cadence Design Systems, Inc.) - Bug 516325
 *******************************************************************************/
package org.eclipse.swtbot.nebula.nattable.finder.test3;

import static org.junit.Assert.assertEquals;
import org.eclipse.swtbot.nebula.nattable.finder.widgets.Position;
import org.eclipse.swtbot.nebula.nattable.finder.widgets.SWTBotNatTable;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class TestTreeWithinNatTable extends TreeGridExample {
	private Position POSITION = new Position(1,1);

	/* Tests collaspeAll and expandAll */
	@Test
	public void testCollapseExpandTree() {
		SWTBotNatTable nattable = bot.nattable();
		nattable.collapseAll(new Position(1,1));
		int count = nattable.preferredRowCount();
		assertEquals("After collapseAll, 3 rows", 3, count);
		assertEquals("Cell data of 1,1", "root", nattable.getCellDataValueByPosition(1, 1));
		assertEquals("Cell data of 2,1", "root2", nattable.getCellDataValueByPosition(2, 1));

		nattable.expandAll(new Position(1,1));
		count = nattable.preferredRowCount();
		assertEquals("After expandAll, 49 rows", 49, count);
		assertEquals("Cell data of 2,1", "C", nattable.getCellDataValueByPosition(2, 1));
		assertEquals("Cell data of 3,1", "C.5", nattable.getCellDataValueByPosition(3, 1));
	}

	/* Tests expandTreeRow and collapseTreeRow */
	@Test
	public void testCollapseExpandTreeRow() {
		SWTBotNatTable nattable = bot.nattable();
		nattable.collapseAll(new Position(1,1));
		int count = nattable.preferredRowCount();
		assertEquals("After collapseAll, 3 rows", 3, count);

		nattable.expandTreeRow(POSITION, 1);
		count = nattable.preferredRowCount();
		assertEquals("After expandTreeRow, 16 rows", 16, count);

		nattable.expandAll(POSITION);
		count = nattable.preferredRowCount();
		assertEquals("After expandAll, 49 rows", 49, count);

		nattable.collapseTreeRow(POSITION, 24);
		count = nattable.preferredRowCount();
		assertEquals("After collapseTreeRow, 26 rows", 26, count);
	}

	/* Tests expandTreeRowToLevel */
	@Test
	public void testexpandTreeRowToLevel() {
		SWTBotNatTable nattable = bot.nattable();
		nattable.collapseAll(POSITION);

		//level 1
		nattable.expandTreeRowToLevel(POSITION, 0, 1);
		int count = nattable.preferredRowCount();
		assertEquals("After expandTreeRowToLevel 1", 16, count);
		assertEquals("Cell data of 2,1", "C", nattable.getCellDataValueByPosition(2, 1));

		//level 2
		nattable.expandTreeRowToLevel(POSITION, 1, 2);
		assertEquals("Cell data of 3,1", "C.5", bot.nattable().getCellDataValueByPosition(3, 1));
		count = nattable.preferredRowCount();
		assertEquals("After expandTreeRowToLevel 2", 17, count);
	}

	/* Tests expandAllToLevel */
	@Test
	public void testExpandAllToLevel() {
		SWTBotNatTable nattable = bot.nattable();
		nattable.collapseAll(POSITION);

		//level 1
		nattable.expandAllToLevel(POSITION, 1);
		int count = nattable.preferredRowCount();
		assertEquals("After expandAllToLevel 1", 29, count);

		//level 2
		nattable.collapseAll(POSITION);
		nattable.expandAllToLevel(POSITION, 2);
		count = nattable.preferredRowCount();
		assertEquals("After expandAllToLevel 2", 49, count);
	}

}
