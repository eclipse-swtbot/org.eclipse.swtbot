/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Rastislav Wagner (Red Hat)
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework;

import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.generator.ui.BotGeneratorEventDispatcher;

public interface IRecorderDialog {
	/**
	 * 
	 * @return shell dialog's shell
	 */
	public Shell getShell();
	/**
	 * 
	 * @param availableGenerators generators which are available
	 */
	public void setAvailableGenerators(List<Generator> availableGenerators);
	/**
	 * Sets event dispatcher
	 * @param recorder
	 */
	public void setRecorder(BotGeneratorEventDispatcher recorder);
	
	/**
	 * Opens dialog
	 */
	public int open();
	/**
	 * 
	 * @return shells which events should be ignored
	 */
	public List<Shell> getIgnoredShells();
	/**
	 * 
	 * @return dialog name
	 */
	public String getName();
	/**
	 * 
	 * @return unique dialog ID
	 */
	public String getId();
}
