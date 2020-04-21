/*******************************************************************************
 * Copyright (c) 2008 SWTBot Committers and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Hans Schwaebli - initial API and implementation (Bug 259787)
 *     Toby Weston  - initial API and implementation (Bug 259787)
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.junit;

import org.eclipse.swtbot.swt.finder.ApplicationRunner;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;

/**
 * A runner that launches your application and captures screenshots on test failures. If you wish to control the
 * lifecycle of your application, use {@link SWTBotJunit4ClassRunner}. Clients are supposed to subclass this. Typical
 * usage is:
 *
 * <pre>
 * &#064;RunWith(FooApplicationTestClassRunner.class)
 * public class FooTest {
 * 	&#064;Test
 * 	public void canSendEmail() {
 * 	}
 * }
 * </pre>
 *
 * The implementation of the <code>FooApplicationTestClassRunner</code> is:
 *
 * <pre>
 * public class FooApplicationTestClassRunner extends SWTBotApplicationLauncherClassRunner {
 * 	public void runApplication() {
 * 		FooApplication.main(new String[] { &quot;arguments&quot;, &quot;to&quot;, &quot;your application&quot; });
 * 	}
 * }
 * </pre>
 *
 * @author Hans Schwaebli (Bug 259787)
 * @author Toby Weston (Bug 259787)
 * @version $Id$
 * @see SWTBotJunit4ClassRunner
 */
public abstract class SWTBotApplicationLauncherClassRunner extends SWTBotJunit4ClassRunner implements ApplicationRunner {

	/**
	 * Creates a SWTBotRunner to run {@code klass}
	 *
	 * @throws Exception if the test class is malformed.
	 */
	public SWTBotApplicationLauncherClassRunner(Class<?> klass) throws Exception {
		super(klass);
		startApplicationInAnotherThread();
	}

	private void startApplicationInAnotherThread() {
		if (isApplicationRunning())
			return;
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				startApplication();
			}
		};

		Thread uiThread = new Thread(runnable);
		uiThread.start();

		// so we started the application, we'll wait until we see a display.
		SWTUtils.waitForDisplayToAppear();
	}

	@Override
	public boolean isApplicationRunning() {
		try {
			SWTUtils.waitForDisplayToAppear();
		} catch (Throwable e) {
			return false;
		}
		return true;
	}

}
