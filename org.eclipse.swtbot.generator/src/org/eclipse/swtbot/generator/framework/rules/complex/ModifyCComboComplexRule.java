/*******************************************************************************
 * Copyright (c) 2017 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Aparna Argade - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules.complex;

import java.util.List;

import org.eclipse.swtbot.generator.framework.GenerationComplexRule;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.rules.simple.CComboTextModifyRule;

public class ModifyCComboComplexRule extends GenerationComplexRule{

	private int textIndex;

	@Override
	public boolean appliesToPartially(GenerationSimpleRule rule, int i) {
		if(i==0 &&  rule instanceof CComboTextModifyRule){ //first event, so we will store some identifier of text
			this.textIndex = ((CComboTextModifyRule)rule).getTextIndex();
		}
		//check whether CComboTextModifyRule happened on the same text as the first one
		//if not then it should be processed by another ModifyTextComplexRule
		return rule instanceof CComboTextModifyRule && ((CComboTextModifyRule)rule).getTextIndex() == textIndex;
	}

	@Override
	public boolean appliesTo(List<GenerationSimpleRule> rules) {
		for(GenerationSimpleRule r: rules){
			if (!(r instanceof CComboTextModifyRule)) {
				return false;
			} else if (((CComboTextModifyRule)r).getTextIndex() != this.textIndex){
				return false;
			}
		}
		return true;
	}

	@Override
	public List<String> getActions() {
		return ((CComboTextModifyRule)getInitializationRules().get(getInitializationRules().size()-1)).getActions();
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

}
