/*******************************************************************************
 * Copyright (c) 2016 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Aparna Argade(Cadence Design Systems, Inc.) - Bug 498220
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.junit;

import org.eclipse.swtbot.swt.finder.junit.internal.CapturingFrameworkMethod;
import org.eclipse.swtbot.swt.finder.junit.internal.ScreenshotCaptureNotifier;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

/**
 * A runner that combines the behavior of {@link Parameterized} and
 * {@link SWTBotJunit4ClassRunner}. Typical usage is:
 *
 * <pre>
 * &#064;RunWith(Parameterized.class)
 * &#064;Parameterized.UseParametersRunnerFactory(ParameterizedSWTBotJunit4ClassRunner.RunnerFactory.class)
 * </pre>
 * This should be followed by code which is compatible for {@link Parameterized} runner.
 *
 * @author Aparna Argade(Cadence Design Systems, Inc.) - Bug 498220
 * @version $Id$
 * @noextend This class is not intended to be sub-classed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @since 2.5
 */
public class ParameterizedSWTBotJunit4ClassRunner extends BlockJUnit4ClassRunnerWithParameters {

	private final Object[] parameters;

	/**
	 * Creates a SWTBotRunner with test instance
	 *
	 * @param test
	 *            Test instance
	 * @throws InitializationError
	 */
	public ParameterizedSWTBotJunit4ClassRunner(TestWithParameters test) throws InitializationError {
		super(test);
		parameters = test.getParameters().toArray(new Object[test.getParameters().size()]);
	}

	@Override
	public Object createTest() throws Exception {
		return getTestClass().getOnlyConstructor().newInstance(parameters);
	}

	@Override
	public void run(final RunNotifier notifier) {
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
	 * Creates a runner for a single {@link TestWithParameters}.
	 */
	public static class RunnerFactory implements ParametersRunnerFactory {
		@Override
		public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
			return new ParameterizedSWTBotJunit4ClassRunner(test);
		}
	}

}