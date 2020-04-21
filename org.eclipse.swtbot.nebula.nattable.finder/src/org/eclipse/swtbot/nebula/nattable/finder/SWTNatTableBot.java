/*******************************************************************************
 * Copyright (c) 2016 Cadence Design Systems, Inc. and others.
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
package org.eclipse.swtbot.nebula.nattable.finder;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.inGroup;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withId;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withLabel;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.nebula.nattable.finder.widgets.SWTBotNatTable;
import org.hamcrest.Matcher;

public class SWTNatTableBot extends SWTWorkbenchBot {

	/**
	 * @return a {@link SWTBotNatTable} with the specified <code>none</code>.
	 */
	public SWTBotNatTable nattable() {
		return nattable(0);
	}

	/**
	 * @param label
	 *            the label on the widget.
	 * @return a {@link SWTBotNatTable} with the specified <code>label</code>.
	 */
	public SWTBotNatTable nattableWithLabel(String label) {
		return nattableWithLabel(label, 0);
	}

	/**
	 * @param key
	 *            the key set on the widget.
	 * @param value
	 *            the value for the key.
	 * @return a {@link SWTBotNatTable} with the specified
	 *         <code>key/value</code>.
	 */
	public SWTBotNatTable nattableWithId(String key, String value) {
		return nattableWithId(key, value, 0);
	}

	/**
	 * @param key
	 *            the key set on the widget.
	 * @param value
	 *            the value for the key.
	 * @param index
	 *            the index of the widget.
	 * @return a {@link SWTBotNatTable} with the specified
	 *         <code>key/value</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotNatTable nattableWithId(String key, String value, int index) {
		Matcher matcher = allOf(widgetOfType(NatTable.class), withId(key, value));
		return new SWTBotNatTable((NatTable) widget(matcher, index), matcher);
	}

	/**
	 * @param value
	 *            the value for the key
	 *            {@link org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences#DEFAULT_KEY}
	 *            .
	 * @return a {@link SWTBotNatTable} with the specified <code>value</code>.
	 */
	public SWTBotNatTable nattableWithId(String value) {
		return nattableWithId(value, 0);
	}

	/**
	 * @param value
	 *            the value for the key
	 *            {@link org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences#DEFAULT_KEY}
	 *            .
	 * @param index
	 *            the index of the widget.
	 * @return a {@link SWTBotNatTable} with the specified <code>value</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotNatTable nattableWithId(String value, int index) {
		Matcher matcher = allOf(widgetOfType(NatTable.class), withId(value));
		return new SWTBotNatTable((NatTable) widget(matcher, index), matcher);
	}

	/**
	 * @param inGroup
	 *            the inGroup on the widget.
	 * @return a {@link SWTBotNatTable} with the specified <code>inGroup</code>.
	 */
	public SWTBotNatTable nattableInGroup(String inGroup) {
		return nattableInGroup(inGroup, 0);
	}

	/**
	 * @param inGroup
	 *            the inGroup on the widget.
	 * @param index
	 *            the index of the widget.
	 * @return a {@link SWTBotNatTable} with the specified <code>inGroup</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotNatTable nattableInGroup(String inGroup, int index) {
		Matcher matcher = allOf(widgetOfType(NatTable.class), inGroup(inGroup));
		return new SWTBotNatTable((NatTable) widget(matcher, index), matcher);
	}

	/**
	 * @param index
	 *            the index of the widget.
	 * @return a {@link SWTBotNatTable} with the specified <code>none</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotNatTable nattable(int index) {
		Matcher matcher = allOf(widgetOfType(NatTable.class));
		return new SWTBotNatTable((NatTable) widget(matcher, index), matcher);
	}

	/**
	 * @param label
	 *            the label on the widget.
	 * @param inGroup
	 *            the inGroup on the widget.
	 * @return a {@link SWTBotNatTable} with the specified <code>label</code>
	 *         with the specified <code>inGroup</code>.
	 */
	public SWTBotNatTable nattableWithLabelInGroup(String label, String inGroup) {
		return nattableWithLabelInGroup(label, inGroup, 0);
	}

	/**
	 * @param label
	 *            the label on the widget.
	 * @param inGroup
	 *            the inGroup on the widget.
	 * @param index
	 *            the index of the widget.
	 * @return a {@link SWTBotNatTable} with the specified <code>label</code>
	 *         with the specified <code>inGroup</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotNatTable nattableWithLabelInGroup(String label, String inGroup, int index) {
		Matcher matcher = allOf(widgetOfType(NatTable.class), withLabel(label), inGroup(inGroup));
		return new SWTBotNatTable((NatTable) widget(matcher, index), matcher);
	}

	/**
	 * @param label
	 *            the label on the widget.
	 * @param index
	 *            the index of the widget.
	 * @return a {@link SWTBotNatTable} with the specified <code>label</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SWTBotNatTable nattableWithLabel(String label, int index) {
		Matcher matcher = allOf(widgetOfType(NatTable.class), withLabel(label));
		return new SWTBotNatTable((NatTable) widget(matcher, index), matcher);
	}
}
