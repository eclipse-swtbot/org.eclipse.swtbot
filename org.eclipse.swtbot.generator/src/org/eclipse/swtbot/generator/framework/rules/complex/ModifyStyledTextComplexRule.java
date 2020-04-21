/*******************************************************************************
 * Copyright (c) 2014 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - copied and adapted from ModifyTextComplexRule
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules.complex;

import java.util.List;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.generator.framework.GenerationComplexRule;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ModifyStyledTextRule;

public class ModifyStyledTextComplexRule extends GenerationComplexRule {


	private Widget widget;

	@Override
	public boolean appliesToPartially(GenerationSimpleRule rule, int i) {
		if (! (rule instanceof ModifyStyledTextRule)) {
			return false;
		}
		if (i == 0) {
			this.widget = rule.getWidget();
		}
		return this.widget.equals(rule.getWidget());
	}

	@Override
	public boolean appliesTo(List<GenerationSimpleRule> rules) {
		Widget widget = null;
		for (GenerationSimpleRule rule : rules) {
			if (! (rule instanceof ModifyStyledTextRule)) {
				return false;
			}
			if (widget == null) {
				widget = rule.getWidget();
			} else if (!this.widget.equals(rule.getWidget())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<String> getActions() {
		return getInitializationRules().get(getInitializationRules().size()-1).getActions();
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}
}
