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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.Assert;
import org.junit.Test;

public class RecordTreeItemDoubleclickTest extends AbstractGeneratorTest {

	@Override
	protected void contributeToDialog(Composite container) {
		Tree tree = new Tree(container, SWT.MULTI | SWT.CHECK);
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText(0, "item0"); //$NON-NLS-1$
		TreeItem itemchild = new TreeItem(item, SWT.NONE);
		itemchild.setText(0, "subitem0"); //$NON-NLS-1$
	}

	@Test
	public void testTreeDoubleClick() {
		this.bot.tree().getTreeItem("item0").doubleClick(); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals(
				"bot.tree().getTreeItem(\"item0\").select();\nbot.tree().getTreeItem(\"item0\").doubleClick();", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

	@Test
	public void testTreeLevel2DoubleClick() {
		this.bot.tree().getTreeItem("item0").getNode("subitem0").doubleClick(); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals(
				"bot.tree().getTreeItem(\"item0\").expand();\nbot.tree().getTreeItem(\"item0\").getNode(\"subitem0\").select();\nbot.tree().getTreeItem(\"item0\").getNode(\"subitem0\").doubleClick();", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

}
