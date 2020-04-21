/*******************************************************************************
 * Copyright (c) 2008, 2017 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.spy.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtbot.eclipse.spy.EclipseSpy;
import org.eclipse.swtbot.swt.finder.resolvers.DefaultChildrenResolver;
import org.eclipse.swtbot.swt.finder.resolvers.DefaultParentResolver;
import org.eclipse.ui.part.ViewPart;

public class EclipseSpyView extends ViewPart {

	private EclipseSpy	swtSpy;

	@Override
	public void createPartControl(Composite parent) {
		swtSpy = new EclipseSpy(parent, new DefaultChildrenResolver(), new DefaultParentResolver());
	}

	@Override
	public void setFocus() {
		swtSpy.setFocus();
	}

}
