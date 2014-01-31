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

import java.util.List;

import org.eclipse.swtbot.generator.framework.GenerationComplexRule;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ComboTextModifyRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ModifyTextRule;

public class ModifyComboComplexRule extends GenerationComplexRule{

	private int textIndex;

	@Override
	public boolean appliesToPartially(GenerationSimpleRule rule, int i) {
		if(i==0 &&  rule instanceof ComboTextModifyRule){ //first event, so we will store some identifier of text
			this.textIndex = ((ComboTextModifyRule)rule).getTextIndex();
		}
		//check whether ComboTextModifyRule happened on the same text as the first one
		//if not then it should be processed by another ModifyTextComplexRule
		return rule instanceof ComboTextModifyRule && ((ComboTextModifyRule)rule).getTextIndex() == textIndex;
	}

	@Override
	public boolean appliesTo(List<GenerationSimpleRule> rules) {
		for(GenerationSimpleRule r: rules){
			if (!(r instanceof ComboTextModifyRule)) {
				return false;
			} else if (((ComboTextModifyRule)r).getTextIndex() != this.textIndex){
				return false;
			}
		}
		return true;
	}

	@Override
	public List<String> getActions() {
		return ((ComboTextModifyRule)getInitializationRules().get(getInitializationRules().size()-1)).getActions();
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

}
