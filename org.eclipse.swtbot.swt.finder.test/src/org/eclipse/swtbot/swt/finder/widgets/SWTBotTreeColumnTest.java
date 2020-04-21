/*******************************************************************************
 * Copyright (c) 2013, 2017 Robin Stocker and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Robin Stocker - initial implementation
 *     Patrick Tasse - Add column header context menu test
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertText;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertTextContains;
import static org.junit.Assert.assertEquals;

import org.eclipse.swt.SWT;
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

		SWTBotText text = bot.textInGroup("Listeners");

		assertTextContains("Selection [13]: SelectionEvent{TreeColumn {Name}", text);
		assertEventMatches(text, "MouseUp [4]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=" + toStateMask(SWT.BUTTON1, tree.widget) + " x=0 y=0 count=1}");
	}

	@Test
	public void clicksHeaderContextMenuItem() throws Exception {
		Text text = bot.textInGroup("Listeners").widget;

		SWTBotTreeColumn header = tree.header("Name");
		header.contextMenu("Get Column Header Text").click();
		assertTextContains("Get Column Header Text returned: Name", text);

		header = tree.header("Type");
		header.contextMenu("Get Column Header Text").click();
		assertTextContains("Get Column Header Text returned: Type", text);

		header = tree.header("Size");
		header.contextMenu("Get Column Header Text").click();
		assertTextContains("Get Column Header Text returned: Size", text);

		header = tree.header("Modified");
		header.contextMenu("Get Column Header Text").click();
		assertTextContains("Get Column Header Text returned: Modified", text);
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("Tree").activate();
		bot.radio("SWT.MULTI").click();
		bot.checkBox("Header Visible").select();
		bot.checkBox("Multiple Columns").select();
		bot.checkBox("Popup Menu").select();
		bot.checkBox("Listen").select();
		bot.button("Clear").click();
		tree = bot.treeInGroup("Tree");
	}

}
