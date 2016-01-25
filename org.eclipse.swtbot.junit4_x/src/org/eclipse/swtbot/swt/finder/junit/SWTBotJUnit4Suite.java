/*******************************************************************************
 * Copyright (c) 2008 SWTBot Committers and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Hans Schwaebli - initial API and implementation (Bug 273403)
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.junit;

import org.eclipse.swtbot.swt.finder.junit.internal.ScreenshotCaptureNotifier;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.RunnerBuilder;

/**
 * A suite runner that captures screenshots on test failures. Clients are not supposed to subclass this. Typical usage is:
 * 
 * <pre>
 * &#064;RunWith(SWTBotJUnit4Suite.class)
 * &#064;SuiteClasses( { FooTest.class, BarTest.class })
 * public class MySuite {
 * }
 * </pre>
 * 
 * @author Hans Schwaebli (Bug 273403)
 * @version $Id:
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @deprecated Please use {@link Suite} <code>@RunWith(Suite.class)</code> instead and annotate the individual tests 
 * to run with {@link SWTBotJunit4ClassRunner} <code>@RunWith(SWTBotJunit4ClassRunner.class)</code>. 
 */
public final class SWTBotJUnit4Suite extends Suite {
	
	public SWTBotJUnit4Suite(Class<?> klass, RunnerBuilder builder) throws Exception {
		super(klass, builder);
		System.err.println("This class has been deprecated, please use org.junit.runners.Suite.class instead.");
	}

	public SWTBotJUnit4Suite(Class<?> klass, Class<?>[] annotatedClasses) throws Exception {
		super(klass, annotatedClasses);
		System.err.println("This class has been deprecated, please use org.junit.runners.Suite.class instead.");
	}

	/**
	 * Decorates the run method of the super class with a screenshot capturer.
	 * 
	 * @see org.junit.runners.Suite#run(RunNotifier)
	 */
	public void run(RunNotifier notifier) {
		if (notifier instanceof ScreenshotCaptureNotifier) {
			super.run(notifier);
		} else {
			RunNotifier wrappedNotifier = new ScreenshotCaptureNotifier(notifier);
			super.run(wrappedNotifier);
		}
	}

}
