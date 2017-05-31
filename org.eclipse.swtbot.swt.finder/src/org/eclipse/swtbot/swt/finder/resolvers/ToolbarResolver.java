/*******************************************************************************
 * Copyright (c) 2008, 2017 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public boolean canResolve(Widget w) {
		return w instanceof ToolBar;
	}

	public List<Widget> getChildren(Widget w) {
		return hasChildren(w) ? Arrays.<Widget>asList(((ToolBar) w).getItems()) : new ArrayList<Widget>();
	}

	public Widget getParent(Widget w) {
		return (canResolve(w)) ? ((ToolBar) w).getParent() : null;
	}

	public Class<?>[] getResolvableClasses() {
		return new Class[] { ToolBar.class };
	}

	public boolean hasChildren(Widget w) {
		return (canResolve(w)) && ((ToolBar) w).getItems().length > 0;
	}

	public boolean hasParent(Widget w) {
		return getParent(w) != null;
	}

}
