/*******************************************************************************
 * Copyright (c) 2014 SWTBot Committers and others
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
package org.eclipse.swtbot.e4.finder.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.hamcrest.Matcher;

/**
 * WorkbenchContentsFinder allows to access the contents of a workbench window (views, editors, pages etc).
 * 
 * @author Ralf Ebert www.ralfebert.de (bug 271630)
 * @author Matt Biggs - Modified for E4
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public class WorkbenchContentsFinder {

	private final IEclipseContext context;

	public WorkbenchContentsFinder(final IEclipseContext context) {
		this.context = context;
	}

	/**
	 * @param matcher
	 * @return
	 */
	public List<MPerspective> findPerspectives(final Matcher<?> matcher) {
		final MApplication application = context.get(MApplication.class);
		final EModelService modelService = context.get(EModelService.class);

		final List<MPerspective> perspectives = modelService.findElements(application, null, MPerspective.class, null);
		final List<MPerspective> matchingPerspectives = new ArrayList<MPerspective>();
		for (final MPerspective perspective : perspectives) {
			if (matcher.matches(perspective)) {
				matchingPerspectives.add(perspective);
			}
		}
		return matchingPerspectives;
	}
	
	/**
	 * @return
	 */
	public MPerspective findActivePerspective() {
		final MApplication application = context.get(MApplication.class);
		final EModelService modelService = context.get(EModelService.class);
		final List<MWindow> windows = (List<MWindow>) modelService.findElements(application, null, MWindow.class, null);		
		return modelService.getActivePerspective(windows.get(0));
	}

	/**
	 * @param matcher
	 * @return
	 */
	public List<MPart> findParts(final Matcher<?> matcher) {
		final MPerspective perspective = findActivePerspective();
		final EModelService modelService = context.get(EModelService.class);

		final List<MPart> parts = modelService.findElements(perspective, null, MPart.class, null);
		final List<MPart> matchingParts = new ArrayList<MPart>();
		for (final MPart part : parts) {
			if (matcher.matches(part)) {
				matchingParts.add(part);
			}
		}
		return matchingParts;
	}
	
	/**
	 * @return the active part.
	 */
	public MPart findActivePart() {
		final EPartService partService = context.get(EPartService.class);
		return partService.getActivePart();
	}

}
