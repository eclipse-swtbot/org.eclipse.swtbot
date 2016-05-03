/*******************************************************************************
 * Copyright (c) 2016 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Aparna Argade(Cadence Design Systems, Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.nebula.nattable.finder.widgets;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.edit.editor.ICellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swtbot.nebula.nattable.finder.finders.NatTableContextMenuFinder;
import org.eclipse.swtbot.swt.finder.ReferenceBy;
import org.eclipse.swtbot.swt.finder.SWTBotWidget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.IntResult;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.internal.Assert;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotRootMenu;
import org.hamcrest.SelfDescribing;

/**
 * A class which allows an SWTBot to detect and interact with a NatTable.
 * <p>
 * All row and column numbers are relative to the visible grid layer. The row
 * and column numbers include headers also and are affected by filtering,
 * sorting and scrolling, etc.
 */
@SWTBotWidget(clasz = NatTable.class, preferredName = "NatTable", referenceBy = { ReferenceBy.LABEL })
public class SWTBotNatTable extends AbstractSWTBot<NatTable> {
	/**
	 * The default constructor
	 *
	 * @param natTable
	 *            The NatTable to be wrapped.
	 * @throws WidgetNotFoundException
	 *             An exception to be thrown when the SWTBot is unable to find a
	 *             valid NatTable.
	 */
	public SWTBotNatTable(NatTable natTable) throws WidgetNotFoundException {
		this(natTable, null);
	}

	/**
	 * The default constructor
	 *
	 * @param natTable
	 *            The NatTable to be wrapped.
	 * @param description
	 *            Self description
	 * @throws WidgetNotFoundException
	 *             An exception to be thrown when the SWTBot is unable to find a
	 *             valid NatTable.
	 */
	public SWTBotNatTable(NatTable natTable, SelfDescribing description) throws WidgetNotFoundException {
		super(natTable, description);
	}

