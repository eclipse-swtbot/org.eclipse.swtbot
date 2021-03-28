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
package org.eclipse.swtbot.nebula.checkboxgroup.finder.widgets;

import org.eclipse.nebula.widgets.opal.checkboxgroup.CheckBoxGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swtbot.swt.finder.ReferenceBy;
import org.eclipse.swtbot.swt.finder.SWTBotWidget;
import org.eclipse.swtbot.swt.finder.Style;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.MessageFormat;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;
import org.hamcrest.SelfDescribing;

@SWTBotWidget(clasz = CheckBoxGroup.class, style = @Style(name = "SWT.CHECK", value = SWT.CHECK), preferredName = "checkBoxgroup", referenceBy = { //$NON-NLS-1$
		ReferenceBy.LABEL, ReferenceBy.MNEMONIC }) public class SWTBotCheckBoxGroup extends AbstractSWTBotControl<CheckBoxGroup> {
	public SWTBotCheckBoxGroup(CheckBoxGroup w) throws WidgetNotFoundException {
		super(w);
	}

	public SWTBotCheckBoxGroup(CheckBoxGroup w, SelfDescribing description) throws WidgetNotFoundException {
		super(w, description);
	}

	/**
	 * Sets the selection state of the receiver
	 *
	 * @param selection the new selection state
	 */
	public void setSelection(final boolean selection) {
		log.debug(MessageFormat.format("Selecting {0}", this)); //$NON-NLS-1$
		waitForEnabled();
		syncExec(new VoidResult() {
			@Override
			public void run() {
				log.debug(MessageFormat.format("Selecting {0}", this)); //$NON-NLS-1$

				Control children[] = widget.getChildren();
				for (Control child : children) {
					/*
					 * CheckBoxGroup adds SelectionListener on Button. Depending on button's
					 * selection state, it activates activates/deactivates all children. No need to
					 * call widget.setSelection again.
					 */
					if (child instanceof Button) {
						((Button) child).setSelection(selection);
						((Button) child).notifyListeners(SWT.Selection, createEvent());
					}
				}
			}
		});
	}

	/**
	 * Gets if the checkboxGroup button is selected.
	 *
	 * @return <code>true</code> if the receiver is checked. Otherwise
	 *         <code>false</code>.
	 */
	public boolean getSelection() {
		return syncExec(new BoolResult() {
			@Override
			public Boolean run() {
				return widget.getSelection();
			}
		});
	}

}
