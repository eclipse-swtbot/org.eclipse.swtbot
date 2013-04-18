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

/**
 * This class represents abstract GenerationRule
 *
 */
public abstract class GenerationRule {
	
	/**
	 * Generates code
	 * @return String of generated code
	 */
	public String generateCode() {
		if(getWidgetAccessor() !=null && getAction() != null){
			return getWidgetAccessor() + getAction();
		}
		return null;
	}
	
	/**
	 * 
	 * @return String of code for accessing Widget
	 */
	public abstract String getWidgetAccessor();
	
	/**
	 * 
	 * @return String of code to call action on widget
	 */
	public abstract String getAction();
}