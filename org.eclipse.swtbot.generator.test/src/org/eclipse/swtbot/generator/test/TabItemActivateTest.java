/*******************************************************************************
 * Copyright (c) 2014 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Mickael Istria (Red Hat Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.junit.Assert;
import org.junit.Test;

public class TabItemActivateTest extends AbstractGeneratorTest {

	@Override
	protected void contributeToDialog(Composite container) {
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		TabItem item1 = new TabItem(tabFolder, SWT.NONE);
		item1.setText("item1"); //$NON-NLS-1$
		TabItem item2 = new TabItem(tabFolder, SWT.NONE);
		item2.setText("item2"); //$NON-NLS-1$
	}
	
	@Test
	public void testActivateCTabItem() {
		bot.tabItem("item2").activate();
		flushEvents();
		Assert.assertEquals(
			"bot.tabItem(\"item2\").activate();", //$NON-NLS-1$
			recorderShellBot().text().getText().trim());
	}

}
