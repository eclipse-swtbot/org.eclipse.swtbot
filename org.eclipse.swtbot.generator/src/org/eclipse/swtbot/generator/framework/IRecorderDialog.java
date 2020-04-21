/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Rastislav Wagner (Red Hat)
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework;

import java.util.List;

import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.generator.ui.BotGeneratorEventDispatcher;

public interface IRecorderDialog extends IShellProvider {

	/**
	 *
	 * @param availableGenerators generators which are available
	 */
	void setAvailableGenerators(List<Generator> availableGenerators);

	/**
	 * Sets event dispatcher
	 * @param recorder
	 */
	void setRecorder(BotGeneratorEventDispatcher recorder);


	/**
	 * Returns generator
	 * @return current generator
	 */
	BotGeneratorEventDispatcher getRecorder();

	/**
	 * Opens dialog
	 */
	int open();

	/**
	 * Returns list of available generators
	 * @return list of generators
	 */
	List<Generator> getAvailableGenerators();

	/**
	 *
	 * @return shells which events should be ignored
	 */
	List<Shell> getIgnoredShells();

	/**
	 *
	 * @return dialog name
	 */
	String getName();

	/**
	 *
	 * @return unique dialog ID
	 */
	String getId();
}
