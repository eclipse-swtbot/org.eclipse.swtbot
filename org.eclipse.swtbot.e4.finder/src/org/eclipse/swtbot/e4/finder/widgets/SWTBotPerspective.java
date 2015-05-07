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
package org.eclipse.swtbot.e4.finder.widgets;

import org.eclipse.core.runtime.Assert;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;

/**
 * This represents an eclipse workbench perspective.
 * 
 * @author Ralf Ebert www.ralfebert.de (bug 271630)
 * @author Matt Biggs - Modified for E4
 */
public class SWTBotPerspective {

	private final SWTWorkbenchBot			bot;
	private final MPerspective				perspective;

	/**
	 * Constructs an instance of the given object.
	 *
	 * @param perspectiveDescriptor the perspective descriptor.
	 * @param bot the instance of {@link SWTWorkbenchBot} which will be used to drive operations on behalf of this
	 *            object.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 * @since 2.3.0
	 */
	public SWTBotPerspective(final MPerspective perspective, final SWTWorkbenchBot bot) throws WidgetNotFoundException {
		this.bot = bot;
		Assert.isNotNull(perspective, "The perspective cannot be null");
		this.perspective = perspective;
	}

	/**
	 * Switches the active workbench page to this perspective.
	 */
	public void activate() {
		this.bot.switchPerspective(this.perspective);
	}
	
	/**
	 * @return true if this perspective is active in the active workbench page
	 */
	public boolean isActive() {
		return bot.activePerspective().perspective.getElementId().equals(this.perspective.getElementId());
	}
	
	/**
	 * @return the perspective id
	 */
	public String getId() {
		return this.perspective.getElementId();
	}

	/**
	 * @return the perspective label
	 */
	public String getLabel() {
		return this.perspective.getLabel();
	}
	
	public String toString() {
		return "SWTBotEclipsePerspective[id=\"" + perspective.getLabel() + "\", label=\"" + perspective.getLabel()
				+ "\"]";
	}

}
