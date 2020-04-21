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
package org.eclipse.gef.examples.logicdesigner.actions;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.ui.IEditorPart;

/**
 * @author Eric Bordeau
 */
public class LogicPasteTemplateAction extends PasteTemplateAction {

/**
 * Constructor for LogicPasteTemplateAction.
 * @param editor
 */
public LogicPasteTemplateAction(IEditorPart editor) {
	super(editor);
}

/**
 * @see PasteTemplateAction#getPasteLocation(GraphicalEditPart)
 */
protected Point getPasteLocation(GraphicalEditPart container) {
	Point result = new Point(10, 10);
	IFigure fig = container.getContentPane();
	result.translate(fig.getClientArea(Rectangle.SINGLETON).getLocation());
	fig.translateToAbsolute(result);
	return result;
}

}
