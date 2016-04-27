/*******************************************************************************
 * Copyright (c) 2016 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Stephane Bouchet (Intel Corporation) - added tooltip test
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.matchers;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertSameWidget;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertText;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.inGroup;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withId;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withLabel;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withRegex;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withText;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withTextIgnoringCase;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withTooltip;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withTooltipIgnoringCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.test.AbstractClipboardExampleTest;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class WidgetMatcherFactoryTest extends AbstractClipboardExampleTest {

	@Test
	public void matchesControlsWithLabel() throws Exception {
		List findControls = finder.findControls(withLabel("TextTransfer:", finder));
		assertText("some\n" + "plain\n" + "text", (Widget) findControls.get(0));
		assertText("Copy", (Widget) findControls.get(1));
	}

	@Test
	public void matchesControlsWithRegex() throws Exception {
		List findControls = finder.findControls(withRegex(".*Transfer.*"));
		assertThat(findControls.size(), is(8));
	}

	@Test
	public void matchesControlsWithText() throws Exception {
		List findControls = finder.findControls(withText("some\n" + "plain\n" + "text"));
		assertThat(findControls.size(), is(1));
		assertThat(findControls.get(0), is(instanceOf(Text.class)));
	}

	@Test
	public void matchesControlsWithTextIgnoringCase() throws Exception {
		List findControls = finder.findControls(withTextIgnoringCase("SOME\n" + "plain\n" + "TeXt"));
		assertThat(findControls.size(), is(1));
		assertThat(findControls.get(0), is(instanceOf(Text.class)));
	}
	
	@Test
	public void matchesControlsWithTooltip() throws Exception {
		List findControls = finder.findControls(withTooltip("TextTransfer Tooltip"));
		assertThat(findControls.size(), is(1));
		assertThat(findControls.get(0), is(instanceOf(Label.class)));
	}

	@Test
	public void matchesControlsWithTooltipIgnoringCase() throws Exception {
		List findControls = finder.findControls(withTooltipIgnoringCase("texttransfer tooltip"));
		assertThat(findControls.size(), is(1));
		assertThat(findControls.get(0), is(instanceOf(Label.class)));
	}

	@Test
	public void matchesControlsInGroup() throws Exception {
		List findControls = finder.findControls(inGroup("Paste To:"));
		assertThat(findControls.size(), is(12));
		assertThat(findControls.get(0), is(instanceOf(Label.class)));
		assertThat(findControls.get(1), is(instanceOf(Text.class)));
		assertThat(findControls.get(2), is(instanceOf(Button.class)));
		assertThat(findControls.get(3), is(instanceOf(Label.class)));
	}

	@Test
	public void findsControlsById() throws Exception {
		final Text text = (Text) bot.widget(allOf(withLabel("TextTransfer:", finder), inGroup("Copy From:")));
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				text.setData("foo-text", "bar");
			}
		});
		assertSameWidget(text, bot.widget(withId("foo-text", "bar")));
	}

}
