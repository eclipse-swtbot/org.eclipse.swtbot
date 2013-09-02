/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *    Rastislav Wagner (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework;

import java.util.List;

/**
 * This class represents abstract GenerationRule
 *
 */
public abstract class GenerationRule {
	
	/**
	 * 
	 * @return list of actions done by this rule
	 */
	public abstract List<String> getActions();
	
	/**
	 * 
	 * @return list of imports needed by this rule
	 */
	public abstract List<String> getImports();
}