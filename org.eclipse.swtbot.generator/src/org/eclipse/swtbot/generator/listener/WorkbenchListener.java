/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Rastislav Wagner (Red Hat)
 *******************************************************************************/
package org.eclipse.swtbot.generator.listener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.generator.ui.BotGeneratorEventDispatcher;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;

public class WorkbenchListener implements IPartListener2{

	private BotGeneratorEventDispatcher dispatcher;
	public static final int PART_ACTIVATED=1;
	public static final int PART_CLOSED=0;

	public WorkbenchListener(BotGeneratorEventDispatcher dispatcher){
		this.dispatcher=dispatcher;
	}

	@Override
	public void partActivated(IWorkbenchPartReference arg0) {
		Event e = new Event();
		e.type=SWT.Selection;
		e.data=arg0;
		e.detail=PART_ACTIVATED;
		dispatcher.handleEvent(e);
	}

	@Override
	public void partClosed(IWorkbenchPartReference arg0) {
		Event e = new Event();
		e.type=SWT.Selection;
		e.data=arg0;
		e.detail=PART_CLOSED;
		dispatcher.handleEvent(e);

	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partHidden(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partOpened(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partVisible(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
		
	}

}