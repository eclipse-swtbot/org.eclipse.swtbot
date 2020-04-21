/*******************************************************************************
 * Copyright (c) 2010 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Mariot Chauvin <mariot.chauvin@obeo.fr> - initial API and implementation
 *******************************************************************************/

package org.eclipse.swtbot.eclipse.gef.finder.matchers;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Match if an edit part is selected.
 * @author mchauvin
 */
public class IsSelected extends BaseMatcher<SWTBotGefEditPart>{

	private EditPartViewer viewer;
	
	public IsSelected(final EditPartViewer viewer) {
		this.viewer = viewer;
	}
	
	public boolean matches(Object item) {
		if (item instanceof SWTBotGefEditPart && viewer.getSelectedEditParts().contains(((SWTBotGefEditPart) item).part()))
			return true;
		return false;
	}

	public void describeTo(Description description) {
		  description.appendText("is a selected edit part");
	}
}
