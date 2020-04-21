/**
 * Copyright (C) 2010 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Aurelien Pupier <aurelien.pupier@bonitasoft.com> - initial API and implementation
 */
package org.eclipse.swtbot.nebula.gallery.finder;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.inGroup;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withId;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withLabel;

import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.nebula.gallery.finder.widgets.SWTBotGallery;
import org.hamcrest.Matcher;

/**
 * @author Aurelien Pupier
 */
public class SWTNebulaBot extends SWTWorkbenchBot {

	/**
	 * @return a {@link SWTBotGallery} with the specified <code>none</code>.
	 */
	public SWTBotGallery gallery() {
		return gallery(0);
	}
	
	/**
	 * @param label the label on the widget.
	 * @return a {@link SWTBotGallery} with the specified <code>label</code>.
	 */
	public SWTBotGallery galleryWithLabel(String label) {
		return galleryWithLabel(label, 0);
	}

	/**
	 * @param label the label on the widget.
	 * @param index the index of the widget.
	 * @return a {@link SWTBotGallery} with the specified <code>label</code>.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public SWTBotGallery galleryWithLabel(String label, int index) {
		Matcher matcher = allOf(widgetOfType(Gallery.class), withLabel(label));
		return new SWTBotGallery((Gallery) widget(matcher, index), matcher);
	}

	/**
	 * @param key the key set on the widget.
	 * @param value the value for the key.
	 * @return a {@link SWTBotGallery} with the specified <code>key/value</code>.
	 */
	public SWTBotGallery galleryWithId(String key, String value) {
		return galleryWithId(key, value, 0);
	}

	/**
	 * @param key the key set on the widget.
	 * @param value the value for the key.
	 * @param index the index of the widget.
	 * @return a {@link SWTBotGallery} with the specified <code>key/value</code>.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public SWTBotGallery galleryWithId(String key, String value, int index) {
		Matcher matcher = allOf(widgetOfType(Gallery.class), withId(key, value));
		return new SWTBotGallery((Gallery) widget(matcher, index), matcher);
	}

	/**
	 * @param value the value for the key {@link org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences#DEFAULT_KEY}.
	 * @return a {@link SWTBotGallery} with the specified <code>value</code>.
	 */
	public SWTBotGallery galleryWithId(String value) {
		return galleryWithId(value, 0);
	}

	/**
	 * @param value the value for the key {@link org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences#DEFAULT_KEY}.
	 * @param index the index of the widget.
	 * @return a {@link SWTBotGallery} with the specified <code>value</code>.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public SWTBotGallery galleryWithId(String value, int index) {
		Matcher matcher = allOf(widgetOfType(Gallery.class), withId(value));
		return new SWTBotGallery((Gallery) widget(matcher, index), matcher);
	}

	/**
	 * @param inGroup the inGroup on the widget.
	 * @return a {@link SWTBotGallery} with the specified <code>inGroup</code>.
	 */
	public SWTBotGallery galleryInGroup(String inGroup) {
		return galleryInGroup(inGroup, 0);
	}

	/**
	 * @param inGroup the inGroup on the widget.
	 * @param index the index of the widget.
	 * @return a {@link SWTBotGallery} with the specified <code>inGroup</code>.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public SWTBotGallery galleryInGroup(String inGroup, int index) {
		Matcher matcher = allOf(widgetOfType(Gallery.class), inGroup(inGroup));
		return new SWTBotGallery((Gallery) widget(matcher, index), matcher);
	}

	/**
	 * @param index the index of the widget.
	 * @return a {@link SWTBotGallery} with the specified <code>none</code>.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public SWTBotGallery gallery(int index) {
		Matcher matcher = allOf(widgetOfType(Gallery.class));
		return new SWTBotGallery((Gallery) widget(matcher, index), matcher);
	}

	/**
	 * @param label the label on the widget.
	 * @param inGroup the inGroup on the widget.
	 * @return a {@link SWTBotGallery} with the specified <code>label</code> with the specified <code>inGroup</code>.
	 */
	public SWTBotGallery galleryWithLabelInGroup(String label, String inGroup) {
		return galleryWithLabelInGroup(label, inGroup, 0);
	}

	/**
	 * @param label the label on the widget.
	 * @param inGroup the inGroup on the widget.
	 * @param index the index of the widget.
	 * @return a {@link SWTBotGallery} with the specified <code>label</code> with the specified <code>inGroup</code>.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public SWTBotGallery galleryWithLabelInGroup(String label, String inGroup, int index) {
		Matcher matcher = allOf(widgetOfType(Gallery.class), withLabel(label), inGroup(inGroup));
		return new SWTBotGallery((Gallery) widget(matcher, index), matcher);
	}
	
}
