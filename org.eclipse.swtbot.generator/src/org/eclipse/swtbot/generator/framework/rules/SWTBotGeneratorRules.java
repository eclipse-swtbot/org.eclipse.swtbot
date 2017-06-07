/*******************************************************************************
 * Copyright (c) 2014 Red Hat Inc..
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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swtbot.generator.SWTBotGeneratorPlugin;
import org.eclipse.swtbot.generator.framework.AnnotationRule;
import org.eclipse.swtbot.generator.framework.GenerationComplexRule;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.Generator;
import org.eclipse.swtbot.generator.framework.rules.annotation.TestAnnotationRule;
import org.eclipse.swtbot.generator.framework.rules.complex.ModifyComboComplexRule;
import org.eclipse.swtbot.generator.framework.rules.complex.ModifyStyledTextComplexRule;
import org.eclipse.swtbot.generator.framework.rules.complex.ModifyTextComplexRule;
import org.eclipse.swtbot.generator.framework.rules.complex.ToolBarMenuComplexRule;
import org.eclipse.swtbot.generator.framework.rules.simple.CComboSelectionRule;
import org.eclipse.swtbot.generator.framework.rules.simple.CTabItemActivateRule;
import org.eclipse.swtbot.generator.framework.rules.simple.CheckboxClickedRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ComboSelectionRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ComboTextModifyRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ContextMenuRule;
import org.eclipse.swtbot.generator.framework.rules.simple.DoubleClickTreeItemRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ExpandTreeItemRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ModifyStyledTextRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ModifyTextRule;
import org.eclipse.swtbot.generator.framework.rules.simple.PressShortCutRule;
import org.eclipse.swtbot.generator.framework.rules.simple.PushButtonClickedRule;
import org.eclipse.swtbot.generator.framework.rules.simple.RadioButtonClickedRule;
import org.eclipse.swtbot.generator.framework.rules.simple.SelectListItemRule;
import org.eclipse.swtbot.generator.framework.rules.simple.SelectTreeItemRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ShellMenuClickedRule;
import org.eclipse.swtbot.generator.framework.rules.simple.TabItemActivateRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ToolBarDropDownRule;
import org.eclipse.swtbot.generator.framework.rules.simple.ToolBarItemClickedRule;

public class SWTBotGeneratorRules implements Generator {

	private static final String RULES_EXTENSION_POINT = "org.eclipse.swtbot.generator.rules.additions"; //$NON-NLS-1$
	
	@Override
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
		res.add(new ModifyStyledTextRule());
		res.add(new ComboTextModifyRule());
		res.add(new ContextMenuRule());
		res.add(new ToolBarDropDownRule());
		res.add(new ToolBarItemClickedRule());
		res.add(new SelectListItemRule());
		res.add(new CTabItemActivateRule());
		res.add(new TabItemActivateRule());

		res.add(new PressShortCutRule());
		
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		if (registry != null) {
			for (IConfigurationElement element : registry.getConfigurationElementsFor(RULES_EXTENSION_POINT)) {
				if (element.getName().equals("simpleRule")) {
					try {
						GenerationSimpleRule rule = (GenerationSimpleRule)element.createExecutableExtension("class");
						res.add(rule);
					} catch (CoreException ex) {
						SWTBotGeneratorPlugin.getDefault().getLog().log(
							new Status(
									IStatus.ERROR,
									element.getContributor().getName(),
									ex.getMessage(),
									ex));
					}
				}
			}
		}

		return res;

	}

	@Override
	public String getLabel() {
		return "SWTBot";
	}

	@Override
	public List<GenerationComplexRule> createComplexRules() {
		List<GenerationComplexRule> cres = new ArrayList<GenerationComplexRule>();
		cres.add(new ToolBarMenuComplexRule());
		cres.add(new ModifyTextComplexRule());
		cres.add(new ModifyStyledTextComplexRule());
		cres.add(new ModifyComboComplexRule());
		
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		if (registry != null) {
			for (IConfigurationElement element : registry.getConfigurationElementsFor(RULES_EXTENSION_POINT)) {
				if (element.getName().equals("complexRule")) {
					try {
						GenerationComplexRule rule = (GenerationComplexRule)element.createExecutableExtension("class");
						cres.add(rule);
					} catch (CoreException ex) {
						SWTBotGeneratorPlugin.getDefault().getLog().log(
							new Status(
									IStatus.ERROR,
									element.getContributor().getName(),
									ex.getMessage(),
									ex));
					}
				}
			}
		}
		return cres;
	}

	@Override
	public List<AnnotationRule> createAnnotationRules() {
		List<AnnotationRule> ares = new ArrayList<AnnotationRule>();
		ares.add(new TestAnnotationRule());
		return ares;
	}

	@Override
	public Image getImage() {
		// TODO SWTBot has no logo ?
		return null;
	}
}
