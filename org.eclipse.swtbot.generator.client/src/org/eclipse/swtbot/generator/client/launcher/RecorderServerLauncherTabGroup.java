/*******************************************************************************
 * Copyright (c) 2016 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Isaac Arvestad (Ericsson) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.client.launcher;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.pde.ui.launcher.EclipseLauncherTabGroup;
import org.eclipse.swtbot.generator.server.StartupRecorderServer;

/**
 * Appends <code>StartupRecorderServer.ENABLED_WITH_PORT</code> to the VM
 * arguments.
 */
public class RecorderServerLauncherTabGroup extends EclipseLauncherTabGroup {

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		super.setDefaults(configuration);

		try {
			String launchArguments = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
					"");
			String launchArgument = "\n-D" + StartupRecorderServer.ENABLED_WITH_PORT + "=" + 8000;
			configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
					launchArguments + " " + launchArgument);

			configuration.doSave();
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}
}
