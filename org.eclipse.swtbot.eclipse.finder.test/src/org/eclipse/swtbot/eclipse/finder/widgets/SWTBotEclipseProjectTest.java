/*******************************************************************************
 * Copyright (c) 2012 Lorenzo Bettini.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Lorenzo Bettini - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.widgets;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.helpers.NewJavaClass;
import org.eclipse.swtbot.eclipse.finder.widgets.helpers.NewJavaProject;
import org.eclipse.swtbot.eclipse.finder.widgets.helpers.PackageExplorerView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Lorenzo Bettini &lt;bettini [at] dsi [dot] unifi [dot] it&gt;
 * @version $Id$
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class SWTBotEclipseProjectTest {

	private static final String		PROJECT_NAME		= "FooBarProject";
	private static final String		PACKAGE_NAME		= "org.eclipse.swtbot.eclipse.test";
	private static final String		CLASS_NAME			= "HelloWorld";
	private static final String		CLASS_FILE_NAME		= CLASS_NAME + ".java";

	private NewJavaClass			javaClass			= new NewJavaClass();
	private NewJavaProject			javaProject			= new NewJavaProject();
	private PackageExplorerView		packageExplorerView	= new PackageExplorerView();
	private static SWTWorkbenchBot	bot					= new SWTWorkbenchBot();

	@BeforeClass
	public static void beforeClass() {
		closeWelcomePage();
	}

	@Before
	public void setUp() throws Exception {
		javaProject.createProject(PROJECT_NAME);
		javaClass.createClass(PACKAGE_NAME, CLASS_NAME);
	}

	private static void closeWelcomePage() {
		try {
			System.setProperty("org.eclipse.swtbot.search.timeout", "0");
			bot.viewByTitle("Welcome").close();
		} catch (WidgetNotFoundException e) {
			// do nothing
		}finally{
			System.setProperty("org.eclipse.swtbot.search.timeout", "5000");
		}
	}

	@After
	public void tearDown() throws Exception {
		saveAndCloseAllEditors();
		packageExplorerView.deleteProject(PROJECT_NAME);
	}

	/**
	 * @throws WidgetNotFoundException
	 */
	private void saveAndCloseAllEditors() {
		List<? extends SWTBotEditor> editors = bot.editors();
		for (SWTBotEditor editor : editors) {
			editor.saveAndClose();
		}
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
		// this fails in e4 with current implementation of
		// SWTBotTreeItem.contextMenu
		// since "Open With" has submenus
		javaClassFileTreeItem().contextMenu("Open With");
	}
	
	@Test
	public void canAccessContextMenuCopyQualifiedName() {
		javaClassFileTreeItem().contextMenu("Copy Qualified Name");
	}
	
	@Test
	public void canAccessContextMenuSubmenu() {
		// this fails in e4 with current implementation of
		// SWTBotTreeItem.contextMenu
		// since "Open With" has submenus
		SWTBotMenu openWithMenu = javaClassFileTreeItem().contextMenu("Open With");
		openWithMenu.menu("Text Editor").click();
	}

	private SWTBotTreeItem javaClassFileTreeItem() {
		return packageExplorerTree().expandNode(PROJECT_NAME, "src",
				PACKAGE_NAME, CLASS_FILE_NAME);
	}

	private SWTBotTree packageExplorerTree() {
		return bot.viewByTitle("Package Explorer").bot().tree();
	}
}
