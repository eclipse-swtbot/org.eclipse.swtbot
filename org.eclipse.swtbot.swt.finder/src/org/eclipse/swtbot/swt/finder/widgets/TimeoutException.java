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
package org.eclipse.swtbot.swt.finder.widgets;

/**
 * This is an exception that is thrown when a timeout occurs waiting for something (e.g. a condition) to complete.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class TimeoutException extends RuntimeException {

	/** the serialVersionUID */
	private static final long	serialVersionUID	= -4097219100771019730L;

	/**
	 * Constructs the exception with the given message.
	 * 
	 * @param message the message.
	 */
	public TimeoutException(String message) {
		super(message);
	}
}
