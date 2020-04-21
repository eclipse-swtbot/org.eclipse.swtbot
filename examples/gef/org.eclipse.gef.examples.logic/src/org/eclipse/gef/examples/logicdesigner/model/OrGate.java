/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.model;

import org.eclipse.swt.graphics.Image;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class OrGate
	extends Gate 
{

static private Image OR_ICON = createImage(OrGate.class, "icons/or16.gif");  //$NON-NLS-1$
static final long serialVersionUID = 1;

public Image getIconImage() {
	return OR_ICON;
}

public boolean getResult() {
	return getInput(TERMINAL_A) | getInput(TERMINAL_B);
}

public String toString() {
	return LogicMessages.OrGate_LabelText + " #" + getID();  //$NON-NLS-1$
}

}
