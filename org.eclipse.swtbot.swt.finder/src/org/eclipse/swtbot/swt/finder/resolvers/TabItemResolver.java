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
import java.util.List;

import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class TabItemResolver implements IChildrenResolver, IParentResolver {

	@Override
	public boolean canResolve(Widget w) {
		return w instanceof TabItem;
	}

	@Override
	public List<Widget> getChildren(Widget w) {
		List<Widget> children = new ArrayList<Widget>();
		children.add(((TabItem) w).getControl());
		return children;
	}

	@Override
	public Widget getParent(Widget w) {
		return (canResolve(w)) ? ((TabItem) w).getParent() : null;
	}

	@Override
	public Class<?>[] getResolvableClasses() {
		return new Class[] { TabItem.class };
	}

	@Override
	public boolean hasChildren(Widget w) {
		return (canResolve(w)) && ((TabItem) w).getControl() != null;
	}

	@Override
	public boolean hasParent(Widget w) {
		return getParent(w) != null;
	}

}
