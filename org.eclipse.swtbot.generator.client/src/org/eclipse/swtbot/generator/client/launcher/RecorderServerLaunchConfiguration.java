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

import javax.print.attribute.standard.Severity;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.pde.launching.EclipseApplicationLaunchConfiguration;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.generator.client.Recorder;
import org.eclipse.swtbot.generator.client.SWTBotRecorderClientPlugin;
import org.eclipse.swtbot.generator.client.views.RecorderClientView;
import org.eclipse.swtbot.generator.server.StartupRecorderServer;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Launch configuration for starting a new Eclipse application with the SWTBot
 * recorder running in the background as a server.
 */
public class RecorderServerLaunchConfiguration extends EclipseApplicationLaunchConfiguration {

	/**
	 * Launches an Eclipse application and then starts connecting with the
	 * client recorder.
	 */
	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {

		super.launch(configuration, mode, launch, monitor);

		int port = getServerPort(configuration);

		if (port == -1) {
			Status status = new Status(Severity.ERROR.getValue(), SWTBotRecorderClientPlugin.PLUGIN_ID,
					"Could not find a port number in the launch arguments");
			throw new CoreException(status);
		}

		startClientRecorder(port);
	}

	/**
	 * Returns the port that the server was launched with.
	 *
	 * @param configuration
	 *            The launch configuration used when launching the Eclipse
	 *            application.
	 * @return The port number or '-1' if port could not be parsed correctly.
	 * @throws CoreException
	 *             If attribute from configuration cannot be parsed.
	 */
	private int getServerPort(ILaunchConfiguration configuration) throws CoreException {
		String launchArguments = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "");
		String[] vmArguments = launchArguments.split(" ");
		for (int i = 0; i < vmArguments.length; i++) {
			String vmArgument = vmArguments[i];
			if (vmArgument.contains(StartupRecorderServer.ENABLED_WITH_PORT)) {
				String[] portArgumentKeyValue = vmArgument.split("=");
				if (portArgumentKeyValue.length == 2) {
					return Integer.parseInt(portArgumentKeyValue[1]);
				}
			}
		}

		return -1;
	}

	/**
	 * Starts the recorder client on a certain port asynchronously on UI thread.
	 *
	 * @param port
	 *            The port to start the recorder client on.
	 */
	private void startClientRecorder(final int port) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				RecorderClientView view = getRecorderClientView();
				if (view == null || !(view instanceof RecorderClientView)) {
					// Cannot find RecorderClientView, try to open it.
					try {
						openRecorderClientView();
						view = getRecorderClientView();
					} catch (PartInitException e) {
						throw new RuntimeException("Could not open RecorderClientView: " + e.getMessage());
					}
				}

				Recorder.INSTANCE.startRecorderClient(port);
				view.updateUI();
			}
		});
	}

	/**
	 * Finds the RecorderClientView. Should be called on the UI thread.
	 *
	 * @return The RecorderClientView
	 */
	private RecorderClientView getRecorderClientView() {
		return (RecorderClientView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(RecorderClientView.ID);
	}

	/**
	 * Opens the RecorderClientView. Should be called on the UI thread.
	 *
	 * @throws PartInitException
	 *             If the view could not be opened.
	 */
	private void openRecorderClientView() throws PartInitException {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RecorderClientView.ID);
	}
}
