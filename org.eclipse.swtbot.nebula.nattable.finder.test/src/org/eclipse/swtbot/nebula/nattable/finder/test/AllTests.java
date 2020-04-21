/*******************************************************************************
 * Copyright (c) 2016 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Aparna Argade(Cadence Design Systems, Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.nebula.nattable.finder.test;

import org.eclipse.swtbot.nebula.nattable.finder.test1.TestNatTableContextMenu;
import org.eclipse.swtbot.nebula.nattable.finder.test1.TestRightClickXY;
import org.eclipse.swtbot.nebula.nattable.finder.test2.TestNatTableOtherFeatures;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestNatTableContextMenu.class, TestNatTableOtherFeatures.class,
	TestRightClickXY.class })
public class AllTests {

}
