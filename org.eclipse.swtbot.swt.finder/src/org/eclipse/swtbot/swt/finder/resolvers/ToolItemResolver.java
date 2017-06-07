/*******************************************************************************
 * Copyright (c) 2013, 2017 Marcel Hoetter and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Marcel Hoetter - initial implementation based on TabItemResolver
 *     Lorenzo Bettini - (Bug 426869) mark new methods with since annotation
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
 * @author Lorenzo Bettini - (Bug 426869) mark new methods with since annotation
 * @version $Id$
 * @since 2.2
 */
public class ToolItemResolver implements IChildrenResolver, IParentResolver {

	@Override
	public boolean canResolve(Widget w) {
		return w instanceof ToolItem;
	}

	@Override
	public List<Widget> getChildren(Widget w) {
		List<Widget> children = new ArrayList<Widget>();
		children.add(((ToolItem) w).getControl());
		return children;
	}

	@Override
	public Widget getParent(Widget w) {
		return (canResolve(w)) ? ((ToolItem) w).getParent() : null;
	}

	@Override
	public Class<?>[] getResolvableClasses() {
		return new Class[] { ToolItem.class };
	}

	@Override
	public boolean hasChildren(Widget w) {
		return (canResolve(w)) && ((ToolItem) w).getControl() != null;
	}

	@Override
	public boolean hasParent(Widget w) {
		return getParent(w) != null;
	}
}