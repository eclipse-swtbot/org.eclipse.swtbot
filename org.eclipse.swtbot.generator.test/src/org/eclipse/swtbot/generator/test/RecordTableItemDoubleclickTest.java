/*******************************************************************************
 * Copyright (c) 2018 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

public class RecordTableItemDoubleclickTest extends AbstractGeneratorTest {

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
	public void testTableDoubleClick() {
		this.bot.table().getTableItem("item1").doubleClick(); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals("bot.table().select(\"item1\");\nbot.table().getTableItem(\"item1\").doubleClick();", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

	@Test
	public void testTableDoubleClickSameItem() {
		this.bot.table().getTableItem("item3").doubleClick(); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals("bot.table().select(2);\nbot.table().getTableItem(2).doubleClick();", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

	@Test
	public void testTableDoubleClickSameItem2() {
		this.bot.table().getTableItem(3).doubleClick(); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals("bot.table().select(3);\nbot.table().getTableItem(3).doubleClick();", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

}
