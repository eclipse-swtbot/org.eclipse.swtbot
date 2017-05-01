/*******************************************************************************
 * Copyright (c) 2013, 2017 Rohit Agrawal and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Rohit Agrawal - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import org.eclipse.swt.examples.dnd.DNDExample;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtbot.swt.finder.test.AbstractSWTShellTest;
import org.junit.Assert;
import org.junit.Test;

public class DnDTreeTest extends AbstractSWTShellTest {

	@Override
	protected void createUI(Composite parent) {
		new DNDExample().open(shell);
	}

	@Test
	public void dragsAndDropsFromTreeToTree() throws Exception {
		bot.comboBoxInGroup("Widget", 0).setSelection("Tree");
		bot.comboBoxInGroup("Widget", 1).setSelection("Tree");
		final SWTBotTree sourceTree = bot.tree(0);
		final SWTBotTree targetTree = bot.tree(1);
		final SWTBotTreeItem sourceItem = sourceTree.getTreeItem("Drag Source name 0");
		final SWTBotTreeItem targetItem = targetTree.getTreeItem("Drop Target name 0");

		sourceItem.dragAndDrop(targetItem);

		for (SWTBotTreeItem item : sourceTree.getAllItems()) {
			if (item.getText().equals("Drag Source name 0")) {
				Assert.fail("Drag Source item should have disappeared from source tree");
			}
		}
		SWTBotTreeItem droppedNode = targetItem.expand().getNode("Drag Source name 0");
		Assert.assertNotNull("Could not find dropped node", droppedNode);
	}
}
