/*******************************************************************************
 * Copyright (c) 2014 Red Hat Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
