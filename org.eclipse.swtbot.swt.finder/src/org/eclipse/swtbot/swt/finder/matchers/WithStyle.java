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
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Matches if the widget has the specified style bits set.
 * 
 * @see Widget#getStyle()
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 * @since 2.0
 */
public class WithStyle<T extends Widget> extends AbstractMatcher<T> {

	private final int		style;
	private final String	styleDescription;

	/**
	 * Matches a widget that has the specified style bit set.
	 * 
	 * @param style the style bits.
	 * @param styleDescription the description of the style bits.
	 */
	WithStyle(int style, String styleDescription) {
		this.style = style;
		this.styleDescription = styleDescription;
	}

	@Override
	protected boolean doMatch(Object obj) {
		return SWTUtils.hasStyle((Widget) obj, style);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("with style '").appendText(styleDescription).appendText("'"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Matches a widget that has the specified style bit set.
	 * 
	 * @param style the style bits.
	 * @param styleDescription the description of the style bits.
	 * @return a matcher.
	 * @since 2.0
	 */
	public static <T extends Widget> Matcher<T> withStyle(int style, String styleDescription) {
		return new WithStyle<T>(style, styleDescription);
	}

}
