/*******************************************************************************
 * Copyright (c) 2014, 2017 Red Hat Inc.  and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Mickael Istria (Red Hat Inc.) - initial API and implementation
 *    Aparna Argade - Bug 510835
 *******************************************************************************/
package org.eclipse.swtbot.generator.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.junit.Assert;
import org.junit.Test;

public class RecordListSelectionTest extends AbstractGeneratorTest {

	@Override
	protected void contributeToDialog(Composite container) {
		List list = new List(container, SWT.MULTI);
		list.add("item1"); //$NON-NLS-1$
		list.add("item2"); //$NON-NLS-1$
		list.add("item3"); //$NON-NLS-1$
		list.add("item3"); //$NON-NLS-1$
	}

	@Test
	public void testSingleListSelection() {
		this.bot.list().select("item2"); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals(
				"bot.list().select(\"item2\");", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

	@Test
	public void testMultipleListSelectionDifferentItems() {
		this.bot.list().select("item1", "item2"); //$NON-NLS-1$ //$NON-NLS-2$
		flushEvents();
		Assert.assertEquals(
				"bot.list().select(\"item1\");\nbot.list().select(\"item1\", \"item2\");", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

	@Test
	public void testMultipleListSelectionSameItem() {
		this.bot.list().select(2, 3);
		flushEvents();
		Assert.assertEquals(
				"bot.list().select(2);\nbot.list().select(2, 3);", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

	@Test
	public void testMultipleListSelectionSameItem2() {
		this.bot.list().select("item1", "item3"); //$NON-NLS-1$ //$NON-NLS-2$
		flushEvents();
		Assert.assertEquals(
				"bot.list().select(\"item1\");\nbot.list().select(0, 2);", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

}
