/*******************************************************************************
 * Copyright (c) 2015 SWTBot Contributors and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Matt Biggs - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.e4.finder.test;

import org.eclipse.swtbot.e4.finder.test.parts.tests.SWTBotPerspectiveTest;
import org.eclipse.swtbot.e4.finder.test.parts.tests.SWTBotViewTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		SWTBotViewTest.class,
		SWTBotPerspectiveTest.class
		})
public class AllTests {
}
