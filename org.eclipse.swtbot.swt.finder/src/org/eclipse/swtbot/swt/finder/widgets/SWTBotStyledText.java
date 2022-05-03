/*******************************************************************************
 * Copyright (c) 2008, 2020 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Aparna Argade - API to consider Tab width for column (Bug 536131)
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.bindings.keys.KeyLookupFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.swt.finder.ReferenceBy;
import org.eclipse.swtbot.swt.finder.SWTBotWidget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.ArrayResult;
import org.eclipse.swtbot.swt.finder.results.IntResult;
import org.eclipse.swtbot.swt.finder.results.ListResult;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.StringResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.Position;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.hamcrest.SelfDescribing;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
@SWTBotWidget(clasz = StyledText.class, preferredName = "styledText", referenceBy = { ReferenceBy.LABEL, ReferenceBy.TEXT })
public class SWTBotStyledText extends AbstractSWTBotControl<StyledText> {

	/**
	 * Constructs a new instance of this object.
	 *
	 * @param styledText the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 * @since 2.0
	 */
	public SWTBotStyledText(StyledText styledText) throws WidgetNotFoundException {
		this(styledText, null);
	}

	/**
	 * Constructs a new instance of this object.
	 *
	 * @param styledText the widget.
	 * @param description the description of the widget, this will be reported by {@link #toString()}
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 * @since 2.0
	 */
	public SWTBotStyledText(StyledText styledText, SelfDescribing description) throws WidgetNotFoundException {
		super(styledText, description);
	}

	/**
	 * Sets the text into the styled text.
	 *
	 * @param text the text to set.
	 */
	public void setText(final String text) {
		waitForEnabled();
		asyncExec(new VoidResult() {
			@Override
			public void run() {
				widget.setText(text);
			}
		});
	}

	/**
	 * Sets the caret at the specified location.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number, 0 based. Here Tab needs to be counted as 1.
	 * @see SWTBotStyledText#navigateTo(int, int, boolean)
	 */
	public void navigateTo(final int line, final int column) {
		navigateTo(line, column, false);
	}

	/**
	 * Sets the caret at the specified location.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number, 0 based.
	 * @param withTabWidth <code>true</code> if column is specified considering tab width preference;
	 *                     <code>false</code> if column is specified counting tab as a 1.
	 * @since 2.8
	 */
	public void navigateTo(final int line, final int column, final boolean withTabWidth) {
		log.info("test {}", "Test");
		log.debug("Enquing navigation to location {}, {} in {}", line, column, this); //$NON-NLS-1$
		waitForEnabled();
		setFocus();
		syncExec(new VoidResult() {
			@Override
			public void run() {
				log.debug("Navigating to location {}, {} in {}", line, column, widget); //$NON-NLS-1$
				widget.setSelection(offset(line, column, withTabWidth));
			}
		});
	}

	/**
	 * Sets the caret at the specified location.
	 *
	 * @param position the position of the caret.
	 */
	public void navigateTo(Position position) {
		navigateTo(position.line, position.column);
	}

	/**
	 * Sets the caret at the specified location.
	 *
	 * @param position the position of the caret.
	 * @param withTabWidth <code>true</code> if column of the position is specified considering tab width preference;
	 *                     <code>false</code> if column of the position is specified counting tab as 1.
	 * @since 2.8
	 */
	public void navigateTo(Position position, final boolean withTabWidth) {
		navigateTo(position.line, position.column, withTabWidth);
	}

	/**
	 * Gets the current position of the cursor. The returned position will contain a
	 * 0-based line and column, with tabs counting as 1 column.
	 *
	 * @return the position of the cursor in the styled text.
	 * @see SWTBotStyledText#cursorPosition(boolean)
	 */
	public Position cursorPosition() {
		return cursorPosition(false);
	}

	/**
	 * Gets the current position of the cursor. The returned position will contain a
	 * 0-based line and column.
	 *
	 * @param withTabWidth <code>true</code> if column in the returned position
	 *                     should consider tab width preference; <code>false</code>
	 *                     if column in the returned position should count tab as 1.
	 * @return the position of the cursor in the styled text.
	 * @since 2.8
	 */
	public Position cursorPosition(final boolean withTabWidth) {
		return syncExec(new Result<Position>() {
			@Override
			public Position run() {
				widget.setFocus();
				int offset = widget.getCaretOffset();
				int line = widget.getContent().getLineAtOffset(offset);
				int offsetAtLine = widget.getContent().getOffsetAtLine(line);
				int column = offset - offsetAtLine;
				if (!withTabWidth) {
					return new Position(line, column);
				} else {
					int tabwidth = widget.getTabs();
					int displayColumn = 0;
					// Get display column corresponding to character index
					for (int i = 0; i < column; i++) {
						displayColumn = getDisplayColumnForNextChar(offsetAtLine, i, displayColumn, tabwidth);
					}
					return new Position(line, displayColumn);
				}
			}
		});
	}

	/**
	 * Returns display column index for next character.
	 *
	 * If current character is tab, it calculates the spaces consumed by tab by
	 * considering the column index at which tab occurs. If current character is not
	 * tab, it increments display column by 1.
	 *
	 * @param offset offset of start of the line
	 * @param column column of current character, 0 based
	 * @param displayColumn display column for the current character, 0 based
	 * @param tabwidth tab width measured in characters
	 * @return display column for the next character, 0 based
	 */
	private int getDisplayColumnForNextChar(int offset, int column, int displayColumn, int tabwidth) {
		char ch = widget.getContent().getTextRange(offset + column, 1).charAt(0);
		if (ch == SWT.TAB) {
			// Increment displayColumn by spaces required to align to tab-width
			int mod = displayColumn % tabwidth;
			displayColumn += (tabwidth - mod);
		} else {
			displayColumn++;
		}
		return displayColumn;
	}

	/**
	 * Types the text at the given location.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number, 0 based. Here Tab needs to be counted as 1.
	 * @param text the text to be typed at the specified location
	 * @see SWTBotStyledText#typeText(int, int, String, boolean)
	 * @since 1.0
	 */
	public void typeText(int line, int column, String text) {
		typeText(line, column, text, false);
	}

	/**
	 * Types text at the given location.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number, 0 based.
	 * @param text the text to be typed at the specified location
	 * @param withTabWidth <code>true</code> if column is specified considering tab width preference;
	 *                     <code>false</code> if column is specified counting tab as 1.
	 * @since 2.8
	 */
	public void typeText(int line, int column, String text, boolean withTabWidth) {
		navigateTo(line, column, withTabWidth);
		typeText(text);
	}

	/**
	 * Inserts text at the given location.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number, 0 based. Here Tab needs to be counted as 1.
	 * @param text the text to be inserted at the specified location
	 * @see SWTBotStyledText#insertText(int, int, String, boolean)
	 */
	public void insertText(int line, int column, String text) {
		insertText(line, column, text, false);
	}

	/**
	 * Inserts text at the given location.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number, 0 based.
	 * @param text the text to be inserted at the specified location.
	 * @param withTabWidth <code>true</code> if column is specified considering tab width preference;
	 *                     <code>false</code> if column is specified counting tab as 1.
	 * @since 2.8
	 */
	public void insertText(int line, int column, String text, boolean withTabWidth) {
		navigateTo(line, column, withTabWidth);
		insertText(text);
	}

	/**
	 * Inserts text at the end.
	 * <p>
	 * FIXME handle line endings
	 * </p>
	 *
	 * @param text the text to be inserted at the location of the caret.
	 */
	public void insertText(final String text) {
		waitForEnabled();
		syncExec(new VoidResult() {
			@Override
			public void run() {
				widget.insert(text);
			}
		});
	}

	/**
	 * Types the text.
	 * <p>
	 * FIXME handle line endings
	 * </p>
	 *
	 * @param text the text to be typed at the location of the caret.
	 * @since 1.0
	 */
	public void typeText(final String text) {
		typeText(text, SWTBotPreferences.TYPE_INTERVAL);
	}

	/**
	 * Types the text.
	 * <p>
	 * FIXME handle line endings
	 * </p>
	 *
	 * @param text the text to be typed at the location of the caret.
	 * @param interval the interval between consecutive key strokes.
	 * @since 1.0
	 */
	public void typeText(final String text, int interval) {
		log.debug("Inserting text:{} into styledtext{}", text, this); //$NON-NLS-1$
		setFocus();
		keyboard().typeText(text);
	}

	/**
	 * Gets the style for the given line.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number, 0 based. Here Tab needs to be counted as 1.
	 * @return the {@link StyleRange} at the specified location
	 * @see SWTBotStyledText#getStyle(int, int, boolean)
	 */
	public StyleRange getStyle(final int line, final int column) {
		return getStyle(line, column, false);
	}

	/**
	 * Gets the style for the given line.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number, 0 based.
	 * @param withTabWidth <code>true</code> if column is specified considering tab width preference;
	 *                     <code>false</code> if column is specified counting tab as 1.
	 * @return the {@link StyleRange} at the specified location
	 * @since 2.8
	 */
	public StyleRange getStyle(final int line, final int column, final boolean withTabWidth) {
		return syncExec(new Result<StyleRange>() {
			@Override
			public StyleRange run() {
				return widget.getStyleRangeAtOffset(offset(line, column, withTabWidth));
			}
		});
	}

	/**
	 * Gets the offset.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number, 0 based. Here Tab needs to be counted as 1.
	 * @return the character offset at the specified location in the styledtext.
	 * @see StyledTextContent#getOffsetAtLine(int)
	 * @see SWTBotStyledText#offset(int, int, boolean)
	 */
	protected int offset(final int line, final int column) {
		return offset(line, column, false);
	}

	/**
	 * Gets the offset.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number, 0 based.
	 * @param withTabWidth <code>true</code> if column is specified considering tab width preference;
	 *                     <code>false</code> if column is specified counting tab as 1.
	 * @return the character offset at the specified location in the styledtext.
	 * @see StyledTextContent#getOffsetAtLine(int)
	 * @since 2.8
	 */
	protected int offset(final int line, final int column, final boolean withTabWidth) {
		int offset = widget.getContent().getOffsetAtLine(line);
		if (!withTabWidth) {
			return offset + column;
		} else {
			int tabwidth = widget.getTabs();
			int charIndex = 0;
			// Get character index considering display column that may change due to tabs.
			for (int displayColumn = 0; column > charIndex && column > displayColumn; charIndex++) {
				displayColumn = getDisplayColumnForNextChar(offset, charIndex, displayColumn, tabwidth);
			}
			return offset + charIndex;
		}
	}

	/**
	 * Selects the range.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number, 0 based. Here Tab needs to be counted as 1.
	 * @param length the length of the selection.
	 * @see SWTBotStyledText#selectRange(int, int, int, boolean)
	 */
	public void selectRange(final int line, final int column, final int length) {
		selectRange(line, column, length, false);
	}

	/**
	 * Selects the range.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number, 0 based.
	 * @param length the length of the selection.
	 * @param withTabWidth <code>true</code> if column and length are specified considering tab width preference;
	 *                     <code>false</code> if column and length are specified considering tab as 1.
	 * @since 2.8
	 */
	public void selectRange(final int line, final int column, final int length, final boolean withTabWidth) {
		waitForEnabled();
		asyncExec(new VoidResult() {
			@Override
			public void run() {
				int offset = offset(line, column, withTabWidth);
				int endOffset = offset(line, column + length, withTabWidth);
				widget.setSelection(offset, endOffset);
			}
		});
		notify(SWT.Selection);
	}

	/**
	 * Gets the current selection text.
	 *
	 * @return the selection in the styled text
	 */
	public String getSelection() {
		return syncExec(new StringResult() {
			@Override
			public String run() {
				return widget.getSelectionText();
			}
		});
	}

	/**
	 * Gets the style information.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number, 0 based. Here Tab needs to be counted as 1.
	 * @param length the length.
	 * @return the styles in the specified range.
	 * @see SWTBotStyledText#getStyles(int, int, int, boolean)
	 * @see StyledText#getStyleRanges(int, int)
	 */
	public StyleRange[] getStyles(final int line, final int column, final int length) {
		return getStyles(line, column, length, false);
	}

	/**
	 * Gets the style information.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number, 0 based.
	 * @param length the length.
	 * @param withTabWidth <code>true</code> if column and length are specified considering tab width preference;
	 *                     <code>false</code> if column and length are specified counting tab as 1.
	 * @return the styles in the specified range.
	 * @see StyledText#getStyleRanges(int, int)
	 * @since 2.8
	 */
	public StyleRange[] getStyles(final int line, final int column, final int length, final boolean withTabWidth) {
		return syncExec(new ArrayResult<StyleRange>() {
			@Override
			public StyleRange[] run() {
				int offset = offset(line, column, withTabWidth);
				int endOffset = offset(line, column + length, withTabWidth);
				return widget.getStyleRanges(offset, endOffset - offset);
			}
		});
	}

	/**
	 * Gets the text on the current line.
	 *
	 * @return the text on the current line, without the line delimiters.
	 * @see SWTBotStyledText#getTextOnLine(int)
	 */
	public String getTextOnCurrentLine() {
		final Position currentPosition = cursorPosition();
		final int line = currentPosition.line;
		return getTextOnLine(line);
	}

	/**
	 * Gets the text on the line.
	 * <p>
	 * TODO: throw exception if the line is out of range.
	 * </p>
	 *
	 * @param line the line number, 0 based.
	 * @return the text on the given line number, without the line delimiters.
	 */
	public String getTextOnLine(final int line) {
		return syncExec(new StringResult() {
			@Override
			public String run() {
				return widget.getContent().getLine(line);
			}
		});
	}

	/**
	 * Checks if this has a bullet on the current line.
	 *
	 * @return <code>true</code> if the styledText has a bullet on the given line, <code>false</code> otherwise.
	 * @see StyledText#getLineBullet(int)
	 */
	public boolean hasBulletOnCurrentLine() {
		return hasBulletOnLine(cursorPosition().line);
	}

	/**
	 * Gets if this has a bullet on the specific line.
	 *
	 * @param line the line number, 0 based.
	 * @return <code>true</code> if the styledText has a bullet on the given line, <code>false</code> otherwise.
	 * @see StyledText#getLineBullet(int)
	 */
	public boolean hasBulletOnLine(final int line) {
		return getBulletOnLine(line) != null;
	}

	/**
	 * Gets the bullet on the current line.
	 *
	 * @return the bullet on the current line.
	 * @see StyledText#getLineBullet(int)
	 */
	public Bullet getBulletOnCurrentLine() {
		return getBulletOnLine(cursorPosition().line);
	}

	/**
	 * Gets the bullet on the given line.
	 *
	 * @param line the line number, 0 based.
	 * @return the bullet on the given line.
	 * @see StyledText#getLineBullet(int)
	 */
	public Bullet getBulletOnLine(final int line) {
		return syncExec(new Result<Bullet>() {
			@Override
			public Bullet run() {
				return widget.getLineBullet(line);
			}
		});
	}

	/**
	 * Selects the text on the specified line.
	 *
	 * @param line the line number, 0 based.
	 * @since 1.1
	 */
	public void selectLine(int line) {
		selectRange(line, 0, getTextOnLine(line).length());
	}

	/**
	 * Selects the text on the current line.
	 *
	 * @since 1.1
	 */
	public void selectCurrentLine() {
		selectLine(cursorPosition().line);
	}

	/**
	 * Gets the color of the background on the specified line.
	 *
	 * @param line the line number, 0 based.
	 * @return the RGB of the line background color of the specified line.
	 * @since 1.3
	 */
	public RGB getLineBackground(final int line) {
		return syncExec(new Result<RGB>() {
			@Override
			public RGB run() {
				return widget.getLineBackground(line).getRGB();
			}
		});
	}

	/**
	 * Gets the number of lines in the {@link StyledText}.
	 *
	 * @return the number of lines in the {@link StyledText}.
	 */
	public int getLineCount(){
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getLineCount();
			}
		});
	}

	/**
	 * Gets all the lines in the editor.
	 *
	 * @return the lines in the editor.
	 */
	public List<String> getLines() {
		return syncExec(new ListResult<String>() {
			@Override
			public List<String> run() {
				int lineCount = widget.getLineCount();
				ArrayList<String> lines = new ArrayList<String>(lineCount);
				for (int i = 0; i < lineCount; i++) {
					lines.add(widget.getLine(i));
				}
				return lines;
			}
		});
	}

	/**
	 * Gets the tab width of the {@link StyledText} measured in characters.
	 *
	 * @return the tab width of the {@link StyledText} measured in characters.
	 */
	public int getTabs() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getTabs();
			}
		});
	}

	/**
	 * Clicks on the widget at the given line and column.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number considering tab spaces, 0 based.
	 * @since 2.8
	 */
	public void click(final int line, final int column) {
		navigateTo(line, column, true);
		notifyClick(getXY(line, column));
		log.debug("Clicked on {}", this); //$NON-NLS-1$
	}

	private void notifyClick(final Point p) {
		notify(SWT.MouseEnter);
		notify(SWT.Activate);
		notify(SWT.FocusIn);
		notify(SWT.MouseDown, createMouseEvent(p.x, p.y, 1, SWT.NONE, 1));
		notify(SWT.MouseUp, createMouseEvent(p.x, p.y, 1, SWT.BUTTON1, 1));
	}

	/**
	 * Double-clicks on the widget at the given line and column.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number considering tab spaces, 0 based.
	 * @since 2.8
	 */
	public void doubleClick(final int line, final int column) {
		navigateTo(line, column, true);
		Point p = getXY(line, column);
		notifyClick(p);
		notify(SWT.MouseDown, createMouseEvent(p.x, p.y, 1, SWT.NONE, 2));
		notify(SWT.MouseDoubleClick, createMouseEvent(p.x, p.y, 1, SWT.NONE, 2));
		notify(SWT.MouseUp, createMouseEvent(p.x, p.y, 1, SWT.BUTTON1, 2));
		log.debug("Double-clicked on {}", this); //$NON-NLS-1$
	}

	/**
	 * Clicks on the widget at the given line and column with modifier key.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number considering tab spaces, 0 based.
	 * @param modifierKey modifier key or zero.
	 * @since 2.8
	 */
	public void click(final int line, final int column, final int modifierKey) {
		if (!KeyLookupFactory.getDefault().isModifierKey(modifierKey) && modifierKey != 0) {
			log.error("{} is not a modifier key.", modifierKey); //$NON-NLS-1$
			return;
		}
		log.debug("Clicking on {} with modifier key {}", this, //$NON-NLS-1$
				modifierKey);
		notify(SWT.MouseEnter);
		notify(SWT.FocusIn);
		notify(SWT.Activate);
		syncExec(new VoidResult() {
			@Override
			public void run() {
				Point originalCursorLocation = display.getCursorLocation();
				/*
				 * Scroll vertically and then horizontally so that the given point becomes
				 * visible. This is done to avoid auto-scrolling that can happen after sending
				 * MouseDown event. The calculated absolute position should point to the given
				 * line and column.
				 */
				widget.setTopIndex(line);
				widget.setHorizontalIndex(column);
				Point p = getXY(line, column);
				Point absolutePoint = widget.toDisplay(p);
				moveMouse(absolutePoint.x, absolutePoint.y);
				notifyStyledText(SWT.MouseDown, createMouseEvent(p.x, p.y, 1, SWT.NONE | modifierKey, 1));
				notifyStyledText(SWT.MouseUp, createMouseEvent(p.x, p.y, 1, SWT.BUTTON1 | modifierKey, 1));
				moveMouse(originalCursorLocation.x, originalCursorLocation.y);
			}
		});
		log.debug("Clicked on {} with modifier key {}", this, //$NON-NLS-1$
				modifierKey);
	}

	private void notifyStyledText(int eventType, Event event) {
		notify(eventType, event, widget);
	}

	/**
	 * Returns x, y coordinates for the given line and column.
	 *
	 * @param line the line number, 0 based.
	 * @param column the column number considering tab spaces, 0 based.
	 * @return Point which represents x, y for the given line and column.
	 */
	private Point getXY(final int line, final int column) {
		return syncExec(new Result<Point>() {
			@Override
			public Point run() {
				int offset = offset(line, column, true);
				Point p = widget.getLocationAtOffset(offset);
				return new Point(p.x, p.y + widget.getLineHeight() / 2);
			}
		});
	}

}
