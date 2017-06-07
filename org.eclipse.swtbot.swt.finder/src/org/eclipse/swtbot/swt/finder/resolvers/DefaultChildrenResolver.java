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
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class DefaultChildrenResolver extends Resolvable implements IChildrenResolver {

	@Override
	public List<Widget> getChildren(Widget w) {
		List<Widget> result = new ArrayList<Widget>();

		if (!hasChildren(w))
			return result;

		List<IResolvable> resolvers = resolver.getResolvers(w.getClass());

		for (Iterator<IResolvable> iterator = resolvers.iterator(); iterator.hasNext();) {
			IChildrenResolver resolver = (IChildrenResolver) iterator.next();
			if (resolver.canResolve(w) && resolver.hasChildren(w)) {
				List<Widget> children = resolver.getChildren(w);
				if (children != null) {
					result.addAll(children);
					return result;
				}
			}
		}
		return result;
	}

	@Override
	public boolean hasChildren(Widget w) {
		List<IResolvable> resolvers = resolver.getResolvers(w.getClass());
		for (Iterator<IResolvable> iterator = resolvers.iterator(); iterator.hasNext();) {
			IChildrenResolver resolver = (IChildrenResolver) iterator.next();
			if (resolver.canResolve(w) && resolver.hasChildren(w))
				return true;
		}

		return false;
	}

}
