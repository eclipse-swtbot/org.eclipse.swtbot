/*******************************************************************************
 * Copyright (c) 2009 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder;

import org.eclipse.swt.SWT;

/**
 * Dumps information useful for providing debug info.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotInfo {

	private static final String	VERSION	= "@git.version@";

	/**
	 * Prints out some platform specific information useful for diagnosis.
	 */
	public static void printInfo() {
		System.out.println("--- Begin SWTBot Info ---");
		System.out.println("SWTBot version: " + VERSION);
		System.out.println("SWT Version: " + SWT.getVersion());
		System.out.println("SWT Platform: " + SWT.getPlatform());
		System.out.println("Operating System: " + System.getProperty("os.name") + "/" + System.getProperty("os.arch"));
		System.out.println("Version " + System.getProperty("os.version"));
		System.out.println("--- End SWTBot Info ---");
	}

}
