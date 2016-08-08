/*******************************************************************************
 * Copyright (c) 2016 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Isaac Arvestad (Ericsson) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.client;

/**
 * An interface for objects interested in receiving generated code as it is
 * received.
 */
public interface RecorderClientCodeListener {
	/**
	 * Called when new code has been received.
	 * 
	 * @param code
	 *            The generated code.
	 */
	public void codeGenerated(String code);
}
