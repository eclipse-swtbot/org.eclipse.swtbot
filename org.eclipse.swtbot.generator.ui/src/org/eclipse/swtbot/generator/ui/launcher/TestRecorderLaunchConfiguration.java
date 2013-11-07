/*******************************************************************************
 * Copyright (c) 2013 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Rastislav Wagner (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.ui.launcher;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.pde.launching.EclipseApplicationLaunchConfiguration;
import org.eclipse.swtbot.generator.ui.StartupRecorder;

public class TestRecorderLaunchConfiguration extends EclipseApplicationLaunchConfiguration{
	
	public static final String RECORDER_ENABLEMENT = " -D"+StartupRecorder.ENABLEMENT_PROPERTY+"=true";
	
	@Override
    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException{
		ILaunchConfigurationWorkingCopy c=configuration.getWorkingCopy();
		String currentAttributes = (String)c.getAttributes().get(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS);
        c.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,currentAttributes+ RECORDER_ENABLEMENT);
        super.launch(c, mode, launch, monitor);
    }

}