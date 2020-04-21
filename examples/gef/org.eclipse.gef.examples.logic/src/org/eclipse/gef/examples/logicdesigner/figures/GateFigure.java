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
package org.eclipse.gef.examples.logicdesigner.figures;

import org.eclipse.gef.examples.logicdesigner.model.Gate;

public class GateFigure  
	extends OutputFigure 
{

public GateFigure() {
	FixedConnectionAnchor inputConnectionAnchorA, inputConnectionAnchorB;
	inputConnectionAnchorA = new FixedConnectionAnchor(this);
	inputConnectionAnchorA.offsetH = 4;
	inputConnectionAnchorB = new FixedConnectionAnchor(this);
	inputConnectionAnchorB.offsetH =10;
	inputConnectionAnchors.addElement(inputConnectionAnchorA);
	inputConnectionAnchors.addElement(inputConnectionAnchorB);
	connectionAnchors.put(Gate.TERMINAL_A, inputConnectionAnchorA);
	connectionAnchors.put(Gate.TERMINAL_B, inputConnectionAnchorB);
}

public String toString(){
	return "GateFigure"; //$NON-NLS-1$
}

}
