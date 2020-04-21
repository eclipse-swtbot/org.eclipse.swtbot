/*******************************************************************************
 * Copyright (c) 2012, 2019 Lorenzo Bettini and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Lorenzo Bettini - initial API and implementation
 *     Stephane Bouchet (Intel Corporation) - added testCase for bug 451547
 *     Patrick Tasse - Speed up SWTBot tests
 *     Aparna Argade - TestCase for StatusLine (Bug 544633)
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.widgets;

import java.util.List;
import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertContains;
import static org.junit.Assert.assertEquals;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.helpers.NewJavaClass;
import org.eclipse.swtbot.eclipse.finder.widgets.helpers.NewJavaProject;
import org.eclipse.swtbot.eclipse.finder.widgets.helpers.PackageExplorerView;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Lorenzo Bettini &lt;bettini [at] dsi [dot] unifi [dot] it&gt;
 * @version $Id$
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class SWTBotEclipseProjectTest extends AbstractSWTBotEclipseTest {

	private static final String		PROJECT_NAME		= "FooBarProject";
	private static final String		PACKAGE_NAME		= "org.eclipse.swtbot.eclipse.test";
	private static final String		CLASS_NAME			= "HelloWorld";
	private static final String		CLASS_FILE_NAME		= CLASS_NAME + ".java";

	private NewJavaClass			javaClass			= new NewJavaClass();
	private NewJavaProject			javaProject			= new NewJavaProject();
	private PackageExplorerView		packageExplorerView	= new PackageExplorerView();
	private static SWTWorkbenchBot	bot					= new SWTWorkbenchBot();

	@Before
	public void setUp() throws Exception {
		javaProject.createProject(PROJECT_NAME);
		javaClass.createClass(PACKAGE_NAME, CLASS_NAME);
	}

	@Override
	@After
	public void tearDown() throws Exception {
		bot.saveAllEditors();
		packageExplorerView.deleteProject(PROJECT_NAME);
		super.tearDown();
	}

	@Test
	public void canRefreshProject() {
		packageExplorerTree().expandNode(PROJECT_NAME, "src")
				.contextMenu("Refresh").click();
	}

	@Test
	public void canAccessOpen() {
		javaClassFileTreeItem().contextMenu("Open").click();
	}

	@Test
	public void canAccessContextMenuWithSubmenus() {
		javaClassFileTreeItem().contextMenu("Open With");
	}
	
	@Test
	public void canAccessContextMenuCopyQualifiedName() {
		javaClassFileTreeItem().contextMenu("Copy Qualified Name");
	}
	
	@Test
	public void canAccessContextMenuSubmenu() {
		SWTBotMenu openWithMenu = javaClassFileTreeItem().contextMenu("Open With");
		openWithMenu.menu("Text Editor").click();
	}
	
	@Test
	public void canAccessContextMenuOnMultipleSelection() {
		NewJavaClass javaClass2 = new NewJavaClass();
		javaClass2.createClass("org.eclipse.swtbot.eclipse.test.other", CLASS_NAME);
		SWTBotTreeItem javaClass2item = packageExplorerTree().expandNode(PROJECT_NAME, "src", "org.eclipse.swtbot.eclipse.test.other",
				CLASS_FILE_NAME);
		packageExplorerTree().select(javaClassFileTreeItem(), javaClass2item);
		packageExplorerTree().contextMenu("Compare With").menu("Each Other").click();
	}

	@Test
	public void canGetStatusMessageOfView() {
		packageExplorerTree().select(PROJECT_NAME);
		List<String> lstMsgs = bot.viewByTitle("Package Explorer").getStatusLineMessages();
		assertEquals(1, lstMsgs.size());
		assertContains(PROJECT_NAME, lstMsgs.get(0));
	}

	private SWTBotTreeItem javaClassFileTreeItem() {
		return packageExplorerTree().expandNode(PROJECT_NAME, "src",
				PACKAGE_NAME, CLASS_FILE_NAME);
	}

	private SWTBotTree packageExplorerTree() {
		return bot.viewByTitle("Package Explorer").bot().tree();
	}
}
