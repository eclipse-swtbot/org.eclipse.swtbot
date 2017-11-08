/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Ketan Padegaonkar - http://swtbot.org/bugzilla/show_bug.cgi?id=126
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.matchers;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

/**
 * Tells if a particular widget has value for the given key.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 * @since 2.0
 */
public class WithId<T extends Widget> extends AbstractMatcher<T> {

	/**
	 * The key to use while matching widgets.
	 */
	private final String	key;
	/**
	 * The value to use while matching widgets.
	 */
	private final Matcher<?>	valueMatcher;

	/**
	 * Matches a widget that has the specified Key/Value pair set as data into it.
	 *
	 * @see Widget#setData(String, Object)
	 * @param key the key
	 * @param value the value
	 */
	WithId(String key, Object value) {
		this(key, Matchers.equalTo(value));
	}

	WithId(String key, Matcher<?> valueMatcher) {
		this.key = key;
		this.valueMatcher = valueMatcher;
	}

	@Override
	protected boolean doMatch(final Object obj) {
		final Widget widget = (Widget) obj;
		Object data = UIThreadRunnable.syncExec(new Result<Object>() {
			@Override
			public Object run() {
				if (key == null) {
					return widget.getData();
				}
				return widget.getData(key);
			}
		});
		return this.valueMatcher.matches(data);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("with key=").appendText(key).appendText(" and value matching "); //$NON-NLS-1$ //$NON-NLS-2$
		this.valueMatcher.describeTo(description);
	}

	/**
	 * @deprecated use {@link #withId(String, Object)}
	 */
	@Deprecated
	@Factory
	public static <T extends Widget> Matcher<T> withId(String key, String value) {
		return new WithId<T>(key, value);
	}

	/**
	 * Matches a widget that has the specified Key/Value pair set as data into it.
	 *
	 * @see org.eclipse.swt.widgets.Widget#setData(String, Object)
	 * @param key the key
	 * @param value the value
	 * @return a matcher.
	 * @since 2.7
	 */
	@Factory
	public static <T extends Widget> Matcher<T> withId(String key, Object value) {
		return new WithId<T>(key, value);
	}

	/**
	 * Matches a widget that has the specified value set for the key
	 * {@link org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences#DEFAULT_KEY}.
	 *
	 * @see org.eclipse.swt.widgets.Widget#setData(String, Object)
	 * @param value the value
	 * @return a matcher.
	 * @since 2.0
	 */
	@Factory
	public static <T extends Widget> Matcher<T> withId(String value) {
		return new WithId<T>(org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences.DEFAULT_KEY, value);
	}

}
