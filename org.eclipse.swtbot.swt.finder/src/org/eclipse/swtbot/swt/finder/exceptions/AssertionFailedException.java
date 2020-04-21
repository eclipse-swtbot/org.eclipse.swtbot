/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
package org.eclipse.swtbot.swt.finder.exceptions;

/**
 * Copy of {@link org.eclipse.core.runtime.AssertionFailedException}, moved here to reduce dependency on
 * org.eclipse.equinox.common.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public class AssertionFailedException extends RuntimeException {

	/**
	 * All serializable objects should have a stable serialVersionUID
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * Constructs a new exception with the given message.
	 * 
	 * @param detail the message
	 */
	public AssertionFailedException(String detail) {
		super(detail);
	}
}
