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
 *    Isaac Arvestad (Ericsson) - initial API and implementation (Based on: org.eclipse.swtbot.generator.ui.StartupRecorder)
 *******************************************************************************/
package org.eclipse.swtbot.generator.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.generator.framework.Generator;
import org.eclipse.swtbot.generator.listener.WorkbenchListener;
import org.eclipse.swtbot.generator.ui.BotGeneratorEventDispatcher;
import org.eclipse.swtbot.generator.ui.GeneratorExtensionPointManager;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

/**
 * StartupRecorderServer handles starting a SWTBot recorder and a
 * RecorderServer.
 */
public class StartupRecorderServer implements IStartup {

	public static final String ENABLED_WITH_PORT = "org.eclipse.swtbot.generator.server.enable";

	private static final int[] monitoredEvents = new int[] { SWT.Activate, SWT.Close, SWT.Selection, SWT.Expand,
			SWT.Modify, SWT.MouseDown, SWT.MouseDoubleClick, SWT.KeyDown, SWT.Close };

	/**
	 * StartRecorderServerRunnable starts the SWTBot recorder and the
	 * RecorderServer on a new thread.
	 */
	private static final class StartRecorderServerRunnable implements Runnable {
		private final Display display;
		private int port;

		/**
		 * Creates a new StartRecorderServerRunnable.
		 * 
		 * @param display
		 *            The display used.
		 * @param port
		 *            The port used for the RecorderServer.
		 */
		public StartRecorderServerRunnable(Display display, int port) {
			this.display = display;
			this.port = port;
		}

		@Override
		public void run() {
			final List<Generator> availableGenerators = GeneratorExtensionPointManager.loadGenerators();
			Generator generator = availableGenerators.get(0);
			final BotGeneratorEventDispatcher dispatcher = new BotGeneratorEventDispatcher();
			dispatcher.setGenerator(generator);

			List<Shell> ignoreList = new ArrayList<Shell>();
			dispatcher.ignoreShells(ignoreList);

			for (int monitoredEvent : monitoredEvents) {
				this.display.addFilter(monitoredEvent, dispatcher);
			}
			if (PlatformUI.isWorkbenchRunning()) {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				if (page != null) {
					page.addPartListener(new WorkbenchListener(dispatcher));
				}
			}

			RecorderServer recorderServer = new RecorderServer(dispatcher);
			recorderServer.start(port);
		}
	}

	/**
	 * Start the SWTBot recorder and the RecorderServer on a new thread.
	 * 
	 * @param port
	 *            The port to use for the RecorderServer.
	 */
	public void start(int port) {
		final Display display = Display.getDefault();
		StartRecorderServerRunnable serverRunnable = new StartRecorderServerRunnable(display, port);

		display.syncExec(serverRunnable);
	}

	/**
	 * Starts recorder server immediately once Eclipse has launched if the
	 * argument ENABLED_WITH_PORT can be found and it is equal to an integer
	 * which is used as the port number.
	 */
	@Override
	public void earlyStartup() {
		if (System.getProperty(ENABLED_WITH_PORT) == null) {
			return;
		}
		try {
			int port = Integer.parseInt(System.getProperty(ENABLED_WITH_PORT));
			start(port);
		} catch (NumberFormatException e) {
			System.out.println("SWTBot recorder server launch aborted. " + ENABLED_WITH_PORT
					+ " argument must be assigned an integer as a port number");
			return;
		}
	}
}
