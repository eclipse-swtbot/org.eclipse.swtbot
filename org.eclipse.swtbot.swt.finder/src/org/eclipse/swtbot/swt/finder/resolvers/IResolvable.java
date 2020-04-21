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

import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public interface IResolvable {

	/**
	 * @param w the widget
	 * @return <code>true</code> if this widget can resolve the widget, <code>false</code> otherwise
	 */
	public boolean canResolve(Widget w);

	/**
	 * @return the list of classes that this resolver can resolve
	 */
	public abstract Class<?>[] getResolvableClasses();

}
