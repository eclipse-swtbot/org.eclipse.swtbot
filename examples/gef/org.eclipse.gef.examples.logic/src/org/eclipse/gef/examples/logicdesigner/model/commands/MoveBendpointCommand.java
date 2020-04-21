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
package org.eclipse.gef.examples.logicdesigner.model.commands;

import org.eclipse.draw2d.Bendpoint;

import org.eclipse.gef.examples.logicdesigner.model.WireBendpoint;

public class MoveBendpointCommand
	extends BendpointCommand 
{

private Bendpoint oldBendpoint;

public void execute() {
	WireBendpoint bp = new WireBendpoint();
	bp.setRelativeDimensions(getFirstRelativeDimension(), 
					getSecondRelativeDimension());
	setOldBendpoint((Bendpoint)getWire().getBendpoints().get(getIndex()));
	getWire().setBendpoint(getIndex(), bp);
	super.execute();
}

protected Bendpoint getOldBendpoint() {
	return oldBendpoint;
}

public void setOldBendpoint(Bendpoint bp) {
	oldBendpoint = bp;
}

public void undo() {
	super.undo();
	getWire().setBendpoint(getIndex(), getOldBendpoint());
}

}


