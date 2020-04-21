/*******************************************************************************
 * Copyright (c) 2008, 2017 SWTBot Committers and others.
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
 *     Thomas Wolf - Bug 372209.
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.junit;

import org.eclipse.swtbot.swt.finder.junit.internal.CapturingFrameworkMethod;
import org.eclipse.swtbot.swt.finder.junit.internal.ScreenshotCaptureNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;

/**
 * A runner that captures screenshots on test failures. If you wish to launch
 * your application for your tests use
 * {@link SWTBotApplicationLauncherClassRunner}. Typical usage is:
 *
 * <pre>
 * &#064;RunWith(SWTBotJunit4ClassRunner.class)
 * public class FooTest {
 * 	&#064;Test
 * 	public void canSendEmail() {
 * 	}
 * }
 * </pre>
 *
 * @author Hans Schwaebli (Bug 259787)
 * @author Toby Weston (Bug 259787)
 * @version $Id$
 * @see SWTBotApplicationLauncherClassRunner
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public class SWTBotJunit4ClassRunner extends BlockJUnit4ClassRunner {

	/**
	 * Creates a SWTBotRunner to run {@code klass}
	 *
	 * @throws Exception
	 *             if the test class is malformed.
	 */
	public SWTBotJunit4ClassRunner(Class<?> klass) throws Exception {
		super(klass);
	}

	@Override
	public void run(RunNotifier notifier) {
		if (notifier instanceof ScreenshotCaptureNotifier) {
			super.run(notifier);
		} else {
			RunNotifier wrappedNotifier = new ScreenshotCaptureNotifier(notifier);
			super.run(wrappedNotifier);
		}
	}

	@Override
	protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
		Description description = describeChild(method);
		if (isIgnored(method)) {
			notifier.fireTestIgnored(description);
		} else {
			FrameworkMethod toRun = method;
			if (notifier instanceof ScreenshotCaptureNotifier) {
				toRun = new CapturingFrameworkMethod(method.getMethod(), description,
						(ScreenshotCaptureNotifier) notifier);
			}
			runLeaf(methodBlock(toRun), description, notifier);
		}
	}

}
