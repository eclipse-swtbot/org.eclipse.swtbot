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
package org.eclipse.swtbot.swt.finder.resolvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class ToolbarResolver implements IChildrenResolver, IParentResolver {

	@Override
	public boolean canResolve(Widget w) {
		return w instanceof ToolBar;
	}

	@Override
	public List<Widget> getChildren(Widget w) {
		return hasChildren(w) ? Arrays.<Widget>asList(((ToolBar) w).getItems()) : new ArrayList<Widget>();
	}

	@Override
	public Widget getParent(Widget w) {
		return (canResolve(w)) ? ((ToolBar) w).getParent() : null;
	}

	@Override
	public Class<?>[] getResolvableClasses() {
		return new Class[] { ToolBar.class };
	}

	@Override
	public boolean hasChildren(Widget w) {
		return (canResolve(w)) && ((ToolBar) w).getItems().length > 0;
	}

	@Override
	public boolean hasParent(Widget w) {
		return getParent(w) != null;
	}

}
