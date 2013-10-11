/*******************************************************************************
 * Copyright (c) 2013 Marcel Hoetter
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Marcel Hoetter - initial implementation based on TabItemResolver
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.resolvers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;

/**
 * Resolver for ToolItems.
 * 
 * @author Marcel Hoetter &lt;Marcel.Hoetter [at] genuinesoftware [dot] de&gt;
 * @version $Id$
 */
public class ToolItemResolver implements IChildrenResolver, IParentResolver {

	public boolean canResolve(Widget w) {
		return w instanceof ToolItem;
	}

	public List getChildren(Widget w) {
		ArrayList children = new ArrayList();
		children.add(((ToolItem) w).getControl());
		return children;
	}

	public Widget getParent(Widget w) {
		return (canResolve(w)) ? ((ToolItem) w).getParent() : null;
	}

	public Class[] getResolvableClasses() {
		return new Class[] { ToolItem.class };
	}

	public boolean hasChildren(Widget w) {
		return (canResolve(w)) && ((ToolItem) w).getControl() != null;
	}

	public boolean hasParent(Widget w) {
		return getParent(w) != null;
	}
}