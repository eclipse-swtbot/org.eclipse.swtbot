/*******************************************************************************
 * Copyright (c) 2016 SWTBot Committers and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Thomas Wolf - initial API and implementation (Bug 372209).
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.junit.internal;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swtbot.swt.finder.junit.ScreenshotCaptureListener;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;

/**
 * A {@link RunNotifier} that wraps another existing {@link RunNotifier} and
 * augments it by taking at most one screenshot upon a test failure.
 */
public class ScreenshotCaptureNotifier extends RunNotifier {

	private final ScreenshotCaptureListener screenshotCreator = new ScreenshotCaptureListener();

	private final RunNotifier delegate;

	private final AtomicBoolean screenshotTakenForTest = new AtomicBoolean();

	private Failure testFailure;

	public ScreenshotCaptureNotifier(RunNotifier notifier) {
		delegate = notifier;
		super.addListener(screenshotCreator);
	}

	/**
	 * Capture a screenshot if none had been captured yet.
	 *
	 * @param failure
	 *            that occurred
	 */
	public void captureScreenshot(Failure failure) {
		if (!screenshotTakenForTest.getAndSet(true)) {
			super.fireTestFailure(failure);
		}
	}

	@Override
	public void fireTestStarted(Description description) throws StoppedByUserException {
		screenshotTakenForTest.set(false);
		testFailure = null;
		delegate.fireTestStarted(description);
	}

	@Override
	public void fireTestFailure(Failure failure) {
		testFailure = failure;
		captureScreenshot(failure);
		delegate.fireTestFailure(failure);
	}

	@Override
	public void fireTestFinished(Description description) {
		delegate.fireTestFinished(description);
		if (testFailure == null) {
			// Test didn't fail after all -- perhaps some rule let the test
			// pass even though it had raised an exception. Remove a previously
			// taken screenshot, if any.
			screenshotCreator.removeScreenshot(description);
		}
		testFailure = null;
	}

	// Every other public method just delegates to the original notifier

	@Override
	public void addListener(RunListener listener) {
		delegate.addListener(listener);
	}

	@Override
	public void removeListener(RunListener listener) {
		delegate.removeListener(listener);
	}

	@Override
	public void fireTestRunStarted(Description description) {
		delegate.fireTestRunStarted(description);
	}

	@Override
	public void fireTestRunFinished(Result result) {
		delegate.fireTestRunFinished(result);
	}

	@Override
	public void fireTestAssumptionFailed(Failure failure) {
		delegate.fireTestAssumptionFailed(failure);
	}

	@Override
	public void fireTestIgnored(Description description) {
		delegate.fireTestIgnored(description);
	}

	@Override
	public void pleaseStop() {
		delegate.pleaseStop();
	}

	@Override
	public void addFirstListener(RunListener listener) {
		// This is actually an operation internal to JUnit according to its
		// javadoc. It's only used in another JUnit method marked as
		// "Do not use. For testing purposes only."
		delegate.addFirstListener(listener);
	}

}
