/*******************************************************************************
 * Copyright (c) 2014 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Mickael Istria (Red Hat Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.test;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class TestDialog extends TitleAreaDialog{

	private AbstractGeneratorTest test;

	public TestDialog(Shell parentShell, AbstractGeneratorTest test) {
		super(parentShell);
		setBlockOnOpen(false);
		this.test = test;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.getShell().setText("test shell"); //$NON-NLS-1$
		setTitle("test shell"); //$NON-NLS-1$

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		test.contributeToDialog(container);
		return container;
	}

}
