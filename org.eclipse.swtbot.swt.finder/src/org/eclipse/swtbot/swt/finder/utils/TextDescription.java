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

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;

/**
 * Writes a fixed string as a description.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class TextDescription implements SelfDescribing {

	private final String	description;

	/**
	 * @param description the description returned as is.
	 */
	public TextDescription(String description) {
		this.description = description;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(this.description);
	}

}
