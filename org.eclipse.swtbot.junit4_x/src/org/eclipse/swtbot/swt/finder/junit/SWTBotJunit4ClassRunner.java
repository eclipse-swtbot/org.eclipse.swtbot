/*******************************************************************************
 * Copyright (c) 2008 SWTBot Committers and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Hans Schwaebli - initial API and implementation (Bug 259787)
 *     Toby Weston  - initial API and implementation (Bug 259787)
 *     Thomas Wolf - Bug 372209.
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.junit;

import java.lang.reflect.Method;

import org.eclipse.swtbot.swt.finder.junit.internal.ScreenshotCaptureNotifier;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
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
 * @noextend This class is not intended to be subclassed by clients.
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

	/**
	 * A {@link FrameworkMethod} that tries to take a screenshot via the given
	 * {@link ScreenshotCaptureNotifier} as soon as the test method fails,
	 * before any @After clean-ups run.
	 */
	private static class CapturingFrameworkMethod extends FrameworkMethod {

		// We test for this exception using the name because it only became
		// public recently in JUnit 4.12
		// TODO: remove once we require JUnit 4.12 or greater
		private static final String ASSUMPTION_VIOLATED_EXCEPTION_NAME = "AssumptionViolatedException";

		private final Class<? extends Throwable> expectedException;
		private final Description description;
		private final ScreenshotCaptureNotifier notifier;

		public CapturingFrameworkMethod(Method method, Description description, ScreenshotCaptureNotifier notifier) {
			super(method);
			this.description = description;
			this.notifier = notifier;
			// Determine expected exception, if any
			Test annotation = method.getAnnotation(Test.class);
			expectedException = getExpectedException(annotation);
		}

		@Override
		public Object invokeExplosively(Object target, Object... params) throws Throwable {
			Object result = null;
			try {
				result = super.invokeExplosively(target, params);
			} catch (Throwable e) {
				// A timeout will give us an InterruptedException here and make
				// us capture a screenshot, too.
				// TODO: test for org.junit.AssumptionViolatedException once
				// we require JUnit 4.12 or greater
				if (!ASSUMPTION_VIOLATED_EXCEPTION_NAME.equals(e.getClass().getSimpleName())) {
					if (expectedException == null || !expectedException.isAssignableFrom(e.getClass())) {
						// Unexpected exception
						notifier.captureScreenshot(new Failure(description, e));
					}
				}
				throw e;
			}
			if (expectedException != null) {
				// No exception, but we expected one
				notifier.captureScreenshot(new Failure(description, null));
				// No need to raise an exception, an outer statement will do so.
			}
			// If the test didn't throw an exception but there is an outer
			// @ExpectedException rule that would expect one and then fail the
			// test, ScreenshotCaptureNotifier.fireTestFailed() will eventually
			// be called and will create the screenshot. In that case, however,
			// @After clean-ups already have run.
			return result;
		}

		private Class<? extends Throwable> getExpectedException(Test annotation) {
			if (annotation == null || annotation.expected() == None.class) {
				return null;
			} else {
				return annotation.expected();
			}
		}

		@Override
		public boolean equals(Object obj) {
			// The new fields added in this sub-class shall not influence
			// equality.
			return super.equals(obj);
		}

		@Override
		public int hashCode() {
			// The new fields added in this sub-class shall not influence the
			// hash code.
			return super.hashCode();
		}
	}

}
