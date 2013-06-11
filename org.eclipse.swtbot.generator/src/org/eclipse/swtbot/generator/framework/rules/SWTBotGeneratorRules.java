/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.generator.framework.GenerationComplexRule;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.Generator;
import org.eclipse.swtbot.generator.framework.rules.complex.ModifyComboComplexRule;
import org.eclipse.swtbot.generator.framework.rules.complex.ModifyTextComplexRule;
import org.eclipse.swtbot.generator.framework.rules.complex.ToolBarMenuComplexRule;
import org.eclipse.swtbot.generator.framework.rules.simple.CComboSelectionRule;
import org.eclipse.swtbot.generator.framework.rules.simple.CheckboxClickedRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ComboSelectionRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ComboTextModifyRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ContextMenuRule;
import org.eclipse.swtbot.generator.framework.rules.simple.DoubleClickTreeItemRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ExpandTreeItemRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ModifyTextRule;
import org.eclipse.swtbot.generator.framework.rules.simple.PushButtonClickedRule;
import org.eclipse.swtbot.generator.framework.rules.simple.RadioButtonClickedRule;
import org.eclipse.swtbot.generator.framework.rules.simple.SelectTreeItemRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ShellMenuClickedRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ToolBarDropDownRule;

public class SWTBotGeneratorRules implements Generator {

	public List<GenerationSimpleRule> createSimpleRules() {
		List<GenerationSimpleRule> res = new ArrayList<GenerationSimpleRule>();

		res.add(new PushButtonClickedRule());
		res.add(new CheckboxClickedRule());
		res.add(new RadioButtonClickedRule());
		res.add(new ComboSelectionRule());
		res.add(new CComboSelectionRule());
		res.add(new ExpandTreeItemRule());
		res.add(new DoubleClickTreeItemRule());
		res.add(new ShellMenuClickedRule());
		res.add(new SelectTreeItemRule());
		res.add(new ModifyTextRule());
		res.add(new ComboTextModifyRule());
		res.add(new ContextMenuRule());
		res.add(new ToolBarDropDownRule());

		return res;

	}

	public String getLabel() {
		return "SWTBot";
	}

	public List<GenerationComplexRule> createComplexRules() {
		List<GenerationComplexRule> cres = new ArrayList<GenerationComplexRule>();
		cres.add(new ToolBarMenuComplexRule());
		cres.add(new ModifyTextComplexRule());
		cres.add(new ModifyComboComplexRule());
		return cres;
	}
}