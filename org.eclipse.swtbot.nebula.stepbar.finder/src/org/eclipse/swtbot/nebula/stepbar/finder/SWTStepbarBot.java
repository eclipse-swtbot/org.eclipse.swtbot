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
package org.eclipse.swtbot.nebula.stepbar.finder;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.inGroup;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withId;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withLabel;

import org.eclipse.nebula.widgets.stepbar.Stepbar;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.nebula.stepbar.finder.widgets.SWTBotStepbar;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.hamcrest.Matcher;

public class SWTStepbarBot extends SWTBot {

	public SWTStepbarBot() {
		super();
	}

	/**
	 * Constructs a bot based on the given parent Widget.
	 * @param parent the parent widget
	 * @since 4.0
	 */
	public SWTStepbarBot(Widget parent) {
		super(parent);
	}

	/**
	 * Constructs a bot based on the given parent SWTBot.
	 * @param bot the parent SWTBot
	 * @since 4.0 
	 */
	public SWTStepbarBot(SWTBot bot) {
		super(bot.getFinder());
	}

	/**
	 * @return a {@link SWTBotStepbar} with the specified <code>none</code>.
	 */
	public SWTBotStepbar stepbar() {
		return stepbar(0);
	}

	/**
	 * @param label the label on the widget.
	 * @return a {@link SWTBotStepbar} with the specified <code>label</code>.
	 */
	public SWTBotStepbar stepbarWithLabel(String label) {
		return stepbarWithLabel(label, 0);
	}

	/**
	 * @param key   the key set on the widget.
	 * @param value the value for the key.
	 * @return a {@link SWTBotStepbar} with the specified <code>key/value</code>.
	 */
	public SWTBotStepbar stepbarWithId(String key, String value) {
		return stepbarWithId(key, value, 0);
	}

	/**
	 * @param key   the key set on the widget.
	 * @param value the value for the key.
	 * @param index the index of the widget.
	 * @return a {@link SWTBotStepbar} with the specified <code>key/value</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotStepbar stepbarWithId(String key, String value, int index) {
		Matcher matcher = allOf(widgetOfType(Stepbar.class), withId(key, value));
		return new SWTBotStepbar((Stepbar) widget(matcher, index), matcher);
	}

	/**
	 * @param value the value for the key
	 *              {@link org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences#DEFAULT_KEY}
	 *
	 * @return a {@link SWTBotStepbar} with the specified <code>value</code>.
	 */
	public SWTBotStepbar stepbarWithId(String value) {
		return stepbarWithId(value, 0);
	}

	/**
	 * @param value the value for the key
	 *              {@link org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences#DEFAULT_KEY}
	 *
	 * @param index the index of the widget.
	 * @return a {@link SWTBotStepbar} with the specified <code>value</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotStepbar stepbarWithId(String value, int index) {
		Matcher matcher = allOf(widgetOfType(Stepbar.class), withId(value));
		return new SWTBotStepbar((Stepbar) widget(matcher, index), matcher);
	}

	/**
	 * @param inGroup the inGroup on the widget.
	 * @return a {@link SWTBotStepbar} with the specified <code>inGroup</code>.
	 */
	public SWTBotStepbar stepbarInGroup(String inGroup) {
		return stepbarInGroup(inGroup, 0);
	}

	/**
	 * @param inGroup the inGroup on the widget.
	 * @param index   the index of the widget.
	 * @return a {@link SWTBotStepbar} with the specified <code>inGroup</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotStepbar stepbarInGroup(String inGroup, int index) {
		Matcher matcher = allOf(widgetOfType(Stepbar.class), inGroup(inGroup));
		return new SWTBotStepbar((Stepbar) widget(matcher, index), matcher);
	}

	/**
	 * @param index the index of the widget.
	 * @return a {@link SWTBotStepbar} with the specified <code>none</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotStepbar stepbar(int index) {
		Matcher matcher = allOf(widgetOfType(Stepbar.class));
		return new SWTBotStepbar((Stepbar) widget(matcher, index), matcher);
	}

	/**
	 * @param label   the label on the widget.
	 * @param inGroup the inGroup on the widget.
	 * @return a {@link SWTBotStepbar} with the specified <code>label</code> with
	 *         the specified <code>inGroup</code>.
	 */
	public SWTBotStepbar stepbarWithLabelInGroup(String label, String inGroup) {
		return stepbarWithLabelInGroup(label, inGroup, 0);
	}

	/**
	 * @param label   the label on the widget.
	 * @param inGroup the inGroup on the widget.
	 * @param index   the index of the widget.
	 * @return a {@link SWTBotStepbar} with the specified <code>label</code> with
	 *         the specified <code>inGroup</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotStepbar stepbarWithLabelInGroup(String label, String inGroup, int index) {
		Matcher matcher = allOf(widgetOfType(Stepbar.class), withLabel(label), inGroup(inGroup));
		return new SWTBotStepbar((Stepbar) widget(matcher, index), matcher);
	}

	/**
	 * @param label the label on the widget.
	 * @param index the index of the widget.
	 * @return a {@link SWTBotStepbar} with the specified <code>label</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotStepbar stepbarWithLabel(String label, int index) {
		Matcher matcher = allOf(widgetOfType(Stepbar.class), withLabel(label));
		return new SWTBotStepbar((Stepbar) widget(matcher, index), matcher);
	}

}
