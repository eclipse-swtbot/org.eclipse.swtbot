/*******************************************************************************
 * Copyright (c) 2008, 2014 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Mickael Istria (Red Hat Inc.) - Added support for CTabFolder toolbars
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.resolvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class CTabFolderResolver implements IChildrenResolver, IParentResolver {

	@Override
	public boolean canResolve(Widget w) {
		return w instanceof CTabFolder;
	}

	@Override
	public List<Widget> getChildren(Widget w) {
		List<Widget> res = new ArrayList<Widget>();
		if (hasChildren(w)) {
			CTabFolder folder = (CTabFolder)w;
			res.addAll(Arrays.asList(folder.getItems()));
			if (folder.getTopRight() != null) {
				res.add(folder.getTopRight());
			}
		}
		return res;
	}

	@Override
	public Widget getParent(Widget w) {
		return canResolve(w) ? ((CTabFolder) w).getParent() : null;
	}

	@Override
	public Class<?>[] getResolvableClasses() {
		return new Class<?>[] { CTabFolder.class };
	}

	@Override
	public boolean hasChildren(Widget w) {
		return canResolve(w) && (((CTabFolder) w).getItems().length > 0 || ((CTabFolder)w).getTopRight() != null);
	}

	@Override
	public boolean hasParent(Widget w) {
		return getParent(w) != null;
	}

}
