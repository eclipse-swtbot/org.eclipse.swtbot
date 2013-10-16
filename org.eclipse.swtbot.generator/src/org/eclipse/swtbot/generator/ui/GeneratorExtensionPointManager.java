/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.swtbot.generator.SWTBotGeneratorPlugin;
import org.eclipse.swtbot.generator.framework.Generator;
import org.eclipse.swtbot.generator.framework.IRecorderDialog;

public class GeneratorExtensionPointManager {

	private final static String GENERATOR_EXTENSION_POINT_ID = SWTBotGeneratorPlugin.PLUGIN_ID + ".botGeneratorSupport"; //$NON-NLS-1$
	private final static String DIALOG_EXTENSION_POINT_ID = SWTBotGeneratorPlugin.PLUGIN_ID + ".dialogSupport"; //$NON-NLS-1$

	public static List<Generator> loadGenerators() {
		List<Generator> res = new ArrayList<Generator>();
		for (IConfigurationElement ext : Platform.getExtensionRegistry().getConfigurationElementsFor(GENERATOR_EXTENSION_POINT_ID)) {
			try {
				Generator generator = (Generator)ext.createExecutableExtension("class");
				res.add(generator);
			} catch (CoreException ex) {
				SWTBotGeneratorPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, SWTBotGeneratorPlugin.PLUGIN_ID, "Could not load generator", ex)); //$NON-NLS-1$
			}
		}
		return res;
	}

	public static List<IRecorderDialog> loadDialogs() {
		List<IRecorderDialog> dialogs = new ArrayList<IRecorderDialog>();
		for (IConfigurationElement ext : Platform.getExtensionRegistry().getConfigurationElementsFor(DIALOG_EXTENSION_POINT_ID)) {
			try {
				IRecorderDialog dialog = (IRecorderDialog)ext.createExecutableExtension("class"); //$NON-NLS-1$
				dialogs.add(dialog);
			} catch (CoreException ex) {
				SWTBotGeneratorPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, SWTBotGeneratorPlugin.PLUGIN_ID, "Could not load dialog", ex)); //$NON-NLS-1$
			}
		}
		return dialogs;
	}

}
