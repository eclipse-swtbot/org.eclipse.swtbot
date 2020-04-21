/*******************************************************************************
 * Copyright (c) 2020 Cadence Design Systems, Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Aparna Argade - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.junit5;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
/**
 * An extension that captures screenshots on test failures for JUnit5 based test.
 * <pre>
 * &#064;ExtendWith(SWTBotJunit5Extension.class)
 * public class FooTest {
 * 	&#064;Test
 * 	public void canSendEmail() {
 * 	}
 * }
 * </pre>
 *
 * @author Aparna Argade
 * @version $Id$
 * @noinstantiate This class is not intended to be instantiated by clients.
 */

public class SWTBotJunit5Extension implements TestWatcher {

	/** The logger. */
	private static Logger log = Logger.getLogger(SWTBot.class);

	/** Counts the screenshots to determine if maximum number is reached. */
	private static int screenshotCounter = 0;

	@Override
	public void testFailed(ExtensionContext context, Throwable cause) {
		captureScreenshot(context);
	}

	@Override
	public void testAborted(ExtensionContext context, Throwable cause) {
		/* no-op */
	}

	@Override
	public void testDisabled(ExtensionContext context, Optional<String> reason) {
		/* no-op */
	}

	@Override
	public void testSuccessful(ExtensionContext context) {
		/* no-op */
	}

	public void captureScreenshot(ExtensionContext context) {
		try {
			int maximumScreenshots = SWTBotPreferences.MAX_ERROR_SCREENSHOT_COUNT;
			String fileName = getFileName(context);
			if (++screenshotCounter <= maximumScreenshots) {
				captureScreenshot(fileName);
			} else {
				log.info("No screenshot captured for '" + context.getTestClass() + "." + context.getTestMethod() //$NON-NLS-1$
						+ "' because maximum number of screenshots reached: " + maximumScreenshots);
			}
		} catch (Exception e) {
			log.warn("Could not capture screenshot", e); //$NON-NLS-1$
		}
	}

	private String getFileName(ExtensionContext context) {
		return SWTBotPreferences.SCREENSHOTS_DIR + "/" + context.getRequiredTestClass().getName() + "." //$NON-NLS-1$
				+ SWTBotPreferences.SCREENSHOT_FORMAT.toLowerCase();
	}

	private boolean captureScreenshot(String fileName) {
		return SWTUtils.captureScreenshot(fileName);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

}
