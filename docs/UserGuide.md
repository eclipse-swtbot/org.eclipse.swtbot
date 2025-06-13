SWTBot/UsersGuide
=================

Contents
--------

*   [1 SWTBot Users Guide](#SWTBot-Users-Guide)
    *   [1.1 Introduction](#Introduction)
    *   [1.2 Quick Start](#Quick-Start)
        *   [1.2.1 A Screencast](#A-Screencast)
        *   [1.2.2 Creating A Project](#Creating-A-Project)
        *   [1.2.3 Configuration](#Configuration)
        *   [1.2.4 Getting started with SWTBot](#Getting-started-with-SWTBot)
        *   [1.2.5 Getting started with SWTBot for Eclipse Plugins](#Getting-started-with-SWTBot-for-Eclipse-Plugins)
        *   [1.2.6 Executing SWTBot Tests for Eclipse Plugins](#Executing-SWTBot-Tests-for-Eclipse-Plugins)
*   [2 GEF/GMF-based editor testing](#GEF.2FGMF-based-editor-testing)
    *   [2.1 Intro](#Intro)
    *   [2.2 Configuration](#Configuration-2)
    *   [2.3 Getting started with examples](#Getting-started-with-examples)
    *   [2.4 General principles](#General-principles)
        *   [2.4.1 Creation of elements](#Creation-of-elements)
        *   [2.4.2 Direct edition of editParts](#Direct-edition-of-editParts)
        *   [2.4.3 Perform a drag'n'drop](#Perform-a-drag.27n.27drop)
        *   [2.4.4 Mix it with GEF/GMF to perform checks](#Mix-it-with-GEF.2FGMF-to-perform-checks)
*   [3 Recorder and Generator](#Recorder-and-Generator)
*   [4 Migration to SLF4J logging](#Migration-to-SLF4J-logging)

SWTBot Users Guide
==================

Information on this page may be outdated.

Note that this page is for first time users. [Advanced Users](/SWTBot/AdvancedUsers "SWTBot/AdvancedUsers") click here.

Introduction
------------

SWTBot is an open-source Java based functional testing tool for testing SWT and Eclipse based applications.

SWTBot provides APIs that are simple to read and write. The APIs also hide the complexities involved with SWT and Eclipse. This makes it suitable for functional testing by everyone. SWTBot also provides its own set of assertions that are useful for SWT. You can also use your own assertion framework with SWTBot.

SWTBot can record and playback tests and integrates with Eclipse, and also provides for ant tasks so that you can run your builds from within CruiseControl or any other CI tool that you use.

SWTBot can run on all platforms that SWT runs on. Very few other testing tools provide such a wide variety of platforms.

Quick Start
-----------

### A Screencast

Videos speak louder than pictures and words put together:

*   [A 5 minute quick quick tutorial on how to get started with swtbot](http://download.eclipse.org/technology/swtbot/docs/videos/beginners/SWTBotGettingStartedIn5Minutes)
*   [Running SWTBot tests from the command line](http://download.eclipse.org/technology/swtbot/docs/videos/beginners/SWTBotHeadlessTestingForNovices)

### Creating A Project

Create a new project by clicking on **File>New>Project**. On the **New Project Dialog**, search for "plug-in", select **New Plug-in Project** and click **Next**. Create a new plugin project named **org.eclipsecon.swtbot.example**.

![Swtbot-create-project.gif](https://raw.githubusercontent.com/eclipse-swtbot/org.eclipse.swtbot/master/docs/images/Swtbot-create-project.gif)

### Configuration

*   Add the following to your classpath:

  org.eclipse.ui
  org.eclipse.swtbot.eclipse.finder
  org.eclipse.swtbot.junit4_x
  org.hamcrest.core
  org.junit
  org.apache.log4j
  

![Swtbot-setup-dependencies.gif](https://raw.githubusercontent.com/eclipse-swtbot/org.eclipse.swtbot/master/docs/images/Swtbot-setup-dependencies.gif)

### Getting started with SWTBot

SWTBot requires that tests run on a non-UI thread, so that **PlatformUI.getWorkbench()** will return you null and that traditional unit-test code won't work. If you run tests on the UI thread, they will eventually block the UI at some point in time. Take a look into the FAQ for [explanations](/SWTBot./FAQ#Why_do_tests_run_on_a_non-UI_thread..md "SWTBot./FAQ").md and [workaround](/SWTBot./FAQ#How_do_I_execute_parts_of_tests_that_need_UI_thread..md "SWTBot./FAQ")..md 
### Getting started with SWTBot for Eclipse Plugins

To use SWTBot along with your eclipse plugin application, add the plugin dependencies described above to your dependencies. You can download the example from the SWTBot download site [http://download.eclipse.org/technology/swtbot/docs/eclipsecon2009/examples.zip](http://download.eclipse.org/technology/swtbot/docs/eclipsecon2009/examples.zip).

Below you can find a sample SWTBot testcase:

    package org.eclipsecon.swtbot.example;
     
    import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
    import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
    import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
    import org.junit.AfterClass;
    import org.junit.BeforeClass;
    import org.junit.Test;
    import org.junit.runner.RunWith;
     
    @RunWith(SWTBotJunit4ClassRunner.class)
    public class MyFirstTest {
     
    	private static SWTWorkbenchBot	bot;
     
    	@BeforeClass
    	public static void beforeClass() throws Exception {
    		bot = new SWTWorkbenchBot();
    		bot.viewByTitle("Welcome").close();
    	}
     
     
    	@Test
    	public void canCreateANewJavaProject() throws Exception {
    		bot.menu("File").menu("New").menu("Project...").click();
     
    		SWTBotShell shell = bot.shell("New Project");
    		shell.activate();
    		bot.tree().expandNode("Java").select("Java Project");
    		bot.button("Next >").click();
     
    		bot.textWithLabel("Project name:").setText("MyFirstProject");
     
    		bot.button("Finish").click();
    		// FIXME: assert that the project is actually created, for later
    	}
     
     
    	@AfterClass
    	public static void sleep() {
    		bot.sleep(2000);
    	}
     
    }

### Executing SWTBot Tests for Eclipse Plugins

Now that you've written the great test that you'd always wanted to, lets now see it run. In order to run the test, right click on the test and select **Run As > SWTBot Test**

![Run-as-option.jpg](https://raw.githubusercontent.com/eclipse-swtbot/org.eclipse.swtbot/master/docs/images/Run-as-option.jpg)

Select the application that you want to test

![Run-as-config-options.jpg](https://raw.githubusercontent.com/eclipse-swtbot/org.eclipse.swtbot/master/docs/images/Run-as-config-options.jpg)

GEF/GMF-based editor testing
============================

Intro
-----

SWT has a plugin that allows to manipulate GEF/GMF diagrams, editors and editParts as easily as you can manipulate SWT widgets with SWTBot. Then you can easily create some repeatable user-level UI interations and check their effects on the diagram

Configuration
-------------

The configuration is similar to the one describe before for SWTBot, except that you also have to add **org.eclipse.swtbot.eclipse.gef.finder** plugin and some other dependencies, such as **org.eclipse.ui**. In most case, you'll also like to use GEF and/or GMF plugins to make some checks on diagram.

Getting started with examples
-----------------------------

If you like to get started with working examples, you can take a look at the following URL, or [check them out](/SWTBot/Contributing#Getting_the_source "SWTBot/Contributing") in your workspace.

Example GEF project: [http://git.eclipse.org/c/swtbot/org.eclipse.swtbot.git/tree/examples/gef/org.eclipse.gef.examples.logic](http://git.eclipse.org/c/swtbot/org.eclipse.swtbot.git/tree/examples/gef/org.eclipse.gef.examples.logic)  
Example SWTBot for GEF test case: [http://git.eclipse.org/c/swtbot/org.eclipse.swtbot.git/tree/examples/gef/org.eclipse.gef.examples.logic.test](http://git.eclipse.org/c/swtbot/org.eclipse.swtbot.git/tree/examples/gef/org.eclipse.gef.examples.logic.test)

General principles
------------------

Everything is almost the same as using SWTBot, except that some classes change in order to give you the ability to manipulate DiagramEditors. The SWTBotTestCase superclass must be replaced by **SWTBotGefTestCase**. From the inside of your SWTBotTestCase, you can access your **SWTGefBot** _bot_ field to play with your GEF editor. Then you retrieve a **SWTBotGefEditor** by using _bot.getEditor("label of my editor tab")_.

Once you have your SWTBotGefEditor, you can perform high level user operations programatically:

### Creation of elements

 

    	// retrieve editor
    	SWTBotGefEditor editor = bot.gefEditor("test.logic"); // editor must be already open
    	// Simulate creation of element from palette
    	editor.activateTool("Circuit");  // "Circuit" is the label of the tool in palette
    	editor.mouseDrag(55, 55, 150, 100);
    	editor.activateTool("Circuit");
    	editor.mouseMoveLeftClick(150, 150);
    	editor.activateTool("Connection");
    	editor.mouseMoveLeftClick(150, 150);
    	editor.mouseMoveLeftClick(55, 55);

### Direct edition of editParts

 

    	SWTBotGefEditPart editPart = editor.getEditPart("edit part label"); // select edit part by label
    	editPart.click();
    	editor.directEditType("new edit part label");

### Perform a drag'n'drop

 

    	editor.mouseDrag(fromXPosition, fromYPosition, toXPosition, toYPosition)

### Mix it with GEF/GMF to perform checks

TODO: create an example that ensure that creation of a specific element is not possible on mainEditPart

Recorder and Generator
======================

SWTBot comes with a tool that generates some code based on events performed at runtime. It makes writing tests easier and faster. See [SWTBot/Generator](/SWTBot/Generator "SWTBot/Generator").

Migration to SLF4J logging
==========================

SWTBot 4.0.0 has removed its use of org.apache.log4j and is instead using the org.slf4j.api facade for its logging. See [SWTBot/MigrationToSLF4J](/SWTBot/MigrationToSLF4J "SWTBot/MigrationToSLF4J")

