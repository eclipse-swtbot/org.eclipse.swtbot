/*******************************************************************************
 * Copyright (c) 2013 Robin Stocker and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Robin Stocker - initial implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertText;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertTextContains;
import static org.junit.Assert.assertEquals;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.junit.Before;
import org.junit.Test;

public class SWTBotTreeColumnTest extends AbstractControlExampleTest {

	private SWTBotTree tree;

	@Test
	public void findsTreeColumn() throws Exception {
		SWTBotTreeColumn header = tree.header("Name");
		assertText("Name", header.widget);
		assertEquals(TreeColumn.class, header.widget.getClass());
	}

	@Test
	public void clicksTreeColumn() throws Exception {
		SWTBotTreeColumn header = tree.header("Name");
		header.click();

		Text text = bot.textInGroup("Listeners").widget;

		assertTextContains("Selection [13]: SelectionEvent{TreeColumn {Name}", text);
		assertTextContains("MouseUp [4]: MouseEvent{Tree {}", text);
		assertTextContains("data=null button=1 stateMask=" + toStateMask(524288, tree.widget) + " x=0 y=0 count=1}", text);
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("Tree").activate();
		bot.radio("SWT.MULTI").click();
		bot.checkBox("Header Visible").select();
		bot.checkBox("Multiple Columns").select();
		bot.checkBox("Listen").select();
		bot.button("Clear").click();
		tree = bot.treeInGroup("Tree");
	}

}
