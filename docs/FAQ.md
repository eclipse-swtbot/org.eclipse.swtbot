SWTBot/FAQ
==========

Contents
--------

*   [1 Can SWTBot be used to test RCP Applications?](#Can-SWTBot-be-used-to-test-RCP-Applications.3F)
*   [2 What platforms is SWTBot tested on?](#What-platforms-is-SWTBot-tested-on.3F)
*   [3 How often is SWTBot released?](#How-often-is-SWTBot-released.3F)
*   [4 Why do tests run on a non-UI thread?](#Why-do-tests-run-on-a-non-UI-thread.3F)
*   [5 How do I execute parts of tests that need UI thread?](#How-do-I-execute-parts-of-tests-that-need-UI-thread.3F)
*   [6 Why do I have to run tests as SWTBot tests, instead of PDE-JUnit tests?](#Why-do-I-have-to-run-tests-as-SWTBot-tests.2C-instead-of-PDE-JUnit-tests.3F)
*   [7 Can I slow down the execution speed of SWTBot tests?](#Can-I-slow-down-the-execution-speed-of-SWTBot-tests.3F)
*   [8 Can I change the timeout for execution of SWTBot tests?](#Can-I-change-the-timeout-for-execution-of-SWTBot-tests.3F)
*   [9 Can I change the poll delay for evaluating conditions in SWTBot tests?](#Can-I-change-the-poll-delay-for-evaluating-conditions-in-SWTBot-tests.3F)
*   [10 How do I configure log4j to turn on logging for SWTBot?](#How-do-I-configure-log4j-to-turn-on-logging-for-SWTBot.3F)
*   [11 Does SWTBot support my keyboard layout?](#Does-SWTBot-support-my-keyboard-layout.3F)
*   [12 How do I use SWTBot to test native dialogs (File Dialogs, Color Dialogs, etc)?](#How-do-I-use-SWTBot-to-test-native-dialogs-.28File-Dialogs.2C-Color-Dialogs.2C-etc.29.3F)
*   [13 How do I test a login dialog using SWTBot](#How-do-I-test-a-login-dialog-using-SWTBot)
*   [14 Can I test an exported eclipse product](#Can-I-test-an-exported-eclipse-product)
    *   [14.1 Using p2 to install the SWTBot headless feature](#Using-p2-to-install-the-SWTBot-headless-feature)
    *   [14.2 Copy-paste feature in your product](#Copy-paste-feature-in-your-product)
    *   [14.3 Ship directly your product with SWTBot](#Ship-directly-your-product-with-SWTBot)
*   [15 Can I test a custom swt control with swtbot?](#Can-I-test-a-custom-swt-control-with-swtbot.3F)
*   [16 Can I run SWTBot tests using Hudson/CruiseControl/Whatever?](#Can-I-run-SWTBot-tests-using-Hudson.2FCruiseControl.2FWhatever.3F)
*   [17 Can I get more details on a particular widget?](#Can-I-get-more-details-on-a-particular-widget.3F)
*   [18 Is it possible to have JUnit5 based tests with SWTBot?](#Is-it-possible-to-have-JUnit5-based-tests-with-SWTBot.3F)

### Can SWTBot be used to test RCP Applications?

Yes SWTBot can be used to test **any** kind of SWT apps -- SWT, Eclipse plugins and RCP applications.

### What platforms is SWTBot tested on?

SWTBot can be built and tested on any platform from Eclipse Mars up to latest Eclipse. However, the Continuous Integration jobs test it on Linux, with latest Eclipse.

### How often is SWTBot released?

Yearly, mostly in June Simultaneous Release.

### Why do tests run on a non-UI thread?

A lot of events that SWTBot sends to the UI are blocking. SWT [dialogs](https://help.eclipse.org/ganymede/topic/org.eclipse.platform.doc.isv/reference/api/org/eclipse/jface/dialogs/Dialog.html) are one of them. This means that functions opening dialogs, will block until the dialog closes. Since we do not want tests to block when a dialog open up, SWTBot runs in a non-UI thread, and posts events to the UI thread.

There are two solutions to this:

1.  Make the dialog non-modal, by invoking [Dialog#setBlockOnOpen(false)](https://help.eclipse.org/ganymede/topic/org.eclipse.platform.doc.isv/reference/api/org/eclipse/jface/window/Window.html#setBlockOnOpen(boolean))
2.  Open the dialog in a non-ui thread

SWTBot chooses the later approach, since the first approach is not always practical.

### How do I execute parts of tests that need UI thread?

Since SWTBot runs on a non-UI thread, all your code using **PlatformUI.getWorkbench()** will generally result with a NullPointerException, since PlatformUI.getWorkbench() returns null in non-UI threads.

If you really need to invoke or execute code that needs UI-thread (such as opening an editor without simulating click on New and so on), you can use **Display.getDefault().syncExec()** to wrap the piece of code that requires UI-thread in your tests:

    @Test
    public void testDiagram() throws ExecutionException {
    	// part of test that requires UI-thread
    	Display.getDefault().syncExec(new Runnable() {
    		public void run() {
    			try {
    				new NewProcessCommandHandler().execute(null); // requires UI-thread since it is gonna invoke PlatformUI.getWorkbench()
    			} catch (Exception ex) {
    				ex.printStackTrace();
    			}
    		}
    	});
     
    	// Normal SWTBot execution
    	SWTBotEditor botEditor = bot.activeEditor();
    	SWTBotGefEditor gmfEditor = bot.gefEditor(botEditor.getTitle());
    	gmfEditor.activateTool("Step");
    	gmfEditor.mouseMoveLeftClick(200, 200);
    }

### Why do I have to run tests as SWTBot tests, instead of PDE-JUnit tests?

PDE-Junit tests run on the UI thread. SWTBot needs that tests run on a non-UI thread, hence a new run configuration. See [Why do tests run on a non-UI thread?](#Why-do-tests-run-on-a-non-UI-thread.3F) for more info. You could also run SWTBot test as plain JUnit/PDE tests, just ensure that the "Run in UI Thread" checkbox is not ticked.

### Can I slow down the execution speed of SWTBot tests?

Yes you can! To slow down the speed of execution of SWTBot, you need to set the system property "org.eclipse.swtbot.playback.delay". This delay is in milliseconds. You can also set this property in code as follows:

    // slow down tests
    SWTBotPreferences.PLAYBACK_DELAY = 10;
    // set to the default speed
    SWTBotPreferences.PLAYBACK_DELAY = 0;

### Can I change the timeout for execution of SWTBot tests?

Yes you can! To change the timeout, you need to set the system property "org.eclipse.swtbot.search.timeout". The timeout is specified in milliseconds. You can also set this property in code as follows:

    // increase timeout to 10 seconds
    SWTBotPreferences.TIMEOUT = 10000;
    // set to the default timeout of 5 seconds
    SWTBotPreferences.TIMEOUT = 5000;

### Can I change the poll delay for evaluating conditions in SWTBot tests?

Yes you can! To change the poll delay, you need to set the system property "org.eclipse.swtbot.playback.poll.delay". The poll delay is specified in milliseconds. You can also set this property in code as follows:

    // increase timeout to 1 second
    SWTBotPreferences.DEFAULT_POLL_DELAY = 1000;
    // set to the default timeout of 500ms.
    SWTBotPreferences.DEFAULT_POLL_DELAY = 500;

### How do I configure log4j to turn on logging for SWTBot?

Copy the file [http://git.eclipse.org/c/swtbot/org.eclipse.swtbot.git/tree/org.eclipse.swtbot.swt.finder.test/src/log4j.xml](http://git.eclipse.org/c/swtbot/org.eclipse.swtbot.git/tree/org.eclipse.swtbot.swt.finder.test/src/log4j.xml) to the src directory in your plugin. Ensure that the plugin's MANIFEST.MF contains the following lines, note the dependency on org.apache.log4j

Require-Bundle: org.eclipse.swtbot.go,
 org.apache.log4j
Eclipse-RegisterBuddy: org.apache.log4j

If you still get the message:

log4j:WARN No appenders could be found for logger (net.sf.swtbot.matcher.WidgetMatcherFactory$MenuMatcher).
log4j:WARN Please initialize the log4j system properly.

Then there's something else not right. Drop an email on the [mailing list](/SWTBot/Support "SWTBot/Support").

### Does SWTBot support my keyboard layout?

Yes it does. See [SWTBot/Keyboard_Layouts](/SWTBot/Keyboard_Layouts "SWTBot/Keyboard Layouts") for how to configure SWTBot for your own keyboard layout.

### How do I use SWTBot to test native dialogs (File Dialogs, Color Dialogs, etc)?

You can't! Very unfortunate but true.

SWT does not have support to recognize native dialogs, and SWTBot therefore cannot test them. See [eclipse bug #164191](https://bugs.eclipse.org/bugs/show_bug.cgi?id=164192) for more info. Consider adding a comment and a vote on that bug as well.

### How do I test a login dialog using SWTBot

You can't! The login dialog pops up before SWTBot gets an opportunity to initialize itself. A good workaround is to make the license dialog a bit intelligent so you can bypass it by setting the username and password as a system property. A login dialog like this is a good choice:

    public class LoginDialog {
      ...
      public void open() {
        String username = System.getProperty("com.yourapp.username");
        String password = System.getProperty("com.yourapp.password");
        if (isValid(username, password){
            // the password is good, continue doing whatever ?
        } else {
            // revalidate password ?
        }
      }
      ...
    }

  
You can then start SWTBot tests with the following JVM arguments. You may set these JVM args in the target platform(preferred) or the launch configuration for the SWTBot test.

-Dcom.yourapp.username=joe -Dcom.yourapp.password=secret

### Can I test an exported eclipse product

You can. You need to ensure that some bits of SWTBot are present in your product at the time you are running your tests. SWTBot cannot run tests on a product if there's no agent sitting inside the product :)

Here are 3 ways to have this working, by order of preference:

#### Using p2 to install the SWTBot headless feature

This is the preferred method when your product is p2-ready.

Use the [Equinox p2 director application](/Equinox_p2_director_application "Equinox p2 director application") to install the headless feature, and any other SWTBot modules you need (GEF, Forms...) to your product.

From your product main directory

$ java -jar plugins/org.eclipse.equinox.launcher_*.jar \
-application org.eclipse.equinox.p2.director \
-repository [http://download.eclipse.org/technology/swtbot/galileo/dev-build/update-site/,http://download.eclipse.org/eclipse/updates/3.6](http://download.eclipse.org/technology/swtbot/galileo/dev-build/update-site/,http://download.eclipse.org/eclipse/updates/3.6) \
-installIU org.eclipse.swtbot.eclipse.finder,org.eclipse.swtbot.eclipse.test.junit4.feature.group \
-consoleLog \
-noSplash

The exact IUs to be installed will vary, the above is an example. To get a list of all the IUs you can possibly install, run the above command with just the SWTBot repository and the "-list" option instead of "-installIU".

Installing the IU "org.eclipse.swtbot.eclipse.feature.group", previously recommended here, will install everything you could possibly ever want and probably a lot more besides. Note in particular that it depends on org.eclipse.ui.ide, which when installed will cause various additional menu items, such as "Convert Line Delimiters To..." to be added to your product. This may then make it awkward to use the product for anything other than headless testing.

#### Copy-paste feature in your product

Simply download the feature from SWTBot download site, and copy-paste it to your product directory (if not p2-ready), or in the dropins folder (if product is p2-ready).

You may hit some missong dependency. Resolve them using the OSGi console:

From your product directory

$ MyProduct -console
osgi> start org.eclipse.swtbot.eclipse,junit4.headless
...Inspect...

#### Ship directly your product with SWTBot

If your product file is based on features, it will look like this:

<features>
  <feature id="com.yourcompany.feature1" version="x.y.z"/>
  <feature id="com.yourcompany.feature2" version="x.y.z"/>
  <feature id="org.eclipse.whatever" version="x.y.z"/>
  ... some other features...
</features> 

Make a copy of your .product file. Lets call it test.product for the time being. If your product is based on features, add the following lines in the test.product:

<features>
  <feature id="com.yourcompany.feature1" version="x.y.z"/>
  <feature id="com.yourcompany.feature2" version="x.y.z"/>
  <feature id="org.eclipse.whatever" version="x.y.z"/>
   ... some other features...  
  <feature id="org.eclipse.swtbot" />
  <feature id="org.eclipse.swtbot.eclipse" />
  <feature id="org.eclipse.pde" />
</features>

Once you have that in place, you can run the tests using the method described in [headless execution](/SWTBot/Ant "SWTBot/Ant"). See [this thread](https://www.eclipse.org/forums/index.php?&t=msg&th=153316) for a discussion on executing tests with products.

### Can I test a custom swt control with swtbot?

Yes, it's possible to [Test Custom Controls](/SWTBot/Testing_Custom_Controls "SWTBot/Testing Custom Controls") using SWTBot.

### Can I run SWTBot tests using Hudson/CruiseControl/Whatever?

Yes, see [CI Server Setup](/SWTBot/CI_Server "SWTBot/CI Server") for more information.

### Can I get more details on a particular widget?

Yes. SWTBot has a spy view that can be opened using "Window>Show View>Other...". Select "SWTBot Category>EclipseSpy View" to open a spy.

To activate/deactivate the view press CTRL+SHIFT. It will show you details on a particular widget when the cursor hovers over it. To toggle, or freeze info on a particular control, press CTRL+SHIFT.

### Is it possible to have JUnit5 based tests with SWTBot?

Yes, SWTBot 3.0.0 has the support for JUnit5. "SWTBot - JUnit5 Support" feature has to be selected while updating SWTBot and the Eclipse platform must support JUnit5. While running JUnit5 based SWTBot test from PDE, 'Test runner' should be set as 'JUnit5' in 'Test' tab of 'SWTBot Test' launch. In headless bundle, org.eclipse.swtbot.junit5.feature.group and org.eclipse.jdt.junit5.runtime should be added. To capture screenshot on failure in JUnit5 SWTBot test, following annotation is required in test- @ExtendWith(SWTBotJunit5Extension.class)

