/*******************************************************************************
 * Copyright (c) 2009, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Mariot Chauvin <mariot.chauvin@obeo.fr> - initial API and implementation
 *******************************************************************************/

package org.eclipse.swtbot.eclipse.gef.finder;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;

/**
 * This is a wrapper test case to the SWTBotTestCase that adds a GEF bot instead
 * of the standard bot.
 *
 * @author mchauvin
 */
public class SWTBotGefTestCase extends SWTBotTestCase {
	protected SWTGefBot	bot	= new SWTGefBot();
}
