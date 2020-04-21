/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.ui.launcher;

import org.eclipse.pde.ui.launcher.JUnitWorkbenchLaunchShortcut;
import org.eclipse.swtbot.eclipse.ui.SWTBotLaunchConfigurationDelegate;

/**
 * Enhances the {@link JUnitWorkbenchLaunchShortcut} to launch SWTBot's launch configuration.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotLaunchShortcut extends JUnitWorkbenchLaunchShortcut {

	@Override
	protected String getLaunchConfigurationTypeId() {
		return SWTBotLaunchConfigurationDelegate.LAUNCH_CONFIG_ID;
	}

}
