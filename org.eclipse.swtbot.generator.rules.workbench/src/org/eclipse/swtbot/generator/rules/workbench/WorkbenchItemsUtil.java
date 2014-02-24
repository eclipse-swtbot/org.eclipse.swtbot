/*******************************************************************************
 * Copyright (c) 2014 Red Hat Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.rules.workbench;

import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewReference;

public class WorkbenchItemsUtil {

	public static String getWidgetAccessor(IEditorReference editor) {
		StringBuilder res = new StringBuilder("bot.");
		res.append("editorByTitle(\"");
		res.append(editor.getTitle());
		res.append("\")");
		return res.toString();
	}
	
	public static String getWidgetAccessor(IViewReference view) {
		StringBuilder res = new StringBuilder("bot.");
		res.append("viewByTitle(\"");
		res.append(view.getTitle());
		res.append("\")");
		return res.toString();
	}
}
