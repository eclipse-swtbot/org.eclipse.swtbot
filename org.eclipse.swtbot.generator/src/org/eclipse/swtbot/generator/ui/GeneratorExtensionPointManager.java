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
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.generator.framework.Generator;

public class GeneratorExtensionPointManager {

	private final static String EXTENSION_POINT_ID = "org.eclipse.swtbot.generator.botGeneratorSupport";

	public static List<Generator> loadGenerators() {
		List<Generator> res = new ArrayList<Generator>();
		for (IConfigurationElement ext : Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_ID)) {
			try {
				Generator generator = (Generator)ext.createExecutableExtension("class");
				res.add(generator);
			} catch (CoreException ex) {
				// TODO log
			}
		}
		return res;
	}

}
