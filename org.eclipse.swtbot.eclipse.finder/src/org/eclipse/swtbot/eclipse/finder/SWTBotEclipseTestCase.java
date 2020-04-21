/*******************************************************************************
 * Copyright (c) 2008, 2017 Ketan Padegaonkar and others.
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

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;

/**
 * This is a wrapper test case to the SWTBotTestCase that adds an eclipse bot
 * instead of the standard bot.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 * @since 1.2
 */
@Deprecated
public abstract class SWTBotEclipseTestCase extends SWTBotTestCase {
	/**
	 * An instance of SWTEclipseBot.
	 *
	 * @since 1.1
	 */
	protected SWTEclipseBot	bot	= new SWTEclipseBot();
}
