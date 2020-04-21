/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertSameWidget;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swtbot.swt.finder.finders.ControlFinder;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.hamcrest.Matcher;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotTabItemTest extends AbstractControlExampleTest {

	@Test
	public void findsTabs() throws Exception {
		bot.shell("SWT Controls").activate();
		final SWTBotTabItem tabItem = bot.tabItem("Sash");
		assertEquals("Sash", tabItem.getText());
		bot.tabItem("Button").activate();
	}

	@Test
	public void activatesTabItem() throws Exception {
		bot.shell("SWT Controls").activate();
		SWTBotTabItem tabItem = bot.tabItem("Sash");
		Matcher<TabItem> withText = withText("Sash");
		List<TabItem> findControls = new ControlFinder().findControls(allOf(widgetOfType(TabItem.class), withText));
		assertSameWidget(findControls.get(0), tabItem.widget);
	}
}
