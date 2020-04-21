/*******************************************************************************
 * Copyright (c) 2016 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Isaac Arvestad (Ericsson) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.client;

/**
 * An interface for objects interested in connection status between recorder
 * server and client.
 */
public interface RecorderClientStatusListener {

	/**
	 * Called when a connection between server and client has started.
	 */
	public void connectionStarted();

	/**
	 * Called when a connection between server and client has ended.
	 */
	public void connectionEnded();
}
