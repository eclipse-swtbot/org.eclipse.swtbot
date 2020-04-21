/*******************************************************************************
 * Copyright (c) 2008, 2009 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     EclipseSource Corporation - ongoing enhancements
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.ui;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.pde.ui.launcher.JUnitLaunchConfigurationDelegate;

/**
 * A launch delegate for launching JUnit Plug-in tests.
 *
 * @since 3.3
 */
@SuppressWarnings("all")
public class SWTBotLaunchConfigurationDelegate extends JUnitLaunchConfigurationDelegate {

	public static final String	LAUNCH_CONFIG_ID	= "org.eclipse.swtbot.eclipse.ui.launcher.JunitLaunchConfig";	//$NON-NLS-1$

	@Override
	protected String getApplication(ILaunchConfiguration configuration) {
		return Activator.APPLICATION_ID;
	}

}
