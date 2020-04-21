/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *    Rastislav Wagner (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.ui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.generator.framework.Generator;
import org.eclipse.swtbot.generator.framework.IRecorderDialog;
import org.eclipse.swtbot.generator.listener.WorkbenchListener;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class StartupRecorder implements IStartup {

	public static final String ENABLEMENT_PROPERTY = "org.eclipse.swtbot.generator.enable"; //$NON-NLS-1$
	public static final String DIALOG_PROPERTY = "org.eclipse.swtbot.generator.dialog"; //$NON-NLS-1$
	private static final int[] monitoredEvents = new int[] {
		SWT.Activate,
		SWT.Close,
		SWT.Selection,
		SWT.Expand,
		SWT.Modify,
		SWT.MouseDown,
		SWT.MouseDoubleClick,
		SWT.KeyDown,
		SWT.Close
	};

	private static final class StartRecorderRunnable implements Runnable {
		private final Display display;
		private String recorderDialogId;
		private IRecorderDialog recorderDialog;

		private StartRecorderRunnable(Display display) {
			this.display = display;
		}

		public void setRecorderDialog(String dialog){
			this.recorderDialogId = dialog;
		}

		@Override
		public void run() {
			final List<Generator> availableGenerators = GeneratorExtensionPointManager.loadGenerators();
			Generator generator = availableGenerators.get(0);
			final BotGeneratorEventDispatcher dispatcher = new BotGeneratorEventDispatcher();
			dispatcher.setGenerator(generator);

			for (int monitoredEvent : monitoredEvents) {
				this.display.addFilter(monitoredEvent, dispatcher);
			}
			if(PlatformUI.isWorkbenchRunning()){
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				if(page != null){
					page.addPartListener(new WorkbenchListener(dispatcher));
				}
			}

			if(recorderDialogId  == null || recorderDialogId.isEmpty()){
				final List<IRecorderDialog> dialogs = GeneratorExtensionPointManager.loadDialogs();
				this.recorderDialog = dialogs.get(0);
			} else {
				final List<IRecorderDialog> dialogs = GeneratorExtensionPointManager.loadDialogs();
				boolean foundRecorderClass = false;
				int i=0;
				while(!foundRecorderClass && dialogs.size() > i){
					if(dialogs.get(i).getId().equals(recorderDialogId)){
						foundRecorderClass = true;
						recorderDialog = dialogs.get(i);
					}
					i++;
				}
			}

			dispatcher.ignoreShells(recorderDialog.getIgnoredShells());
			recorderDialog.setAvailableGenerators(availableGenerators);
			recorderDialog.setRecorder(dispatcher);
			recorderDialog.open();
			recorderDialog.getShell().addShellListener(new ShellAdapter() {
				@Override
				public void shellClosed(ShellEvent e) {
					for (int monitoredEvent : monitoredEvents) {
						display.removeFilter(monitoredEvent, dispatcher);
					}
				}
			});

		}

		public IRecorderDialog getRecorderDialog() {
			return this.recorderDialog;
		}
	}

	@Override
	public void earlyStartup() {
		if (Boolean.parseBoolean(System.getProperty(ENABLEMENT_PROPERTY)) != true) {
			return;
		}
		openRecorder(null);
	}

	public static IRecorderDialog openRecorder(String dialog) {
		final Display display = Display.getDefault();
		StartRecorderRunnable recorderStarter = new StartRecorderRunnable(display);
		if(dialog == null && System.getProperty(DIALOG_PROPERTY) != null){
			dialog = System.getProperty(DIALOG_PROPERTY);
		}
		recorderStarter.setRecorderDialog(dialog);
		display.syncExec(recorderStarter);
		return recorderStarter.getRecorderDialog();
	}

}
