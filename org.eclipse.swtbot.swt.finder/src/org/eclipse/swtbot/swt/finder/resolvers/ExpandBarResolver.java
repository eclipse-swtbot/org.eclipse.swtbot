/*******************************************************************************
 * Copyright (c) 2011, 2017 Ketan Padegaonkar and others.
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

import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 */
public class ExpandBarResolver implements IChildrenResolver, IParentResolver {

	@Override
	public boolean canResolve(Widget w) {
		return w instanceof ExpandBar;
	}

	@Override
	public List<Widget> getChildren(Widget w) {
		return hasChildren(w) ? Arrays.<Widget>asList(((ExpandBar) w).getItems()) : new ArrayList<Widget>();
	}

	@Override
	public Widget getParent(Widget w) {
		return canResolve(w) ? ((ExpandBar) w).getParent() : null;
	}

	@Override
	public Class<?>[] getResolvableClasses() {
		return new Class[] { ExpandBar.class };
	}

	@Override
	public boolean hasChildren(Widget w) {
		return canResolve(w) && ((ExpandBar) w).getItems().length > 0;
	}

	@Override
	public boolean hasParent(Widget w) {
		return getParent(w) != null;
	}

}
