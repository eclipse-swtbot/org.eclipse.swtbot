/*******************************************************************************
 * Copyright (c) 2021 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Aparna Argade(Cadence Design Systems, Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.nebula.checkboxgroup.finder;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.inGroup;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withId;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withLabel;

import org.eclipse.nebula.widgets.opal.checkboxgroup.CheckBoxGroup;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.nebula.checkboxgroup.finder.widgets.SWTBotCheckBoxGroup;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.hamcrest.Matcher;

public class SWTCheckBoxGroupBot extends SWTBot {

	public SWTCheckBoxGroupBot() {
		super();
	}

	/**
	 * Constructs a bot based on the given parent Widget.
	 * @param parent the parent widget
	 * @since 4.0
	 */
	public SWTCheckBoxGroupBot(Widget parent) {
		super(parent);
	}

	/**
	 * Constructs a bot based on the given parent SWTBot.
	 * @param bot the parent SWTBot
	 * @since 4.0 
	 */
	public SWTCheckBoxGroupBot(SWTBot bot) {
		super(bot.getFinder());
	}

	/**
	 * @return a {@link SWTBotCheckBoxGroup} with the specified <code>none</code>.
	 */
	public SWTBotCheckBoxGroup checkBoxGroup() {
		return checkBoxGroup(0);
	}

	/**
	 * @param label the label on the widget.
	 * @return a {@link SWTBotCheckBoxGroup} with the specified <code>label</code>.
	 */
	public SWTBotCheckBoxGroup checkBoxGroupWithLabel(String label) {
		return checkBoxGroupWithLabel(label, 0);
	}

	/**
	 * @param key   the key set on the widget.
	 * @param value the value for the key.
	 * @return a {@link SWTBotCheckBoxGroup} with the specified
	 *         <code>key/value</code>.
	 */
	public SWTBotCheckBoxGroup checkBoxGroupWithId(String key, String value) {
		return checkBoxGroupWithId(key, value, 0);
	}

	/**
	 * @param key   the key set on the widget.
	 * @param value the value for the key.
	 * @param index the index of the widget.
	 * @return a {@link SWTBotCheckBoxGroup} with the specified
	 *         <code>key/value</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotCheckBoxGroup checkBoxGroupWithId(String key, String value, int index) {
		Matcher matcher = allOf(widgetOfType(Button.class), withId(key, value));
		return new SWTBotCheckBoxGroup((CheckBoxGroup) widget(matcher, index), matcher);
	}

	/**
	 * @param value the value for the key
	 *              {@link org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences#DEFAULT_KEY}
	 *              .
	 * @return a {@link SWTBotCheckBoxGroup} with the specified <code>value</code>.
	 */
	public SWTBotCheckBoxGroup checkBoxGroupWithId(String value) {
		return checkBoxGroupWithId(value, 0);
	}

	/**
	 * @param value the value for the key
	 *              {@link org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences#DEFAULT_KEY}
	 *              .
	 * @param index the index of the widget.
	 * @return a {@link SWTBotCheckBoxGroup} with the specified <code>value</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotCheckBoxGroup checkBoxGroupWithId(String value, int index) {
		Matcher matcher = allOf(widgetOfType(CheckBoxGroup.class), withId(value));
		return new SWTBotCheckBoxGroup((CheckBoxGroup) widget(matcher, index), matcher);
	}

	/**
	 * @param inGroup the inGroup on the widget.
	 * @return a {@link SWTBotCheckBoxGroup} with the specified
	 *         <code>inGroup</code>.
	 */
	public SWTBotCheckBoxGroup checkBoxGroupInGroup(String inGroup) {
		return checkBoxGroupInGroup(inGroup, 0);
	}

	/**
	 * @param inGroup the inGroup on the widget.
	 * @param index   the index of the widget.
	 * @return a {@link SWTBotCheckBoxGroup} with the specified
	 *         <code>inGroup</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotCheckBoxGroup checkBoxGroupInGroup(String inGroup, int index) {
		Matcher matcher = allOf(widgetOfType(CheckBoxGroup.class), inGroup(inGroup));
		return new SWTBotCheckBoxGroup((CheckBoxGroup) widget(matcher, index), matcher);
	}

	/**
	 * @param index the index of the widget.
	 * @return a {@link SWTBotCheckBoxGroup} with the specified <code>none</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotCheckBoxGroup checkBoxGroup(int index) {
		Matcher matcher = allOf(widgetOfType(CheckBoxGroup.class));
		return new SWTBotCheckBoxGroup((CheckBoxGroup) widget(matcher, index), matcher);
	}

	/**
	 * @param label   the label on the widget.
	 * @param inGroup the inGroup on the widget.
	 * @return a {@link SWTBotCheckBoxGroup} with the specified <code>label</code>
	 *         with the specified <code>inGroup</code>.
	 */
	public SWTBotCheckBoxGroup checkBoxGroupWithLabelInGroup(String label, String inGroup) {
		return checkBoxGroupWithLabelInGroup(label, inGroup, 0);
	}

	/**
	 * @param label   the label on the widget.
	 * @param inGroup the inGroup on the widget.
	 * @param index   the index of the widget.
	 * @return a {@link SWTBotCheckBoxGroup} with the specified <code>label</code>
	 *         with the specified <code>inGroup</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotCheckBoxGroup checkBoxGroupWithLabelInGroup(String label, String inGroup, int index) {
		Matcher matcher = allOf(widgetOfType(CheckBoxGroup.class), withLabel(label), inGroup(inGroup));
		return new SWTBotCheckBoxGroup((CheckBoxGroup) widget(matcher, index), matcher);
	}

	/**
	 * @param label the label on the widget.
	 * @param index the index of the widget.
	 * @return a {@link SWTBotCheckBoxGroup} with the specified <code>label</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotCheckBoxGroup checkBoxGroupWithLabel(String label, int index) {
		Matcher matcher = allOf(widgetOfType(CheckBoxGroup.class), withLabel(label));
		return new SWTBotCheckBoxGroup((CheckBoxGroup) widget(matcher, index), matcher);
	}

}