	/**
	 * Gets visible row count for the NatTable
	 *
	 * @return The number of rows in the NatTable that are visible
	 */
	public int rowCount() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getRowCount();
			}
		});
	}

	/**
	 * Gets total row count for the NatTable
	 *
	 * @return Total number of rows in the NatTable
	 */
	public int preferredRowCount() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getPreferredRowCount();
			}
		});
	}

	/**
	 * Gets the visible column count.
	 *
	 * @return the number of columns in the NatTable that are visible
	 */
	public int columnCount() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getColumnCount();
			}
		});
	}

	/**
	 * Gets the total column count.
	 *
	 * @return total number of columns in the NatTable
	 */
	public int preferredColumnCount() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getPreferredColumnCount();
			}
		});
	}

	/**
	 * Click on the NatTable on given cell.
	 *
	 * @param row
	 *            the visible row number in the NatTable
	 * @param column
	 *            the visible column number in the NatTable
	 * @return itself
	 */
	public SWTBotNatTable click(final int row, final int column) {
		assertIsLegalCell(row, column);
		waitForEnabled();
		setFocus();
		syncExec(new VoidResult() {
			@Override
			public void run() {
				Rectangle cellBounds = widget.getBoundsByPosition(column, row);
				clickXY(cellBounds.x + (cellBounds.width / 2), cellBounds.y + (cellBounds.height / 2));
			}
		});
		return this;
	}

	/**
	 * DoubleClick on the NatTable on given cell.
	 *
	 * @param row
	 *            the visible row number in the NatTable
	 * @param column
	 *            the visible column number in the NatTable
	 * @return itself
	 */
	public SWTBotNatTable doubleclick(final int row, final int column) {
		assertIsLegalCell(row, column);
		setFocus();
		syncExec(new VoidResult() {
			@Override
			public void run() {
				Rectangle cellBounds = widget.getBoundsByPosition(column, row);
				doubleClickXY(cellBounds.x + (cellBounds.width / 2), cellBounds.y + (cellBounds.height / 2));
			}
		});
		return this;
	}

	/**
	 * RightClick on the NatTable on given cell.
	 *
	 * @param row
	 * 	the visible row number in the NatTable
	 * @param column
	 * 	the visible column number in the NatTable
	 * @return itself
	 */
	public SWTBotNatTable rightClick(final int row, final int column) {
		assertIsLegalCell(row, column);
		setFocus();
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
				Rectangle cellBounds = widget.getBoundsByPosition(column, row);
				rightClick(cellBounds.x + (cellBounds.width / 2), cellBounds.y + (cellBounds.height / 2), false);
			}
		});
		return this;
	}

	/**
	 * RightClick on the NatTable at the center of widget.
	 *
	 * @return itself
	 */
	@Override
	public SWTBotNatTable rightClick() {
		setFocus();
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
				Rectangle widgetBounds = widget.getBounds();
				rightClick(widgetBounds.width / 2, widgetBounds.height / 2, false);
			}
		});
		return this;
	}

	/**
	 * Get the bounds of the Widget
	 *
	 * @return the bounds of the Widget relative to its parent
	 */
	protected Rectangle widgetBounds() {
		return syncExec(new Result<Rectangle>() {
			@Override
			public Rectangle run() {
				return widget.getBounds();
			}
		});
	}

	/**
	 * Gets the context menu of the given control.
	 * <p>
	 * The context menu is invoked at the center of this widget.
	 *
	 * @param control
	 *            the control.
	 * @return the context menu.
	 * @throws WidgetNotFoundException
	 *             if the widget is not found.
	 */
	@Override
	protected SWTBotRootMenu contextMenu(final Control control) throws WidgetNotFoundException {
		waitForEnabled();
		Rectangle location = widgetBounds();
		return NatTableContextMenuFinder.contextMenu(widget, location.width / 2, location.height / 2);
	}

	/**
	 * Gets the context menu on the given cell.
	 *
	 * @param row
	 *            the visible row number in the NatTable
	 * @param column
	 *            the visible column number in the NatTable
	 * @return the context menu.
	 * @throws WidgetNotFoundException
	 *             if the widget is not found.
	 */
	public SWTBotRootMenu contextMenu(int row, int column) throws WidgetNotFoundException {
		assertIsLegalCell(row, column);
		waitForEnabled();
		Rectangle location = widget.getBoundsByPosition(column, row);
		return NatTableContextMenuFinder.contextMenu(widget, location.x + (location.width / 2),
				location.y + (location.height / 2));
	}

	/**
	 * Asserts that the row and column are legal for this instance of the
	 * NatTable.
	 *
	 * @param row
	 *            the row number
	 * @param column
	 *            the column number
	 */
	protected void assertIsLegalCell(final int row, final int column) {
		int rowCount = rowCount();
		int columnCount = columnCount(); // 0 if no TableColumn has been created
		Assert.isLegal(row >= 0);
		Assert.isLegal(column >= 0);
		Assert.isLegal(row < rowCount, "The row number (" + row + ") is more than the number of visible rows (" //$NON-NLS-1$ //$NON-NLS-2$
				+ rowCount + ") in the table."); //$NON-NLS-1$
		Assert.isLegal((column < columnCount) || ((columnCount == 0) && (column == 0)), "The column number (" + column //$NON-NLS-1$
				+ ") is more than the number of visible columns (" + columnCount + ") in the table."); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Reads the NatTable Cell with the given row and column indices.
	 *
	 * @param row
	 *            the visible row number in the NatTable
	 * @param column
	 *            the visible column number in the NatTable
	 * @return String value of the cell
	 *
	 */
	public String getCellDataValueByPosition(int row, int column) {
		assertIsLegalCell(row, column);
		Object obj = widget.getDataValueByPosition(column, row);
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	/**
	 * Sets the data in the NatTable Cell with the given row and column indices.
	 *
	 * @param row
	 *            the visible row number in the NatTable
	 * @param column
	 *            the visible column number in the NatTable
	 * @param text
	 *            the text to set.
	 */
	public void setCellDataValueByPosition(final int row, final int column, final String text) {
		assertIsLegalCell(row, column);
		Assert.isNotNull(text);
		waitForEnabled();
		Assert.isTrue(!hasStyle(widget, SWT.READ_ONLY));
		asyncExec(new VoidResult() {
			@Override
			public void run() {
				click(row, column);
				ICellEditor editor = widget.getActiveCellEditor();
				editor.setEditorValue(text);
			}
		});
		notify(SWT.Modify);
	}

}
