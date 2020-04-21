/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.utils;

import org.eclipse.swt.SWT;

/**
 * An enum that represents traversal events.
 * 
 * @see SWT#TRAVERSE_TAB_NEXT
 * @see SWT#TRAVERSE_TAB_PREVIOUS
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public enum Traverse {
	/** An event that represents traversal using the TAB key */
	TAB_NEXT(SWT.TRAVERSE_TAB_NEXT),
	/** An event that represents traversal using the SHIFT+TAB key */
	TAB_PREVIOUS(SWT.TRAVERSE_TAB_PREVIOUS);

	public final int	type;

	private Traverse(int type) {
		this.type = type;
	}
}
