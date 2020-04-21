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
 *     Hans Schwaebli - http://swtbot.org/bugzilla/show_bug.cgi?id=81
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.resolvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;

/**
 * Resolves {@link Composite}s and {@link Control}s
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class CompositeResolver implements IChildrenResolver, IParentResolver {

	@Override
	public boolean canResolve(Widget w) {
		// FIXME https://bugs.eclipse.org/bugs/show_bug.cgi?id=206868
		return (w instanceof Composite) && !(w.getClass().getName().equals("org.eclipse.swt.widgets.DateTime")); //$NON-NLS-1$
	}

	@Override
	public List<Widget> getChildren(Widget w) {
		// FIXME https://bugs.eclipse.org/bugs/show_bug.cgi?id=206868
		if (w.getClass().getName().equals("org.eclipse.swt.widgets.DateTime")) //$NON-NLS-1$
			return new ArrayList<Widget>();
		return hasChildren(w) ? Arrays.<Widget>asList(((Composite) w).getChildren()) : new ArrayList<Widget>();
	}

	@Override
	public Widget getParent(Widget w) {
		Composite parent = w instanceof Control ? ((Control) w).getParent() : null;
		if ((w instanceof Composite) && (parent instanceof TabFolder)) {
			TabItem[] items = ((TabFolder) parent).getItems();
			return items[SWTUtils.widgetIndex(w)];
		}
		return parent;
	}

	@Override
	public Class<?>[] getResolvableClasses() {
		return new Class[] { Composite.class, Control.class };
	}

	@Override
	public boolean hasChildren(Widget w) {
		// FIXME https://bugs.eclipse.org/bugs/show_bug.cgi?id=206868
		// No "instanceof DateTime" is used in order to be compatible with PDE 3.2.
		if (w.getClass().getName().equals("org.eclipse.swt.widgets.DateTime")) //$NON-NLS-1$
			return false;
		return canResolve(w) && ((Composite) w).getChildren().length > 0;
	}

	@Override
	public boolean hasParent(Widget w) {
		return getParent(w) != null;
	}
}
