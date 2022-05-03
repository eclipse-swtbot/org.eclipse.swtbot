/*******************************************************************************
 * Copyright (c) 2021 Cadence Design Systems, Inc. and others.
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
package org.eclipse.swtbot.nebula.rangeslider.finder.widgets;

import org.eclipse.nebula.widgets.opal.rangeslider.RangeSlider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swtbot.swt.finder.ReferenceBy;
import org.eclipse.swtbot.swt.finder.SWTBotWidget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.IntResult;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.internal.Assert;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;
import org.hamcrest.SelfDescribing;

@SWTBotWidget(clasz = RangeSlider.class, preferredName = "rangeslider", referenceBy = { //$NON-NLS-1$
		ReferenceBy.LABEL, ReferenceBy.MNEMONIC })
public class SWTBotRangeSlider extends AbstractSWTBotControl<RangeSlider> {

	public SWTBotRangeSlider(RangeSlider w) throws WidgetNotFoundException {
		super(w);
	}

	public SWTBotRangeSlider(RangeSlider w, SelfDescribing description) throws WidgetNotFoundException {
		super(w, description);
	}

	/**
	 * Sets the 'lower selection', which is the receiver's lower value, to the input
	 * argument which must be less than or equal to the current 'upper selection'
	 * and greater or equal to the minimum. If either condition fails, no action is
	 * taken. RangeSlider widget must be visible on screen.
	 *
	 * @param value the new lower selection
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 * @see #getUpperValue()
	 * @see #getMinimum()
	 * @see #setSelection(int, int)
	 */
	public void setLowerValue(final int value) {
		Assert.isLegal(value >= 0, "The lower value is not valid");
		waitForEnabled();
		// get x, y of previous lower value and indicate LOWER selection of slider
		Point previousPoint = getXYFromValue(getLowerValue());
		sendEventsOnPrevious(previousPoint);
		// get slider to new point
		Point newPoint = getXYFromValue(value);
		sendEventsOnNew(newPoint);

		syncExec(new VoidResult() {
			@Override
			public void run() {
				log.debug("Selecting lower value {}", this); //$NON-NLS-1$
				widget.setLowerValue(value);
			}
		});
	}

	private void sendEventsOnPrevious(Point p) {
		notify(SWT.MouseEnter, createMouseEvent(0, SWT.NONE, 0));
		notify(SWT.Activate, super.createEvent());
		notify(SWT.FocusIn, super.createEvent());
		notify(SWT.MouseDown, createMouseEvent(p.x, p.y, 1, SWT.NONE, 1));
		notify(SWT.MouseUp, createMouseEvent(p.x, p.y, 1, SWT.BUTTON1, 1));
	}

	private void sendEventsOnNew(Point p) {
		notify(SWT.MouseDown, createMouseEvent(p.x, p.y, 1, SWT.NONE, 1));
		notify(SWT.MouseUp, createMouseEvent(p.x, p.y, 1, SWT.BUTTON1, 1));
		notify(SWT.MouseDown, createMouseEvent(p.x, p.y, 1, SWT.NONE, 2));
		notify(SWT.MouseDoubleClick, createMouseEvent(p.x, p.y, 1, SWT.NONE, 2));
		notify(SWT.MouseUp, createMouseEvent(p.x, p.y, 1, SWT.BUTTON1, 2));
	}

	/**
	 * Sets the 'upper selection', which is the upper receiver's value, to the input
	 * argument which must be greater than or equal to the current 'lower selection'
	 * and less or equal to the maximum. If either condition fails, no action is
	 * taken. RangeSlider widget must be visible on screen.
	 *
	 * @param value the new upper selection
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 * @see #getLowerValue()
	 * @see #getMaximum()
	 * @see #setSelection(int, int)
	 */
	public void setUpperValue(final int value) {
		Assert.isLegal(value >= 0, "The upper value is not valid");
		waitForEnabled();
		// get x, y of previous upper value and send events on that point indicate UPPER
		// selection of slider
		Point previousPoint = getXYFromValue(getUpperValue());
		sendEventsOnPrevious(previousPoint);
		// get slider to new point
		Point newPoint = getXYFromValue(value);
		sendEventsOnNew(newPoint);

		syncExec(new VoidResult() {
			@Override
			public void run() {
				log.debug("Selecting upper value {}", this); //$NON-NLS-1$
				widget.setUpperValue(value);
			}
		});
	}

	/**
	 * Sets the 'selection', which is the receiver's value. The lower value must be
	 * less than or equal to the upper value. Additionally, both values must be
	 * inclusively between the slider minimum and maximum. If either condition
	 * fails, no action is taken. RangeSlider widget must be visible on screen.
	 *
	 * @param lowerValue the new lower selection
	 * @param upperValue the new upper selection
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 * @see #getMinimum()
	 * @see #getMaximum()
	 */
	public void setSelection(final int lowerValue, final int upperValue) {
		Assert.isTrue(lowerValue <= upperValue, "The lower value must be less than or equal to the upper value");
		waitForEnabled();
		final int originalLower = getLowerSelection();
		final int originalUpper = getUpperSelection();
		boolean isLowerChange = originalLower != lowerValue;
		boolean isUpperChange = originalUpper != upperValue;

		if (isLowerChange) {
			setLowerValue(lowerValue);
		}
		if (isUpperChange) {
			setUpperValue(upperValue);
		}
	}

	protected Rectangle getClientArea() {
		return syncExec(new Result<Rectangle>() {
			@Override
			public Rectangle run() {
				if (widget.isDisposed()) {
					return new Rectangle(0, 0, 0, 0);
				}
				return widget.getClientArea();
			}
		});
	}

	/**
	 * Returns the 'lower selection', which is the lower receiver's position.
	 *
	 * @return the selection
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getLowerValue() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getLowerValue();
			}
		});
	}

	/**
	 * Returns the 'upper selection', which is the upper receiver's position.
	 *
	 * @return the selection
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getUpperValue() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getUpperValue();
			}
		});
	}

	/**
	 * Returns the 'selection', which is an array where the first element is the
	 * lower selection, and the second element is the upper selection
	 *
	 * @return the selection
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getLowerSelection() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getSelection()[0];
			}
		});
	}

	/**
	 * Returns the 'upper selection', which is the upper receiver's position.
	 *
	 * @return the selection
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getUpperSelection() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getSelection()[1];
			}
		});
	}

	/**
	 * Gets x, y position from value. Calculations are based on
	 * RangeSlider.getCursorValue code.
	 *
	 * @see RangeSlider#getCursorValue
	 * @param value
	 * @return point with x, y values
	 */
	private Point getXYFromValue(final int value) {
		return syncExec(new Result<Point>() {
			@Override
			public Point run() {
				final Rectangle rect = getClientArea();

				final int min = widget.getMinimum();
				final int max = widget.getMaximum();
				int x = 0, y = 0;

				if ((widget.getStyle() & SWT.HORIZONTAL) != 0) {
					x = (int) ((value * (rect.width - 20) / (max - min)) + 9d);
					y = rect.height / 2;

				} else if ((widget.getStyle() & SWT.VERTICAL) != 0) {
					y = (int) ((value * (rect.height - 20) / (max - min)) + 9d);
					x = rect.width / 2;
				}

				return new Point(x, y);
			}
		});
	}

}
