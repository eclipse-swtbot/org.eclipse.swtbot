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
package org.rcpmail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(SWTBotJunit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageCreateTest {
	private static SWTWorkbenchBot	bot	= new SWTWorkbenchBot();

	@Test
	public void test1_CreatesAnotherMessageWindow() throws Exception {
		assertEquals(2, viewCount());
		bot.menu("File").menu("Open Another Message View").click();

		assertEquals(3, viewCount());
	}

	@Test
	public void test2_ClosesAllMessageWindows() throws Exception {
		bot.viewByTitle("Message").close();
		bot.viewByTitle("Message").close();

		assertEquals(1, viewCount());
	}

	@Test
	public void test3_MyMailBoxContainsDrafts() throws Exception {
		SWTBotTree mailbox = mailBox();
		SWTBotTreeItem myMailBox = mailbox.expandNode("me@this.com");
		assertTrue(myMailBox.getNodes().contains("Drafts"));
	}

	@Test
	public void test4_OtherMailBoxContainsDrafts() throws Exception {
		SWTBotTree mailbox = mailBox();
		SWTBotTreeItem otherMailBox = mailbox.expandNode("other@aol.com");
		assertTrue(otherMailBox.getNodes().contains("Inbox"));
	}

	private SWTBotTree mailBox() throws WidgetNotFoundException {
		// find the tree
		return bot.viewByTitle("Mailboxes").bot().tree();
	}

	private int viewCount() throws WidgetNotFoundException {
		return bot.views().size();
	}

}
