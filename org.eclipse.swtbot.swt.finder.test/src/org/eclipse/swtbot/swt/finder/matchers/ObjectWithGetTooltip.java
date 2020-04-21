/*******************************************************************************
 * Copyright (c) 2016 Stephane Bouchet (Intel Corporation).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Stephane Bouchet (Intel Corporation) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.matchers;

public class ObjectWithGetTooltip {
	private final String	text;

	/**
	 * @param text
	 */
	public ObjectWithGetTooltip(String text) {
		this.text = text;
	}

	public String getToolTipText() {
		return text;
	}
}
