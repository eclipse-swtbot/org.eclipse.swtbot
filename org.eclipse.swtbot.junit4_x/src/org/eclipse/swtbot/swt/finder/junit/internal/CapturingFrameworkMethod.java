/*******************************************************************************
 * Copyright (c) 2016 SWTBot Committers and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Thomas Wolf - Bug 372209
 *     Aparna Argade - Bug 498220
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.junit.internal;

import java.lang.reflect.Method;

import org.junit.AssumptionViolatedException;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runners.model.FrameworkMethod;

/**
 * A {@link FrameworkMethod} that tries to take a screenshot via the given
 * {@link ScreenshotCaptureNotifier} as soon as the test method fails, before
 * any @After clean-ups run.
 */
public class CapturingFrameworkMethod extends FrameworkMethod {

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
			if (!AssumptionViolatedException.class.isAssignableFrom(e.getClass())) {
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
