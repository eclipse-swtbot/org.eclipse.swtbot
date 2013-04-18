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

	private static final String ENABLEMENT_PROPERTY = "org.eclipse.swtbot.generator.enable";

	public void earlyStartup() {
		if (Boolean.parseBoolean(System.getProperty(ENABLEMENT_PROPERTY)) != true) {
			return;
		}

		final List<Generator> availableGenerators = GeneratorExtensionPointManager.loadGenerators();
		Generator generator = availableGenerators.get(0);
		final BotGeneratorEventDispatcher dispatcher = new BotGeneratorEventDispatcher();
		dispatcher.setGenerator(generator);
		final Display display = PlatformUI.getWorkbench().getDisplay();
		display.asyncExec(new Runnable() {
			public void run() {
				display.addFilter(SWT.Activate, dispatcher);
				display.addFilter(SWT.Close, dispatcher);
				display.addFilter(SWT.Selection, dispatcher);
				display.addFilter(SWT.Expand, dispatcher);
				display.addFilter(SWT.Modify, dispatcher);
				display.addFilter(SWT.MouseDown, dispatcher);
				display.addFilter(SWT.MouseDoubleClick, dispatcher);
				
				Shell recorderShell = new Shell(PlatformUI.getWorkbench().getDisplay(), SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE);
				recorderShell.setText("SWTBot test recorder");
				dispatcher.ignoreShell(recorderShell);
				RecorderDialog recorderDialog = new RecorderDialog(recorderShell, dispatcher, availableGenerators);
				recorderDialog.open();
				recorderDialog.getShell().addShellListener(new ShellAdapter() {
					public void shellClosed(ShellEvent e) {
						display.removeFilter(SWT.Activate, dispatcher);
						display.removeFilter(SWT.Close, dispatcher);
						display.removeFilter(SWT.MouseDown, dispatcher);
						display.removeFilter(SWT.MouseDoubleClick, dispatcher);
						display.removeFilter(SWT.MouseUp, dispatcher);
						display.removeFilter(SWT.KeyDown, dispatcher);
						display.removeFilter(SWT.Selection, dispatcher);
						display.removeFilter(SWT.Expand, dispatcher);
						display.removeFilter(SWT.Modify, dispatcher);
						display.removeFilter(SWT.DefaultSelection, dispatcher);
					}
				});
			}

		});
	}

}