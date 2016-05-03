/*******************************************************************************
 * Copyright (c) 2016 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
