/*******************************************************************************
 * Copyright (c) 2010 SWTBot Committers and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Hans Schwaebli - initial API and implementation (Bug 259787)
 *     Thomas Wolf - Bug 372209
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.junit;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Captures screenshots on failure notifications.
 *
 * @author Hans Schwaebli (Bug 259787)
 * @version $Id$
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public final class ScreenshotCaptureListener extends RunListener {
	/** The logger. */
	private static Logger log = Logger.getLogger(SWTBotApplicationLauncherClassRunner.class);

	/** Counts the screenshots to determine if maximum number is reached. */
	private static int screenshotCounter = 0;

	@Override
	public void testFailure(Failure failure) throws Exception {
		captureScreenshot(failure);
	}

	/**
	 * Removes a previously taken screenshot, if any.
	 *
	 * @param description
	 *            of the test
	 * @since 2.4
	 */
	public void removeScreenshot(Description description) {
		File file = new File(getFileName(new Failure(description, null)));
		if (file.exists()) {
			log.debug("Removing screenshot '" + file.getPath() + '\''); //$NON-NLS-1$
			if (!file.delete() && file.exists()) {
				log.warn("Could not remove screenshot " + file.getAbsolutePath()); //$NON-NLS-1$
			}
			--screenshotCounter;
		}
	}

	private void captureScreenshot(Failure failure) {
		try {
			int maximumScreenshots = SWTBotPreferences.MAX_ERROR_SCREENSHOT_COUNT;
			String fileName = getFileName(failure);
			if (++screenshotCounter <= maximumScreenshots) {
				captureScreenshot(fileName);
			} else {
				log.info("No screenshot captured for '" + failure.getTestHeader() //$NON-NLS-1$
						+ "' because maximum number of screenshots reached: "
						+ maximumScreenshots);
			}
		} catch (Exception e) {
			log.warn("Could not capture screenshot", e); //$NON-NLS-1$
		}
	}

	private String getFileName(Failure failure) {
		return SWTBotPreferences.SCREENSHOTS_DIR + "/" + failure.getTestHeader() + "." //$NON-NLS-1$
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
