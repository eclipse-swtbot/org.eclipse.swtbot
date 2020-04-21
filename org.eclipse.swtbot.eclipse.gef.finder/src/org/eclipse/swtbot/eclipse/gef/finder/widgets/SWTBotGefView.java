/*******************************************************************************
 * Copyright (c) 2010, 2017 Obeo and others.
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

package org.eclipse.swtbot.eclipse.gef.finder.widgets;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;

public class SWTBotGefView extends SWTBotView {

	protected final SWTBotGefViewer viewer;
	
	public SWTBotGefView(final IViewReference partReference, SWTWorkbenchBot bot) {
		super(partReference, bot);
		GraphicalViewer graphicalViewer = UIThreadRunnable.syncExec(new Result<GraphicalViewer>() {
			public GraphicalViewer run() {
				final IViewPart view = partReference.getView(true);
				return view.getAdapter(GraphicalViewer.class);
			}
		});
		viewer = new SWTBotGefViewer(graphicalViewer);
		viewer.init();
	}
	
	/**
	 * Get the wrapped SWTBotGefViewer instance.
	 * @return a SWTBotGefViewer instance
	 */
	public SWTBotGefViewer getSWTBotGefViewer() {
		return viewer;
	}

}
