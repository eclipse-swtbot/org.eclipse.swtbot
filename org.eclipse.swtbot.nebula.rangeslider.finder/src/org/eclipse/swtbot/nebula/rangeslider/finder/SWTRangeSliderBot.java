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
package org.eclipse.swtbot.nebula.rangeslider.finder;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.inGroup;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withId;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withLabel;

import org.eclipse.nebula.widgets.opal.rangeslider.RangeSlider;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.nebula.rangeslider.finder.widgets.SWTBotRangeSlider;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.hamcrest.Matcher;
public class SWTRangeSliderBot extends SWTBot {

	public SWTRangeSliderBot() {
		super();
	}

	/**
	 * Constructs a bot based on the given parent Widget.
	 * @param parent the parent widget
	 * @since 4.0
	 */
	public SWTRangeSliderBot(Widget parent) {
		super(parent);
	}

	/**
	 * Constructs a bot based on the given parent SWTBot.
	 * @param bot the parent SWTBot
	 * @since 4.0 
	 */
	public SWTRangeSliderBot(SWTBot bot) {
		super(bot.getFinder());
	}

	/**
	 * @return a {@link SWTBotRangeSlider} with the specified <code>none</code>.
	 */
	public SWTBotRangeSlider rangeSlider() {
		return rangeSlider(0);
	}

	/**
	 * @param label the label on the widget.
	 * @return a {@link SWTBotRangeSlider} with the specified <code>label</code>.
	 */
	public SWTBotRangeSlider rangeSliderWithLabel(String label) {
		return rangeSliderWithLabel(label, 0);
	}

	/**
	 * @param key   the key set on the widget.
	 * @param value the value for the key.
	 * @return a {@link SWTBotRangeSlider} with the specified
	 *         <code>key/value</code>.
	 */
	public SWTBotRangeSlider rangeSliderWithId(String key, String value) {
		return rangeSliderWithId(key, value, 0);
	}

	/**
	 * @param key   the key set on the widget.
	 * @param value the value for the key.
	 * @param index the index of the widget.
	 * @return a {@link SWTBotRangeSlider} with the specified
	 *         <code>key/value</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotRangeSlider rangeSliderWithId(String key, String value, int index) {
		Matcher matcher = allOf(widgetOfType(Button.class), withId(key, value));
		return new SWTBotRangeSlider((RangeSlider) widget(matcher, index), matcher);
	}

	/**
	 * @param value the value for the key
	 *              {@link org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences#DEFAULT_KEY}
	 *              .
	 * @return a {@link SWTBotRangeSlider} with the specified <code>value</code>.
	 */
	public SWTBotRangeSlider rangeSliderWithId(String value) {
		return rangeSliderWithId(value, 0);
	}

	/**
	 * @param value the value for the key
	 *              {@link org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences#DEFAULT_KEY}
	 *              .
	 * @param index the index of the widget.
	 * @return a {@link SWTBotRangeSlider} with the specified <code>value</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotRangeSlider rangeSliderWithId(String value, int index) {
		Matcher matcher = allOf(widgetOfType(RangeSlider.class), withId(value));
		return new SWTBotRangeSlider((RangeSlider) widget(matcher, index), matcher);
	}

	/**
	 * @param inGroup the inGroup on the widget.
	 * @return a {@link SWTBotRangeSlider} with the specified <code>inGroup</code>.
	 */
	public SWTBotRangeSlider rangeSliderInGroup(String inGroup) {
		return rangeSliderInGroup(inGroup, 0);
	}

	/**
	 * @param inGroup the inGroup on the widget.
	 * @param index   the index of the widget.
	 * @return a {@link SWTBotRangeSlider} with the specified <code>inGroup</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotRangeSlider rangeSliderInGroup(String inGroup, int index) {
		Matcher matcher = allOf(widgetOfType(RangeSlider.class), inGroup(inGroup));
		return new SWTBotRangeSlider((RangeSlider) widget(matcher, index), matcher);
	}

	/**
	 * @param index the index of the widget.
	 * @return a {@link SWTBotRangeSlider} with the specified <code>none</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotRangeSlider rangeSlider(int index) {
		Matcher matcher = allOf(widgetOfType(RangeSlider.class));
		return new SWTBotRangeSlider((RangeSlider) widget(matcher, index), matcher);
	}

	/**
	 * @param label   the label on the widget.
	 * @param inGroup the inGroup on the widget.
	 * @return a {@link SWTBotRangeSlider} with the specified <code>label</code>
	 *         with the specified <code>inGroup</code>.
	 */
	public SWTBotRangeSlider rangeSliderWithLabelInGroup(String label, String inGroup) {
		return rangeSliderWithLabelInGroup(label, inGroup, 0);
	}

	/**
	 * @param label   the label on the widget.
	 * @param inGroup the inGroup on the widget.
	 * @param index   the index of the widget.
	 * @return a {@link SWTBotRangeSlider} with the specified <code>label</code>
	 *         with the specified <code>inGroup</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotRangeSlider rangeSliderWithLabelInGroup(String label, String inGroup, int index) {
		Matcher matcher = allOf(widgetOfType(RangeSlider.class), withLabel(label), inGroup(inGroup));
		return new SWTBotRangeSlider((RangeSlider) widget(matcher, index), matcher);
	}

	/**
	 * @param label the label on the widget.
	 * @param index the index of the widget.
	 * @return a {@link SWTBotRangeSlider} with the specified <code>label</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotRangeSlider rangeSliderWithLabel(String label, int index) {
		Matcher matcher = allOf(widgetOfType(RangeSlider.class), withLabel(label));
		return new SWTBotRangeSlider((RangeSlider) widget(matcher, index), matcher);
	}

}
