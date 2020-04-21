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
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.utils.internal;

import java.util.Arrays;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.results.IntResult;

/**
 * This is used to find the sibling widget and its index.
 * <p>
 * <b>NOTE: This finds all the siblings and finds the index of the widget among the siblings. This does not use SWTUtils
 * to find siblings for performance reasons</b>
 * </p>
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @see PreviousWidgetFinder
 * @see NextWidgetFinder
 */
public final class WidgetIndexFinder implements IntResult {
	/**
	 * The widget.
	 */
	private final Widget	w;

	/**
	 * Constructs the widget index finder for the given widget.
	 *
	 * @param w the widget.
	 */
	public WidgetIndexFinder(Widget w) {
		this.w = w;
	}

	/**
	 * Runs the finder to locate the index of the sibling.
	 *
	 * @see org.eclipse.swtbot.swt.finder.results.IntResult#run()
	 * @return The index value.
	 */
	@Override
	public Integer run() {
		Widget[] siblings = new SiblingFinder(w).run();
		return indexOf(siblings, w);
	}

	/**
	 * Gets the index of the widget in the list of widgets.
	 *
	 * @param widgets The widget set to search through.
	 * @param w The widget to find.
	 * @return The index of the widget.
	 */
	private int indexOf(Widget[] widgets, Widget w) {
		return Arrays.asList(widgets).indexOf(w);
	}
}
