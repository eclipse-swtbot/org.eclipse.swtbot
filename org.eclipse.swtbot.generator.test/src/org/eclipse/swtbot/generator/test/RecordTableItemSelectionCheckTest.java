/*******************************************************************************
 * Copyright (c) 2018 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Aparna Argade - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.junit.Assert;
import org.junit.Test;

public class RecordTableItemSelectionCheckTest extends AbstractGeneratorTest {

	@Override
	protected void contributeToDialog(Composite container) {
		Table table = new Table(container, SWT.MULTI | SWT.CHECK);
		for (int i = 1; i <= 4; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			if (i != 4)
				item.setText(0, "item" + Integer.toString(i)); //$NON-NLS-1$
			else
				item.setText(0, "item" + Integer.toString(i - 1)); //$NON-NLS-1$
		}
	}

	@Test
	public void testSingleTableSelection() {
		this.bot.table().select("item2"); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals("bot.table().select(\"item2\");", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

	@Test
	public void testMultipleTableSelectionDifferentItems() {
		this.bot.table().select("item1", "item2"); //$NON-NLS-1$ //$NON-NLS-2$
		flushEvents();
		Assert.assertEquals("bot.table().select(\"item1\");\nbot.table().select(\"item1\", \"item2\");", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

	@Test
	public void testMultipleTableSelectionSameItem() {
		this.bot.table().select(2, 3);
		flushEvents();
		Assert.assertEquals("bot.table().select(2);\nbot.table().select(2, 3);", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

	@Test
	public void testMultipleTableSelectionSameItem2() {
		this.bot.table().select("item1", "item3"); //$NON-NLS-1$ //$NON-NLS-2$
		flushEvents();
		Assert.assertEquals("bot.table().select(\"item1\");\nbot.table().select(0, 2);", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

	@Test
	public void testTableCheck() {
		this.bot.table().getTableItem("item2").toggleCheck(); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals("bot.table().getTableItem(\"item2\").toggleCheck();", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

	@Test
	public void testTableCheckSameItem() {
		this.bot.table().getTableItem(3).toggleCheck(); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals(
				"bot.table().getTableItem(3).toggleCheck();", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

	@Test
	public void testTableCheckSameItem2() {
		this.bot.table().getTableItem("item3").toggleCheck(); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals(
				"bot.table().getTableItem(2).toggleCheck();", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

}
