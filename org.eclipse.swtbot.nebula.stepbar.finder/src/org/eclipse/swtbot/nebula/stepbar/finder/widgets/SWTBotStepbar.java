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
package org.eclipse.swtbot.nebula.stepbar.finder.widgets;

import java.lang.reflect.Field;
import java.util.List;

import org.eclipse.nebula.widgets.stepbar.Stepbar;
import org.eclipse.swt.SWTException;
import org.eclipse.swtbot.swt.finder.ReferenceBy;
import org.eclipse.swtbot.swt.finder.SWTBotWidget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.results.IntResult;
import org.eclipse.swtbot.swt.finder.results.ListResult;
import org.eclipse.swtbot.swt.finder.results.StringResult;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;
import org.hamcrest.SelfDescribing;

@SWTBotWidget(clasz = Stepbar.class, preferredName = "stepbar", referenceBy = { ReferenceBy.LABEL, //$NON-NLS-1$
		ReferenceBy.MNEMONIC })
public class SWTBotStepbar extends AbstractSWTBotControl<Stepbar> {

	public SWTBotStepbar(Stepbar w) throws WidgetNotFoundException {
		super(w);
	}

	public SWTBotStepbar(Stepbar w, SelfDescribing description) throws WidgetNotFoundException {
		super(w, description);
	}

	/**
	 * Returns the receiver's current step.
	 *
	 * @return the current step (starting index is 0)
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getCurrentStep() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getCurrentStep();
			}
		});
	}

	/**
	 * Returns the receiver's list of steps
	 *
	 * @return the color
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public List<String> getSteps() {
		return syncExec(new ListResult<String>() {
			@Override
			public List<String> run() {
				return widget.getSteps();
			}
		});
	}

	/**
	 * Returns the receiver's color of the text
	 *
	 * @return the color
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public String getTextColor() {
		return syncExec(new StringResult() {
			@Override
			public String run() {
				return widget.getTextColor().toString();
			}
		});
	}

	/**
	 * Returns the receiver's color used when the step is not reached
	 *
	 * @return the color
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public String getUnselectedColor() {
		return syncExec(new StringResult() {
			@Override
			public String run() {
				return widget.getUnselectedColor().toString();
			}
		});
	}

	/**
	 * Returns the error state
	 *
	 * @return <code>true</code> if error has occurred; <code>false</code>
	 *         otherwise.
	 *
	 */
	public boolean getErrorState() {
		return syncExec(new BoolResult() {
			@Override
			public Boolean run() {
				// No direct method to get error state, read private member 'onError'
				Field error = null;
				try {
					error = Stepbar.class.getDeclaredField("onError");
				} catch (NoSuchFieldException | SecurityException e) {
					return false;
				}
				error.setAccessible(true);
				try {
					// get value of field, convert it to boolean and return
					return (boolean) error.get(widget);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					return false;
				}
			}
		});
	}

}
