/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.e4.finder.test.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

public class OpenPartHandler {

	@Execute
	public void execute(EPartService partService, EModelService modelService){
		final MPart part = partService.createPart("org.eclipse.swtbot.e4.finder.test.partdescriptor.0");
		part.getTags().add(EPartService.REMOVE_ON_HIDE_TAG);
		partService.showPart(part, PartState.ACTIVATE);
	}
	
}
