/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTUtilsTest extends AbstractControlExampleTest {

	@Test
	public void findsRightIndexOfControlInParentWithNoParent() throws Exception {
		assertEquals(-1, SWTUtils.widgetIndex(shell));
	}

	@Test
	public void findsIndexOfArbritryControl() throws Exception {
		assertEquals(5, SWTUtils.widgetIndex(getChildren()[5]));
		assertEquals(0, SWTUtils.widgetIndex(getChildren()[0]));
		assertEquals(-1, SWTUtils.widgetIndex(null));
	}

	@Test
	public void findsNextWidget() throws Exception {
		assertSame(getChildren()[1], SWTUtils.nextWidget(getChildren()[0]));
	}

	@Test
	public void nextWidgetOnLastWidgetIsNull() throws Exception {
		final Control[] children = getChildren();
		assertSame(null, SWTUtils.nextWidget(children[children.length - 1]));
	}

	@Test
	public void findsPreviousWidget() throws Exception {
		assertSame(getChildren()[2], SWTUtils.previousWidget(getChildren()[3]));
	}

	@Test
	public void getsCorrectStyle() throws Exception {
		assertTrue(SWTUtils.hasStyle(bot.button("One").widget, SWT.PUSH));
		assertTrue(SWTUtils.hasStyle(bot.radio("SWT.RADIO").widget, SWT.RADIO));
		assertTrue(SWTUtils.hasStyle(bot.checkBox("SWT.FLAT").widget, SWT.CHECK));

		assertTrue(SWTUtils.hasStyle(bot.checkBox("SWT.FLAT").widget, SWT.NONE));
		assertFalse(SWTUtils.hasStyle(null, SWT.CHECK));
	}

	@Test
	public void previousWidgetOnFirstWidget() throws Exception {
		assertSame(null, SWTUtils.previousWidget(getChildren()[0]));
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("Button").activate();
	}

	@Test
	public void getsToString() throws Exception {
		assertEquals("TabFolder {}", SWTUtils.toString(controlExample.getTabFolder()));
	}

	@Test
	public void capturesImageOfAControl() throws Exception {
		Control control = controlExample.getTabFolder();
		Rectangle bounds = boundsOf(control);

		ImageData image = SWTUtils.captureImage(control);

		assertEquals(bounds.width, image.width);
		assertEquals(bounds.height, image.height);
	}

	@Test
	public void capturesImageOfAShell() throws Exception {
		Rectangle bounds = boundsOf(shell);

		ImageData image = SWTUtils.captureImage(shell);

		// A shell already reports display relative bounds, so they must be used as they are.
		assertEquals(bounds.width, image.width);
		assertEquals(bounds.height, image.height);
	}

	@Test
	public void capturesImageOfAnArea() throws Exception {
		ImageData image = SWTUtils.captureImage(new Rectangle(0, 0, 40, 20));

		assertEquals(40, image.width);
		assertEquals(20, image.height);
	}

	@Test
	public void capturingAnEmptyAreaFails() throws Exception {
		try {
			SWTUtils.captureImage(new Rectangle(0, 0, 0, 0));
			fail("Expecting an IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
			assertTrue(expected.getMessage().contains("empty area"));
		}
	}

	private Rectangle boundsOf(final Control control) {
		return UIThreadRunnable.syncExec(new Result<Rectangle>() {
			@Override
			public Rectangle run() {
				return control.getBounds();
			}
		});
	}

	private Control[] getChildren() {
		final Control[][] children = new Control[][] { null };
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				children[0] = controlExample.getTabFolder().getChildren();
			}
		});
		return children[0];
	}

}
