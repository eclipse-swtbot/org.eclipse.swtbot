/*******************************************************************************
 * Copyright (c) 2008, 2020 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Marcel Hoetter - added SWTBotToolbarContributionTest
 *     Stephane Bouchet (Intel Corporation) - added SWTBotEclipsePreferencesTest
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder;

import org.eclipse.swtbot.eclipse.finder.exceptions.QuickFixNotFoundExceptionTest;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditorTest;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipsePreferencesTest;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseProjectTest;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotMultiPageEditorTest;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotToolbarContributionTest;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotViewTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
@RunWith(Suite.class)
@SuiteClasses({
		SWTBotViewTest.class,
		QuickFixNotFoundExceptionTest.class,
		SWTBotEclipseEditorTest.class,
		SWTBotEclipseProjectTest.class,
		SWTBotMultiPageEditorTest.class,
		SWTBotToolbarContributionTest.class,
		SWTBotEclipsePreferencesTest.class})
public class AllTests {
}
