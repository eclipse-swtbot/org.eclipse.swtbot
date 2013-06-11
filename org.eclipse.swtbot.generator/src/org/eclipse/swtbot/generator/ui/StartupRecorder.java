/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.generator.framework.Generator;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

public class StartupRecorder implements IStartup {

	private static final class StartRecorderRunnable implements Runnable {
		private final Display display;
		private RecorderDialog recorderDialog;

		private StartRecorderRunnable(Display display) {
			this.display = display;
		}

		public void run() {
			final List<Generator> availableGenerators = GeneratorExtensionPointManager.loadGenerators();
			Generator generator = availableGenerators.get(0);
			final BotGeneratorEventDispatcher dispatcher = new BotGeneratorEventDispatcher();
			dispatcher.setGenerator(generator);

			this.display.addFilter(SWT.Activate, dispatcher);
			this.display.addFilter(SWT.Close, dispatcher);
			this.display.addFilter(SWT.Selection, dispatcher);
			this.display.addFilter(SWT.Expand, dispatcher);
			this.display.addFilter(SWT.Modify, dispatcher);
			this.display.addFilter(SWT.MouseDown, dispatcher);
			this.display.addFilter(SWT.MouseDoubleClick, dispatcher);

			Shell recorderShell = new Shell(PlatformUI.getWorkbench().getDisplay(), SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE);
			recorderShell.setText("SWTBot test recorder");
			dispatcher.ignoreShell(recorderShell);
			this.recorderDialog = new RecorderDialog(recorderShell, dispatcher, availableGenerators);
			this.recorderDialog.open();
			this.recorderDialog.getShell().addShellListener(new ShellAdapter() {
				public void shellClosed(ShellEvent e) {
					StartRecorderRunnable.this.display.removeFilter(SWT.Activate, dispatcher);
					StartRecorderRunnable.this.display.removeFilter(SWT.Close, dispatcher);
					StartRecorderRunnable.this.display.removeFilter(SWT.MouseDown, dispatcher);
					StartRecorderRunnable.this.display.removeFilter(SWT.MouseDoubleClick, dispatcher);
					StartRecorderRunnable.this.display.removeFilter(SWT.MouseUp, dispatcher);
					StartRecorderRunnable.this.display.removeFilter(SWT.KeyDown, dispatcher);
					StartRecorderRunnable.this.display.removeFilter(SWT.Selection, dispatcher);
					StartRecorderRunnable.this.display.removeFilter(SWT.Expand, dispatcher);
					StartRecorderRunnable.this.display.removeFilter(SWT.Modify, dispatcher);
					StartRecorderRunnable.this.display.removeFilter(SWT.DefaultSelection, dispatcher);
				}
			});
		}

		public RecorderDialog getRecorderDialog() {
			return this.recorderDialog;
		}
	}

	private static final String ENABLEMENT_PROPERTY = "org.eclipse.swtbot.generator.enable";

	public void earlyStartup() {
		if (Boolean.parseBoolean(System.getProperty(ENABLEMENT_PROPERTY)) != true) {
			return;
		}

		openRecorder();
	}

	public static RecorderDialog openRecorder() {
		final Display display = PlatformUI.getWorkbench().getDisplay();
		StartRecorderRunnable recorderStarter = new StartRecorderRunnable(display);
		display.syncExec(recorderStarter);
		return recorderStarter.getRecorderDialog();
	}

}