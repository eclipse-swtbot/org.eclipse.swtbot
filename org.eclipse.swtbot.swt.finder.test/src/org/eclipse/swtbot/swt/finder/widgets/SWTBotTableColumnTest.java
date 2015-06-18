/*******************************************************************************
 * Copyright (c) 2008, 2015 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Patrick Tasse - Add column header context menu test
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertText;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertTextContains;
import static org.junit.Assert.assertEquals;

import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotTableColumnTest extends AbstractControlExampleTest {

	private SWTBotTable	 table;

	@Test
	public void findsTableColumn() throws Exception {
		SWTBotTableColumn header = table.header("Name");
		assertText("Name", header.widget);
		assertEquals(TableColumn.class, header.widget.getClass());
	}

	@Test
	public void clicksTableColumn() throws Exception {
		SWTBotTableColumn header = table.header("Name");
		header.click();

		Text text = bot.textInGroup("Listeners").widget;

		assertTextContains("Selection [13]: SelectionEvent{TableColumn {Name}", text);
		assertTextContains("MouseUp [4]: MouseEvent{Table {}", text);
		assertTextContains("data=null button=1 stateMask=" + toStateMask(524288, table.widget) + " x=0 y=0 count=1}", text);
	}

	@Test
	public void clicksHeaderContextMenuItem() throws Exception {
		Text text = bot.textInGroup("Listeners").widget;

		SWTBotTableColumn header = table.header("Name");
		header.contextMenu("Get Column Header Text").click();
		assertTextContains("Get Column Header Text returned: Name", text);

		header = table.header("Type");
		header.contextMenu("Get Column Header Text").click();
		assertTextContains("Get Column Header Text returned: Type", text);

		header = table.header("Size");
		header.contextMenu("Get Column Header Text").click();
		assertTextContains("Get Column Header Text returned: Size", text);

		header = table.header("Modified");
		header.contextMenu("Get Column Header Text").click();
		assertTextContains("Get Column Header Text returned: Modified", text);
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("Table").activate();
		bot.radio("SWT.MULTI").click();
		bot.checkBox("Header Visible").select();
		bot.checkBox("Multiple Columns").select();
		bot.checkBox("Popup Menu").select();
		bot.checkBox("Listen").select();
		bot.button("Clear").click();
		table = bot.tableInGroup("Table");
	}
	
}
