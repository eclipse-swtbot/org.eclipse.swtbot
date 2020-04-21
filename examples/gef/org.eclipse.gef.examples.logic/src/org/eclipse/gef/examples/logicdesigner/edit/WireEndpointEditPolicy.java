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

import org.eclipse.draw2d.PolylineConnection;

import org.eclipse.gef.GraphicalEditPart;

public class WireEndpointEditPolicy
	extends org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy 
{

protected void addSelectionHandles(){
	super.addSelectionHandles();
	getConnectionFigure().setLineWidth(2);
}

protected PolylineConnection getConnectionFigure(){
	return (PolylineConnection)((GraphicalEditPart)getHost()).getFigure();
}

protected void removeSelectionHandles(){
	super.removeSelectionHandles();
	getConnectionFigure().setLineWidth(0);
}

}
