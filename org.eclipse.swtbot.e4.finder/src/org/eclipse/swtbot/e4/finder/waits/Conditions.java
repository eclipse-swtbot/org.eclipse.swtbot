/*******************************************************************************
 * Copyright (c) 2014 SWTBot Committers and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Matt Biggs - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.e4.finder.waits;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.hamcrest.Matcher;

/**
 * This is a factory class to create some conditions provided with SWTBot.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @author Matt biggs - Converted to E4
 * @version $Id$
 */
public class Conditions extends org.eclipse.swtbot.swt.finder.waits.Conditions {

	/**
	 * @param matcher a matcher
	 * @return a condition that waits until the matcher evaluates to true.
	 */
	public static WaitForPart waitForPart(final IEclipseContext context, final Matcher<MPart> matcher) {
		return new WaitForPart(context, matcher);
	}
	
}
