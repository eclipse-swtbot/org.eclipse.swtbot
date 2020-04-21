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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import org.eclipse.gef.examples.logicdesigner.model.LED;
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;

public class TreePartFactory
	implements EditPartFactory
{

public EditPart createEditPart(EditPart context, Object model) {
	if (model instanceof LED)
		return new LogicTreeEditPart(model);
	if (model instanceof LogicDiagram)
		return new LogicContainerTreeEditPart(model);
	return new LogicTreeEditPart(model);
}

}
