/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Rastislav Wagner (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework;

import java.util.List;


/**
 * This class represents complex rule which is being matched to two or more Simple Rules
 *
 */
public abstract class GenerationComplexRule extends GenerationRule{

	/**
	 * Checks whether given GenerationSimpleRule applies to complex rule
	 * @param rule to match with complex rule
	 * @param i which rule from complex rule to match
	 * @return true if given SimpleRule applies to i-indexed rule
	 */
	public abstract boolean appliesTo(GenerationSimpleRule rule, int i);
	
	/**
	 * 
	 * @return list of simple rules defining this complex rule
	 */
	public abstract List<GenerationSimpleRule> getRules();
	
	/**
	 * Initializes complex rule for given simple rules
	 * @param rules to initialize complex rule for
	 */
	public abstract void initializeForRules(List<GenerationSimpleRule> rules) ;
	
}
