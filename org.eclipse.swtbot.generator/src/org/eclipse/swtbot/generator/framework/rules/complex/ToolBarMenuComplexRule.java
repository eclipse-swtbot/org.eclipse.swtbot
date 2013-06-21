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
	
	public ToolBarMenuComplexRule(){
		rules = new ArrayList<GenerationSimpleRule>();
		
		ToolBarDropDownRule toolBar = new ToolBarDropDownRule();
		ContextMenuRule menu = new ContextMenuRule();
		
		rules.add(toolBar);
		rules.add(menu);
	}
	

	@Override
	public boolean appliesToPartially(GenerationSimpleRule rule, int i) {
		if(rules.size() > i){
			return rules.get(i).getClass().equals(rule.getClass());
		}
		return false;
	}

	@Override
	public boolean appliesTo(List<GenerationSimpleRule> rules) {
		if(rules.size() != this.rules.size()){
			return false;
		}
		for(int i=0;i<rules.size();i++){
			if(!this.rules.get(i).getClass().equals(rules.get(i).getClass())){
				return false;
			}
		}
		return true;
	}

	@Override
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		String toolTip = ((ToolBarDropDownRule)getInitializationRules().get(0)).getToolTipText();
		
		builder.append("bot.toolbarDropDownButtonWithToolTip(\""+toolTip+"\")");
		ContextMenuRule cmr = ((ContextMenuRule)getInitializationRules().get(1));
		for(String s: cmr.getPath()){
			builder.append(".menuItem(\""+s+"\")");
		}
		builder.append(".menuItem(\""+cmr.getMenu()+"\")");
		builder.append(".click()");
		actions.add(builder.toString());
		return actions;
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

}

