/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Ketan Padegaonkar - http://swtbot.org/bugzilla/show_bug.cgi?id=126
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.matchers;

import org.eclipse.swt.widgets.Widget;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Tells if a particular widget is of a specified type.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 * @since 2.0
 */
public class WidgetOfType<T extends Widget> extends AbstractMatcher<T> {

	/**
	 * The type of widget to match.
	 */
	private Class<? extends Widget>	type;

	/**
	 * Matches a widget that has the specified type
	 * 
	 * @param type the type of the widget.
	 */
	WidgetOfType(Class<? extends Widget> type) {
		this.type = type;
	}

	@Override
	protected boolean doMatch(Object obj) {
		return type.isInstance(obj);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("of type '").appendText(type.getSimpleName()).appendText("'"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Matches a widget that has the specified type
	 * 
	 * @param type the type of the widget.
	 * @return a matcher.
	 * @since 2.0
	 */
	public static <T extends Widget> Matcher<T> widgetOfType(Class<T> type) {
		return new WidgetOfType<T>(type);
	}

}
