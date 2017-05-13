/*******************************************************************************
 * Copyright (c) 2012, 2017 Lorenzo Bettini and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Lorenzo Bettini - initial API and implementation
 *     Stephane Bouchet (Intel Corporation) - added testCase for bug 451547
 *     Patrick Tasse - Speed up SWTBot tests
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.widgets;

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

	private SWTBotTreeItem javaClassFileTreeItem() {
		return packageExplorerTree().expandNode(PROJECT_NAME, "src",
				PACKAGE_NAME, CLASS_FILE_NAME);
	}

	private SWTBotTree packageExplorerTree() {
		return bot.viewByTitle("Package Explorer").bot().tree();
	}
}
