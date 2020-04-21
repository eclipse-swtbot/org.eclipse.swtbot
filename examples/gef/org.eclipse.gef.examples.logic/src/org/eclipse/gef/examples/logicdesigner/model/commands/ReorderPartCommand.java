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

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;

public class ReorderPartCommand extends Command {

private int oldIndex, newIndex;
private LogicSubpart child;
private LogicDiagram parent;

public ReorderPartCommand(LogicSubpart child, LogicDiagram parent, int newIndex ) {
	super(LogicMessages.ReorderPartCommand_Label);
	this.child = child;
	this.parent = parent;
	this.newIndex = newIndex;
}

public void execute() {
	oldIndex = parent.getChildren().indexOf(child);
	parent.removeChild(child);
	parent.addChild(child, newIndex);
}

public void undo() {
	parent.removeChild(child);
	parent.addChild(child, oldIndex);
}

}
