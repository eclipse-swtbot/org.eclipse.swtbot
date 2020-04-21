/*******************************************************************************
 * Copyright (c) 2009 David Green and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     David Green - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.dsl;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

/**
 * A DSL for manipulating the Eclipse IDE
 * 
 * @author David Green
 */
public class Eclipse {

	/**
	 * @return A new workbench that provides convenience API to access common features of the Eclipse workbench.
	 */
	public static Workbench workbench() {
		return workbench(new SWTWorkbenchBot());
	}

	/**
	 * @param bot the bot that can drive the workbench.
	 * @return A new workbench that provides convenience API to access common features of the Eclipse workbench.
	 */
	public static Workbench workbench(SWTWorkbenchBot bot) {
		return new DefaultWorkbench(bot);
	}

}
