/*******************************************************************************
 * Copyright (c) 2008,2010,2015 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Stephane Bouchet (Intel Corporation) - Bug 344484
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.ReferenceBy;
import org.eclipse.swtbot.swt.finder.SWTBotWidget;
import org.eclipse.swtbot.swt.finder.Style;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.utils.internal.Assert;
import org.hamcrest.SelfDescribing;

/**
 * Represents a radio {@link Button} of type {@link SWT#RADIO}.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 * @see SWTBotButton
 * @see SWTBotCheckBox
 * @see SWTBotToggleButton
 */
@SWTBotWidget(clasz = Button.class, style = @Style(name = "SWT.RADIO", value = SWT.RADIO), preferredName = "radio", referenceBy = { ReferenceBy.LABEL, ReferenceBy.MNEMONIC, ReferenceBy.TOOLTIP })//$NON-NLS-1$
public class SWTBotRadio extends AbstractSWTBotControl<Button> {

	/**
	 * Constructs an instance of this with the given widget.
	 *
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotRadio(Button w) throws WidgetNotFoundException {
		this(w, null);
	}

	/**
	 * Constructs an instance of this with the given widget.
	 *
	 * @param w the widget.
	 * @param description the description of the widget, this will be reported by {@link #toString()}
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotRadio(Button w, SelfDescribing description) throws WidgetNotFoundException {
		super(w, description);
		Assert.isTrue(SWTUtils.hasStyle(w, SWT.RADIO), "Expecting a radio."); //$NON-NLS-1$
	}

	/**
	 * Selects the radio button.
	 */
	@Override
	public SWTBotRadio click() {
		if (isSelected()) {
			log.debug("Widget {} is already selected, not clicking again.", this); //$NON-NLS-1$
			return this;
		}
		waitForEnabled();

		log.debug("Clicking on {}", this); //$NON-NLS-1$

		final SWTBotRadio otherSelectedButton=otherSelectedButton();

		if (otherSelectedButton != null) {
			otherSelectedButton.notify(SWT.Deactivate);
			asyncExec(new VoidResult() {
				@Override
				public void run() {
					otherSelectedButton.widget.setSelection(false);
				}
			});
			otherSelectedButton.notify(SWT.Selection);
		}

		setFocus();
		notify(SWT.Activate);
		notify(SWT.MouseDown, createMouseEvent(0, 0, 1, 0, 1));
		notify(SWT.MouseUp, createMouseEvent(0, 0, 1, SWT.BUTTON1, 1));
		asyncExec(new VoidResult() {
			@Override
			public void run() {
				widget.setSelection(true);
			}
		});
		notify(SWT.Selection);

		log.debug("Clicked on {}", this); //$NON-NLS-1$
		return this;
	}

	private SWTBotRadio otherSelectedButton() {
		Button button = syncExec(new WidgetResult<Button>() {
			@Override
			public Button run() {
				if (hasStyle(widget.getParent(), SWT.NO_RADIO_GROUP))
					return null;
				Widget[] siblings = SWTUtils.siblings(widget);
				for (Widget widget : siblings) {
					if ((widget instanceof Button) && hasStyle(widget, SWT.RADIO))
						if (((Button) widget).getSelection())
							return (Button) widget;
				}
				return null;
			}
		});

		if (button != null)
			return new SWTBotRadio(button);
		return null;
	}

	/**
	 * Checks if the item is selected.
	 *
	 * @return <code>true</code> if the radio button is selected. Otherwise <code>false</code>.
	 */
	public boolean isSelected() {
		return syncExec(new BoolResult() {
			@Override
			public Boolean run() {
				return widget.getSelection();
			}
		});
	}
}
