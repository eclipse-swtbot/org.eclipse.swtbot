/*******************************************************************************
 * Copyright (c) 2014 Frank Schuerer and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Frank Schuerer - initial implementation
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.test.ui.views;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.actions.ActionDelegate;

/**
 * A action contribution to the {@link SWTBotTestView} local view menu.
 * 
 * @author Frank Schuerer &lt;fschuerer [at] gmail [dot] com&gt;
 */
public class ViewActionWithId extends ActionDelegate implements
		IViewActionDelegate {

	private IViewPart view;

	public void init(IViewPart view) {
		this.view = view;
	}

	@Override
	public void run(IAction action) {
		MessageDialog.openInformation(view.getViewSite().getShell(), "Sample View", "ViewActionWithID executed");
	}
}
