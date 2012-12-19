/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

public class ExpandTreeItemRule extends AbstractTreeGenerationRule {

	@Override
	public boolean appliesTo(Event e) {
		return super.appliesTo(e) && e.type == SWT.Expand;
	}

	@Override
	protected String getActon() {
		return ".expand()";
	}

}
