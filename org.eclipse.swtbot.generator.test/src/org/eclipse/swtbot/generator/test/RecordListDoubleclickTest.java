/*******************************************************************************
 * Copyright (c) 2018 Cadence Design Systems, Inc..
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
import org.eclipse.swt.widgets.List;
import org.junit.Assert;
import org.junit.Test;

public class RecordListDoubleclickTest extends AbstractGeneratorTest {

	@Override
	protected void contributeToDialog(Composite container) {
		List list = new List(container, SWT.BORDER | SWT.MULTI);
		list.add("item0");
		list.add("item0");
		list.add("item1");
		list.add("item2");
	}

	@Test
	public void testTableDoubleClick() {
		this.bot.list().doubleClick("item1"); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals("bot.list().select(\"item1\");\nbot.list().doubleClick(\"item1\");", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

	@Test
	public void testTableDoubleClickSameItem() {
		this.bot.list().doubleClick("item0"); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals("bot.list().select(0);\nbot.list().doubleClick(0);", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

	@Test
	public void testTableDoubleClickSameItem2() {
		this.bot.list().doubleClick(1); // $NON-NLS-1$
		flushEvents();
		Assert.assertEquals("bot.list().select(1);\nbot.list().doubleClick(1);", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

}
