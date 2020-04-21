/*******************************************************************************
 * Copyright (c) 2015 SWTBot Contributors and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Matt Biggs - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.e4.finder.test.parts.tests;

import static org.junit.Assert.assertEquals;

import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.swtbot.e4.finder.test.Activator;
import org.eclipse.swtbot.e4.finder.widgets.SWTBotPerspective;
import org.eclipse.swtbot.e4.finder.widgets.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.FrameworkUtil;

@RunWith(SWTBotJunit4ClassRunner.class)
public class SWTBotPerspectiveTest {

	/**
	 * NOTE: Whilst the class name is the same, this is the E4 version!
	 */
	private SWTWorkbenchBot	bot	= new SWTWorkbenchBot(getEclipseContext());
	
	@Test
	public void switchPerspective() throws Exception {
		final SWTBotPerspective perspective2 = this.bot.perspectiveByLabel("Perspective 2");
		assertEquals("Perspective 2", perspective2.getLabel());
		
		perspective2.activate();
		assertEquals(this.bot.activePerspective().getLabel(), perspective2.getLabel());
		
		final SWTBotPerspective perspective1 = this.bot.perspectiveByLabel("Perspective 1");
		assertEquals("Perspective 1", perspective1.getLabel());
		
		perspective1.activate();
		assertEquals(this.bot.activePerspective().getLabel(), perspective1.getLabel());
	}
		
	protected static IEclipseContext getEclipseContext() {
		final IEclipseContext serviceContext = EclipseContextFactory.getServiceContext(FrameworkUtil.getBundle(Activator.class).getBundleContext());
		return serviceContext.get(IWorkbench.class).getApplication().getContext();
	}
}
