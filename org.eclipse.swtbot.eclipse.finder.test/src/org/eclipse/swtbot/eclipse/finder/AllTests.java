/*******************************************************************************
 * Copyright (c) 2008,2009,2010,2013 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Marcel Hoetter - added SWTBotToolbarContributionTest
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder;

import org.eclipse.swtbot.eclipse.finder.exceptions.QuickFixNotFoundExceptionTest;
import org.eclipse.swtbot.eclipse.finder.finders.CommandFinderTest;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditorTest;
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
		CommandFinderTest.class,
		SWTBotViewTest.class,
		QuickFixNotFoundExceptionTest.class,
		SWTBotEclipseEditorTest.class,
		SWTBotEclipseProjectTest.class,
		SWTBotMultiPageEditorTest.class,
		SWTBotToolbarContributionTest.class})
public class AllTests {
}
