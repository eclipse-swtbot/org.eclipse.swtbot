/*******************************************************************************
 * Copyright (c) 2009 Obeo
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

package org.eclipse.gef.examples.logicdesigner;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;

public class LogicMWizardPage1 extends LogicWizardPage1 {

	public LogicMWizardPage1(IWorkbench aWorkbench,
			IStructuredSelection selection) {
		super(aWorkbench, selection);
	}

	public void createControl(Composite parent) {
		super.createControl(parent);
		this.setFileName("emptyModel" + ".logicm"); //$NON-NLS-2$//$NON-NLS-1$
	}

}
