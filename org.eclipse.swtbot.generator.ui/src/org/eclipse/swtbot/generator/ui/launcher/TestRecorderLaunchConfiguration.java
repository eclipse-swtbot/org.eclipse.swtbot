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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.pde.launching.EclipseApplicationLaunchConfiguration;
import org.eclipse.swtbot.generator.SWTBotGeneratorPlugin;
import org.eclipse.swtbot.generator.ui.GeneratorUIPlugin;
import org.eclipse.swtbot.generator.ui.Messages;
import org.eclipse.swtbot.generator.ui.StartupRecorder;
import org.osgi.framework.Bundle;

public class TestRecorderLaunchConfiguration extends EclipseApplicationLaunchConfiguration{

	public static final String RECORDER_ENABLEMENT = " -D" + StartupRecorder.ENABLEMENT_PROPERTY + "=" + Boolean.toString(true); //$NON-NLS-1$ //$NON-NLS-2$

//	public TestRecorderLaunchConfiguration(List<String> additionalBundles) {
//		super();
//		this.additionalBundles = additionalBundles;
//	}

	@Override
    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException{
		ILaunchConfigurationWorkingCopy c=configuration.getWorkingCopy();
		String currentAttributes = (String)c.getAttributes().get(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS);
        c.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,currentAttributes+ RECORDER_ENABLEMENT);
        super.launch(c, mode, launch, monitor);
    }

	/**
	 * This methods override the default implementation by adding a tweak the the generated
	 * config.ini in order to add recorder bundles to the launch configuration.
	 */
	@Override
	public String[] getProgramArguments(ILaunchConfiguration configuration) throws CoreException {
		String[] res = super.getProgramArguments(configuration);
		File configFile = new File(getConfigDir(configuration), "config.ini"); //$NON-NLS-1$
		Properties configProperties = new Properties();
		InputStream configInputStream = null;
		try {
			configInputStream = new FileInputStream(configFile);
			configProperties.load(configInputStream);
		} catch (Exception ex) {
			throw new CoreException(new Status(IStatus.ERROR, GeneratorUIPlugin.getDefault().getBundle().getSymbolicName(),
					Messages.errorWhileConfiguratingRuntime, ex));
		} finally {
			if (configInputStream != null) {
				try {
					configInputStream.close();
				} catch (Exception ex) {
					// Ignore
				}
			}
		}

		HashSet<String> additionalBundles = new HashSet<String>();
		additionalBundles.add(SWTBotGeneratorPlugin.PLUGIN_ID);
		// TODO this should also add the bundle containing the selected dialog + dependencies

		StringBuilder bundles = new StringBuilder(configProperties.getProperty("osgi.bundles", "")); //$NON-NLS-1$ //$NON-NLS-2$
		for (String additionalBundle : additionalBundles) {
			Bundle bundle = Platform.getBundle(additionalBundle);
			try {
				URL rootUrl= bundle.getEntry("/"); //$NON-NLS-1$
				URL fileRootUrl= FileLocator.toFileURL(rootUrl);
				bundles.append(",reference:file:"); //$NON-NLS-1$
				bundles.append(fileRootUrl.getFile());
			} catch (IOException ex) {
				throw new CoreException(new Status(IStatus.ERROR, GeneratorUIPlugin.getDefault().getBundle().getSymbolicName(), ex.getMessage(), ex));
			}
		}
		configProperties.put("osgi.bundles", bundles.toString());

		OutputStream configOutputStream = null;
		try {
			configOutputStream = new FileOutputStream(configFile);
			configProperties.store(configOutputStream, "Test Recorder");
		} catch (Exception ex) {
			throw new CoreException(new Status(IStatus.ERROR, GeneratorUIPlugin.getDefault().getBundle().getSymbolicName(),
					Messages.errorWhileConfiguratingRuntime, ex));
		} finally {
			if (configOutputStream != null) {
				try {
					configOutputStream.close();
				} catch (Exception ex) {
					// Ignore
				}
			}
		}

		return res;
	}


}