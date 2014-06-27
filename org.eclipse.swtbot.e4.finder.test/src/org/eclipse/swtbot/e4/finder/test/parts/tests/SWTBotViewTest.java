/*******************************************************************************
 * Copyright (c) 2015 SWTBot Contributors and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Matt Biggs - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.e4.finder.test.parts.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swtbot.e4.finder.test.Activator;
import org.eclipse.swtbot.e4.finder.widgets.SWTBotView;
import org.eclipse.swtbot.e4.finder.widgets.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.FrameworkUtil;

@RunWith(SWTBotJunit4ClassRunner.class)
public class SWTBotViewTest {

	/**
	 * NOTE: Whilst the class name is the same, this is the E4 version!
	 */
	private SWTWorkbenchBot	bot	= new SWTWorkbenchBot(getEclipseContext());
	
	@Test
	public void findsPart() throws Exception {
		this.bot.partByTitle("Sample Part 1");
	}
	
	@Test
	public void getsPartTitle() throws Exception {		
		assertEquals("Sample Part 1", this.bot.partByTitle("Sample Part 1").getTitle());
	}
	
	@Test
	public void showsPart() throws Exception {		
		this.bot.partByTitle("Sample Part 2").show();	
		assertEquals("Sample Part 2", this.bot.activePart().getTitle());
	}
	
	/**
	 * NOTE: This test passes because the Part has had the {@link EPartService}.REMOVE_ON_HIDE_TAG tag added
	 * 		 to the part in the Application.e4xmi
	 * 
	 * 		 Otherwise the part will simply have its toBeRendered flag set to false and it will still appear
	 * 		 in the model and be available to the bot.
	 * 
	 * @throws Exception
	 */
	@Test
	public void closePart() throws Exception {
		final SWTBotView part = this.bot.partByTitle("Sample Part 3");
		part.close();
		
		try {
			this.bot.partByTitle("Sample Part 3");
			fail("Expecting WidgetNotFoundException");
		} catch (WidgetNotFoundException expected) {
			// pass
		}	
	}
		
	@Test
	public void openNewlyCreatedPart() throws Exception {
		this.bot.menu("File").menu("Open Part").click();
		
		final SWTBotView part = this.bot.partByTitle("New Part");
		part.close();
	}
		
	protected static IEclipseContext getEclipseContext() {
		final IEclipseContext serviceContext = EclipseContextFactory.getServiceContext(FrameworkUtil.getBundle(Activator.class).getBundleContext());
		return serviceContext.get(IWorkbench.class).getApplication().getContext();
	}
}
