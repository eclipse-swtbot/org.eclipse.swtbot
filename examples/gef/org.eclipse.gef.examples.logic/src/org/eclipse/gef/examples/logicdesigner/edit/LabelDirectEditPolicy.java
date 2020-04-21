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
package org.eclipse.gef.examples.logicdesigner.edit;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import org.eclipse.gef.examples.logicdesigner.figures.StickyNoteFigure;
import org.eclipse.gef.examples.logicdesigner.model.LogicLabel;
import org.eclipse.gef.examples.logicdesigner.model.commands.LogicLabelCommand;

public class LabelDirectEditPolicy 
	extends DirectEditPolicy {

/**
 * @see DirectEditPolicy#getDirectEditCommand(DirectEditRequest)
 */
protected Command getDirectEditCommand(DirectEditRequest edit) {
	String labelText = (String)edit.getCellEditor().getValue();
	LogicLabelEditPart label = (LogicLabelEditPart)getHost();
	LogicLabelCommand command = new LogicLabelCommand((LogicLabel)label.getModel(),labelText);
	return command;
}

/**
 * @see DirectEditPolicy#showCurrentEditValue(DirectEditRequest)
 */
protected void showCurrentEditValue(DirectEditRequest request) {
	String value = (String)request.getCellEditor().getValue();
	((StickyNoteFigure)getHostFigure()).setText(value);
	//hack to prevent async layout from placing the cell editor twice.
	getHostFigure().getUpdateManager().performUpdate();
	
}

}
