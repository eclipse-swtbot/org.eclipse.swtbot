/*******************************************************************************
 * Copyright (c) 2008, 2018 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Aparna Argade - API to consider Tab width for column (Bug 536131)
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertTextContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.test.AbstractCustomControlExampleTest;
import org.eclipse.swtbot.swt.finder.utils.Position;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotStyledTextTest extends AbstractCustomControlExampleTest {

	private SWTBotStyledText	styledText;

	@Test
	public void selectsRange() throws Exception {
		styledText.setText("hello world\n" + "it is a very good day today\n" + "good bye world\n" + "it was nice to meet you");
		styledText.selectRange(1, 0, 27);
		assertEquals("it is a very good day today", styledText.getSelection());
	}

	@Test
	public void selectsRangeWithTabWidth() throws Exception {
		styledText.setText("hello world\n" + "it is a \tvery good day today\n" + "good bye world\n" + "it was nice to meet you");
		styledText.selectRange(1, 0, 31, true); //display column considering tab width
		assertEquals("it is a \tvery good day today", styledText.getSelection());
		styledText.selectRange(1, 0, 28, false); //character count
		assertEquals("it is a \tvery good day today", styledText.getSelection());
	}

	@Test
	public void findsTextStyle() throws Exception {
		setStyles();
		StyleRange range = styledText.getStyle(1, 2);
		assertEquals(SWT.BOLD, range.fontStyle);
		assertTrue(range.underline);
	}

	@Test
	public void findsStylesRangeInARange() throws Exception {
		setStyles();
		StyleRange[] styles = styledText.getStyles(1, 0, 30);
		assertEquals(2, styles.length);
		assertTrue(styles[0].underline);
		assertEquals(SWT.BOLD, styles[0].fontStyle);
		assertEquals(SWT.ITALIC, styles[1].fontStyle);
	}

	/**
	 * @throws WidgetNotFoundException
	 */
	private void setStyles() throws WidgetNotFoundException {
		styledText.setText("hello world\n" + "it is a very good day today\n" + "good bye world\n" + "it was nice to meet you");
		styledText.selectRange(1, 0, 27);
		bot.buttonWithLabel("Bold").click();
		styledText.selectRange(1, 0, 27);
		bot.buttonWithLabel("Underline").click();
		styledText.selectRange(2, 1, 10);
		bot.buttonWithLabel("Italic").click();
	}

	@Test
	public void findsTextStyleWithTabWidth() throws Exception {
		setStylesWithTabWidth();
		StyleRange range = styledText.getStyle(1, 12, true);
		assertEquals(SWT.BOLD, range.fontStyle);
		assertTrue(range.underline);
		range = styledText.getStyle(1, 9, false);
		assertEquals(SWT.BOLD, range.fontStyle);
		assertTrue(range.underline);
	}

	@Test
	public void findsStylesRangeInARangeWithTabWidth() throws Exception {
		setStylesWithTabWidth();
		StyleRange[] styles = styledText.getStyles(1, 0, 50, true);
		assertEquals(3, styles.length);
		assertTrue(styles[0].underline);
		assertEquals(SWT.BOLD, styles[0].fontStyle);
		assertTrue(styles[0].underline);
		assertEquals(SWT.ITALIC, styles[1].fontStyle);
		assertTrue(styles[2].strikeout);
		styles = styledText.getStyles(1, 0, 44, false);
		assertEquals(3, styles.length);
		assertTrue(styles[0].underline);
		assertEquals(SWT.BOLD, styles[0].fontStyle);
		assertTrue(styles[0].underline);
		assertEquals(SWT.ITALIC, styles[1].fontStyle);
		assertTrue(styles[2].strikeout);
	}

	private void setStylesWithTabWidth() throws WidgetNotFoundException {
		styledText.setText("hello world\n" + "it is a \tvery good \tday today\n" + "good\tbye\tworld\n" + "it was nice to meet you");
		styledText.selectRange(1, 12, 9, true); //selects "very good" and make it bold, underline
		bot.buttonWithLabel("Bold").click();
		styledText.selectRange(1, 12, 9, true);
		bot.buttonWithLabel("Underline").click();
		styledText.selectRange(1, 24, 9, true); //selects "day today" and make it Italic
		bot.buttonWithLabel("Italic").click();
		styledText.selectRange(2, 12, 5, true); //selects "world" and strike it out
		bot.buttonWithLabel("Strikeout").click();
	}

	@Test
	public void setsText() throws Exception {
		styledText.setText("hello world");
		assertTextContains("hello world", styledText.widget);
	}

	@Test
	public void navigatesToAParticularLocation() throws Exception {
		styledText.setText("hello world\n" + "it is a very good day today\n" + "good bye world\n" + "it was nice to meet you");
		styledText.navigateTo(1, 17);
		assertEquals(new Position(1, 17), styledText.cursorPosition());
	}

	@Test
	public void navigatesToAParticularLocationWithTAB() throws Exception {
		styledText.setText("\thello\tworld\n");
		styledText.navigateTo(0, 13, true);
		assertEquals(new Position(0, 13), styledText.cursorPosition(true));
		styledText.selectRange(0, 13, 1, true);
		assertEquals("o", styledText.getSelection());
	}

	@Test
	public void typesTextAtALocation() throws Exception {
		styledText.typeText(1, 0, "---typed Text---\n");
		assertTextContains("---typed Text---", styledText.widget);
	}

	@Test
	public void typesTextAtALocationWithTabWidth() throws Exception {
		styledText.setText("hello world\n" + "it is a \tvery good day today\n");
		styledText.typeText(1, 26, "---typed Text---", true);
		assertTextContains("it is a \tvery good day ---typed Text---today\n", styledText.widget);
		styledText.setText("hello world\n" + "it is a \tvery good day today\n");
		styledText.typeText(1, 23, "---typed Text---", false);
		assertTextContains("it is a \tvery good day ---typed Text---today\n", styledText.widget);
	}

	@Test
	public void typesSpecialCharactersAtALocation() throws Exception {
		styledText.typeText(1, 0, "---123 #@! :; {} [] ---\n");
		assertTextContains("---123 #@! :; {} [] ---", styledText.widget);
	}

	@Test
	public void insertsTextAtALocation() throws Exception {
		styledText.insertText(1, 0, "---inserted text---\n");
		assertTextContains("---inserted text---", styledText.widget);
	}

	@Test
	public void insertsTextAtALocationWithTabWidth() throws Exception {
		styledText.setText("hello world\n" + "it is a \tvery good day today\n");
		styledText.insertText(1, 26, "---inserted text---\n", true);
		assertTextContains("it is a \tvery good day ---inserted text---\ntoday\n", styledText.widget);
		styledText.setText("hello world\n" + "it is a \tvery good day today\n");
		styledText.insertText(1, 23, "---inserted text---\n", false);
		assertTextContains("it is a \tvery good day ---inserted text---\ntoday\n", styledText.widget);
	}

	@Test
	public void testTypesTextAtALocation() throws Exception {
		styledText.typeText(1, 0, "---\n\ttyped text\n---\n");
		assertTextContains("---\n\ttyped text\n---\n", styledText.widget);
	}

	@Test
	public void getsTextOnCurrentLine() throws Exception {
		styledText.setText("hello world\n" + "it is a very good day today\n" + "good bye world\n" + "it was nice to meet you");
		styledText.navigateTo(1, 0);
		assertEquals("it is a very good day today", styledText.getTextOnCurrentLine());
	}

	@Test
	public void getsLineCountWithEOLAtEndOfText() throws Exception {
		styledText.setText("1\n2\n3\n");
		assertEquals(4, styledText.getLineCount());
	}

	@Test
	public void getsLineCountWithNoEOLEndOfText() throws Exception {
		styledText.setText("1\n2\n3");
		assertEquals(3, styledText.getLineCount());
	}

	@Test
	public void getsLinesWithEOLAtEndOfText() throws Exception {
		styledText.setText("1\n2\n3\n");
		assertEquals(Arrays.asList("1", "2", "3", ""), styledText.getLines());
	}

	@Test
	public void getsLinesWithNoEOLEndOfText() throws Exception {
		styledText.setText("1\n2\n3");
		assertEquals(Arrays.asList("1", "2", "3"), styledText.getLines());
	}

	@Test
	public void getsTabs() {
		assertEquals(4, styledText.getTabs());
		try {
			setTabs(15);
			assertEquals(15, styledText.getTabs());
		} finally {
			setTabs(4);
		}
	}

	@Test
	public void getsBullet() throws Exception {

		styledText.setText("hello world\n" + "it is a very good day today\n" + "good bye world\n" + "it was nice to meet you");
		styledText.navigateTo(1, 0);
		assertFalse(styledText.hasBulletOnCurrentLine());

		display.syncExec(new Runnable() {
			@Override
			public void run() {
				StyledText t = styledText.widget;
				StyleRange style = new StyleRange();
				style.metrics = new GlyphMetrics(0, 0, 1);
				t.setLineBullet(1, 1, new Bullet(style));
			}
		});

		assertTrue(styledText.hasBulletOnCurrentLine());
	}

	@Before
	public void prepareExample() throws Exception {
		bot.shell("SWT Custom Controls").activate();
		bot.tabItem("StyledText").activate();
		styledText = bot.styledTextInGroup("StyledText");
		bot.checkBox("Horizontal Fill").select();
		bot.checkBox("Vertical Fill").select();
	}

	private void setTabs(final int tabSize) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				StyledText t = styledText.widget;
				t.setTabs(tabSize);
			}
		});
	}

}
