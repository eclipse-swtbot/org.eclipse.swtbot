/*******************************************************************************
 * Copyright (c) 2018 Cadence Design Systems, Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Aparna Argade(Cadence Design Systems, Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swtbot.swt.finder.ReferenceBy;
import org.eclipse.swtbot.swt.finder.SWTBotWidget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.IntResult;
import org.eclipse.swtbot.swt.finder.utils.internal.Assert;
import org.hamcrest.SelfDescribing;

/**
 * SWTBot class representing a ProgressBar.
 *
 * @author Aparna Argade
 * @version $Id$
 * @since 2.8
 */
@SWTBotWidget(clasz = ProgressBar.class, preferredName = "progressbar", referenceBy = { ReferenceBy.IN_GROUP,
		ReferenceBy.LABEL, ReferenceBy.ID_VALUE })
public class SWTBotProgressBar extends AbstractSWTBotControl<ProgressBar> {

	/**
	 * Constructs a new instance with the given widget.
	 *
	 * @param w           the widget.
	 * @param description the description of the widget, this will be reported by
	 *                    {@link #toString()}
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget
	 *                                 has been disposed.
	 */
	public SWTBotProgressBar(ProgressBar w, SelfDescribing description) throws WidgetNotFoundException {
		super(w, description);
	}

	/**
	 * Constructs a new instance with the given widget.
	 *
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget
	 *                                 has been disposed.
	 */
	public SWTBotProgressBar(ProgressBar w) throws WidgetNotFoundException {
		this(w, null);
	}

	/**
	 * Returns the selection that is the position of the ProgressBar.
	 *
	 * @return the selection value of the ProgressBar.
	 */
	public int getProgress() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getSelection();
			}
		});
	}

	/**
	 * Returns the maximum value the ProgressBar will allow.
	 *
	 * @return the maximum value of the ProgressBar.
	 */
	public int getMaximum() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getMaximum();
			}
		});
	}

	/**
	 * Returns the minimum value the ProgressBar will allow.
	 *
	 * @return the minimum value of the ProgressBar.
	 */
	public int getMinimum() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getMinimum();
			}
		});
	}

	/**
	 * Returns the state of the ProgressBar.
	 *
	 * @return the state of the ProgressBar.
	 * @see ProgressBar#getState()
	 */
	public int getState() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getState();
			}
		});
	}

	/**
	 * Returns the style of the ProgressBar.
	 *
	 * @return the style of the ProgressBar.
	 * @see ProgressBar#getStyle()
	 */
	public int getStyle() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getStyle();
			}
		});
	}

	/**
	 * Returns the percentage of progress shown by the ProgressBar.
	 *
	 * @return the integer percentage of progress.
	 */
	public int getProgressPercentage() {
		int minimum = getMinimum();
		int maximum = getMaximum();
		Assert.isLegal(maximum != minimum, "Maximum and minimum settings cannot be equal."); //$NON-NLS-1$
		return ((getProgress() - minimum) * 100) / (maximum - minimum);
	}

}
