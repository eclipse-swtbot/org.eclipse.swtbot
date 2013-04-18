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
package org.eclipse.swtbot.generator.framework.rules.complex;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.generator.framework.GenerationComplexRule;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ContextMenuRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ToolBarDropDownRule;

public class ToolBarMenuComplexRule extends GenerationComplexRule{
	
	private List<GenerationSimpleRule> rules;
	private List<GenerationSimpleRule> initializedRules;
	
	public ToolBarMenuComplexRule(){
		rules = new ArrayList<GenerationSimpleRule>();
		
		ToolBarDropDownRule toolBar = new ToolBarDropDownRule();
		ContextMenuRule menu = new ContextMenuRule();
		
		rules.add(toolBar);
		rules.add(menu);
	}

	@Override
	public List<GenerationSimpleRule> getRules() {
		return rules;
		

	}
	
	@Override
	public String getWidgetAccessor(){
		StringBuilder builder = new StringBuilder();
		builder.append(((ToolBarDropDownRule)initializedRules.get(0)).getWidgetAccessor());
		ContextMenuRule cmr = ((ContextMenuRule)initializedRules.get(1));
		for(String s: cmr.getPath()){
			builder.append(".menuItem(\""+s+"\")");
		}
		builder.append(".menuItem(\""+cmr.getMenu()+"\")");
		return builder.toString();
	}
	
	@Override
	public String getAction(){
		return ".click()";
	}
	

	@Override
	public boolean appliesTo(GenerationSimpleRule rule, int i) {
		return rules.get(i).getClass().equals(rule.getClass()); 
	}

	@Override
	public void initializeForRules(List<GenerationSimpleRule> rules) {
		this.initializedRules=rules;
		
	}

}

