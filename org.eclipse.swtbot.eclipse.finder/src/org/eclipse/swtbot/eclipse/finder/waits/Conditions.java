/*******************************************************************************
 * Copyright (c) 2008, 2016 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Xavier Raynaud <xavier.raynaud@kalray.eu> - bug 437588
 *     Patrick Tasse - SWTBotView does not support dynamic view menus (Bug 489325)
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.waits;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swtbot.swt.finder.waits.WaitForObjectCondition;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewReference;
import org.hamcrest.Matcher;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class Conditions extends org.eclipse.swtbot.swt.finder.waits.Conditions {

	/**
	 * @param matcher a matcher
	 * @return a condition that waits until the matcher evaluates to true.
	 */
	public static WaitForView waitForView(Matcher<IViewReference> matcher) {
		return new WaitForView(matcher);
	}

	/**
	 * @param matcher a matcher
	 * @return a condition that waits until the matcher evaluates to true.
	 */
	public static WaitForEditor waitForEditor(Matcher<IEditorReference> matcher) {
		return new WaitForEditor(matcher);
	}

	/**
	 * @param a job family (see {@link Job#belongsTo(Object)})
	 * @param a human readable job family (used only for error message, may be null)
	 * @return a condition that waits until all jobs of the given family are done.
	 * @since 2.3
	 */
	public static WaitForJobs waitForJobs(Object jobFamily, String humanReadableJobFamily) {
		return new WaitForJobs(jobFamily, humanReadableJobFamily);
	}

	/**
	 * Gets the condition to wait for a view's view menu.
	 *
	 * @param viewReference the view reference.
	 * @return a condition that waits for the view's view menu.
	 * @since 2.4
	 */
	public static WaitForObjectCondition<Menu> waitForViewMenu(IViewReference viewReference) {
		return new WaitForViewMenu(viewReference);
	}

}
