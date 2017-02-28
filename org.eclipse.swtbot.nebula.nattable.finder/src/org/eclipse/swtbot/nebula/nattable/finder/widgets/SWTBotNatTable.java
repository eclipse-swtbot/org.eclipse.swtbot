/*******************************************************************************
 * Copyright (c) 2016 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Aparna Argade(Cadence Design Systems, Inc.) - initial API and implementation
 *     Patrick Tasse - Support viewport scrolling (Bug 504483)
 *     Aparna Argade(Cadence Design Systems, Inc.) - Bug 512815
 *******************************************************************************/
package org.eclipse.swtbot.nebula.nattable.finder.widgets;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.coordinate.PixelCoordinate;
import org.eclipse.nebula.widgets.nattable.edit.editor.ICellEditor;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
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
 * All row and column positions are relative to the visible layer. The row and
 * column positions include headers also and are affected by filtering, sorting
 * and scrolling, etc.
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
	 *            the visible row position in the NatTable
	 * @param column
	 *            the visible column position in the NatTable
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
	 *            the visible row position in the NatTable
	 * @param column
	 *            the visible column position in the NatTable
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
	 *            the visible row position in the NatTable
	 * @param column
	 *            the visible column position in the NatTable
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
	 *            the visible row position in the NatTable
	 * @param column
	 *            the visible column position in the NatTable
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
	 *            the row position
	 * @param column
	 *            the column position
	 */
	protected void assertIsLegalCell(final int row, final int column) {
		int rowCount = rowCount();
		int columnCount = columnCount(); // 0 if no TableColumn has been created
		Assert.isLegal(row >= 0, "The row number (" + row + ") is out of bounds");
		Assert.isLegal(column >= 0, "The column number (" + column + ") is out of bounds");
		Assert.isLegal(row < rowCount, "The row number (" + row + ") is more than the number of visible rows (" //$NON-NLS-1$ //$NON-NLS-2$
				+ rowCount + ") in the NatTable."); //$NON-NLS-1$
		Assert.isLegal((column < columnCount) || ((columnCount == 0) && (column == 0)), "The column number (" + column //$NON-NLS-1$
				+ ") is more than the number of visible columns (" + columnCount + ") in the NatTable."); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Reads the NatTable cell with the given row and column positions.
	 *
	 * @param row
	 *            the visible row position in the NatTable
	 * @param column
	 *            the visible column position in the NatTable
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
	 * Reads the NatTable cell with the given position.
	 *
	 * @param position
	 *            the visible position in the NatTable
	 * @return String value of the cell
	 * @since 2.6
	 */
	public String getCellDataValueByPosition(Position position) {
		return getCellDataValueByPosition(position.row, position.column);
	}

	/**
	 * Sets the data in the NatTable cell with the given row and column
	 * positions.
	 *
	 * @param row
	 *            the visible row position in the NatTable
	 * @param column
	 *            the visible column position in the NatTable
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

	/**
	 * Gets the first underlying viewport layer at the specified NatTable
	 * position. The <code>position</code> parameter will be modified to the
	 * corresponding position in the viewport layer.
	 *
	 * @param position
	 *            the position in the NatTable as input, the position in the
	 *            viewport layer as output
	 * @return the ViewportLayer, or null if none was found in the layer stack
	 */
	private ViewportLayer getViewportLayer(Position position) {
		ILayer layer = widget.getLayer();
		while (layer != null) {
			if (layer instanceof ViewportLayer) {
				return (ViewportLayer) layer;
			}
			ILayer underlyingLayer = layer.getUnderlyingLayerByPosition(position.column, position.row);
			position.column = layer.localToUnderlyingColumnPosition(position.column);
			position.row = layer.localToUnderlyingRowPosition(position.row);
			layer = underlyingLayer;
		}
		return null;
	}

	/**
	 * Scrolls the viewport found at the specified NatTable position so that the
	 * cell at its underlying scrollable layer position is made visible.
	 * <p>
	 * Returns the NatTable position where the specified scrollable layer cell
	 * can be found after scrolling. If possible, this will be the same as the
	 * NatTable input position, but can be a different position if the viewport
	 * cannot be scrolled far enough.
	 *
	 * @param position
	 *            a visible position in the NatTable with an underlying viewport
	 * @param scrollableRow
	 *            the row (in the viewport's underlying scrollable layer's
	 *            coordinates) of the cell that should be made visible
	 * @param scrollableColumn
	 *            the column (in the viewport's underlying scrollable layer's
	 *            coordinates) of the cell that should be made visible
	 * @return the NatTable position of the visible cell after scrolling
	 * @throws IllegalArgumentException
	 *             if the specified position does not have an underlying
	 *             viewport layer, or if the specified scrollable layer position
	 *             is out of bounds
	 * @since 2.6
	 */
	public Position scrollViewport(Position position, int scrollableRow, int scrollableColumn) {
		Position viewportPosition = new Position(position);
		ViewportLayer viewportLayer = getViewportLayer(viewportPosition);
		/*
		 * after returning, viewportPosition has been modified to be the
		 * corresponding position in the viewport layer's coordinates
		 */
		if (viewportLayer == null) {
			throw new IllegalArgumentException("No viewport layer found at position " + position);
		}
		ILayer scrollableLayer = viewportLayer.getUnderlyingLayerByPosition(viewportPosition.column,
				viewportPosition.row);
		if (scrollableColumn < 0 || scrollableColumn >= scrollableLayer.getColumnCount() || scrollableRow < 0
				|| scrollableRow >= scrollableLayer.getRowCount()) {
			throw new IllegalArgumentException(
					"Scrollable position " + new Position(scrollableRow, scrollableColumn) + " is out of range.");
		}
		Position underlyingPosition = new Position(viewportLayer.localToUnderlyingRowPosition(viewportPosition.row),
				viewportLayer.localToUnderlyingColumnPosition(viewportPosition.column));
		Rectangle underlyingBounds = scrollableLayer.getBoundsByPosition(underlyingPosition.column,
				underlyingPosition.row);
		Rectangle desiredBounds = scrollableLayer.getBoundsByPosition(scrollableColumn, scrollableRow);
		PixelCoordinate pixelOffset = new PixelCoordinate(desiredBounds.x - underlyingBounds.x,
				desiredBounds.y - underlyingBounds.y);
		PixelCoordinate origin = viewportLayer.getOrigin();
		viewportLayer.setOriginX(origin.getX() + pixelOffset.getX());
		viewportLayer.setOriginY(origin.getY() + pixelOffset.getY());
		Position newUnderlyingPosition = new Position(viewportLayer.localToUnderlyingRowPosition(viewportPosition.row),
				viewportLayer.localToUnderlyingColumnPosition(viewportPosition.column));
		Position offset = new Position(scrollableRow - newUnderlyingPosition.row,
				scrollableColumn - newUnderlyingPosition.column);
		return new Position(position.row + offset.row, position.column + offset.column);
	}

	/**
	 * Scrolls the viewport found at the specified NatTable position so that the
	 * row header cell is made visible.
	 * <p>
	 * Returns the NatTable position where the specified row header cell can be
	 * found after scrolling.
	 *
	 * @param position
	 *            a visible position in the NatTable with the underlying
	 *            viewport linked to the row header
	 * @param scrollableRow
	 *            the row (in the viewport's underlying scrollable layer's
	 *            coordinates) of the header cell that should be made visible
	 * @param headerColumn
	 *            the fixed header column (in NatTable coordinates)
	 * @return the NatTable position of the visible row header cell after
	 *         scrolling
	 * @throws IllegalArgumentException
	 *             if the specified position does not have an underlying
	 *             viewport layer, or if the specified scrollable layer position
	 *             is out of bounds
	 * @since 2.6
	 */
	public Position scrollToRowHeader(Position position, int scrollableRow,
			int headerColumn)
	{

		Position viewportPosition = new Position(position);
		ViewportLayer viewportLayer = getViewportLayer(viewportPosition);
		if (viewportLayer == null) {
			throw new IllegalArgumentException("No viewport layer found at position " + position);
		}
		int scrollableColumn = viewportLayer.localToUnderlyingColumnPosition(viewportPosition.column);

		int row = scrollViewport(position, scrollableRow, scrollableColumn).row;
		return new Position(row, headerColumn);
	}

	/**
	 * Scrolls the viewport found at the specified NatTable position so that the
	 * column header cell is made visible.
	 * <p>
	 * Returns the NatTable position where the specified column header cell can
	 * be found after scrolling.
	 *
	 * @param position
	 *            a visible position in the NatTable with the underlying
	 *            viewport linked to the column header
	 * @param headerRow
	 *            the fixed header row (in NatTable coordinates)
	 * @param scrollableColumn
	 *            the column (in the viewport's underlying scrollable layer's
	 *            coordinates) of the header cell that should be made visible
	 * @return the NatTable position of the visible column header cell after
	 *         scrolling
	 * @throws IllegalArgumentException
	 *             if the specified position does not have an underlying
	 *             viewport layer, or if the specified scrollable layer position
	 *             is out of bounds
	 * @since 2.6
	 */
	public Position scrollToColumnHeader(Position position, int headerRow,
			int scrollableColumn)
	{
		Position viewportPosition = new Position(position);
		ViewportLayer viewportLayer = getViewportLayer(viewportPosition);
		if (viewportLayer == null) {
			throw new IllegalArgumentException("No viewport layer found at position " + position);
		}
		int scrollableRow = viewportLayer.localToUnderlyingRowPosition(viewportPosition.row);

		int column = scrollViewport(position, scrollableRow, scrollableColumn).column;
		return new Position(headerRow, column);
	}

	/**
	 * Returns <code>true</code> if the given label is associated with the cell;
	 * <code>false</code> otherwise.
	 *
	 * @param row
	 *            the visible row position in the NatTable
	 * @param column
	 *            the visible column position in the NatTable
	 * @param label
	 *            the config label
	 *
	 * @return <code>true</code> if the cell has label; <code>false</code>
	 *         otherwise
	 * @since 2.6
	 */
	public boolean hasConfigLabel(int row, int column, String label) {
		assertIsLegalCell(row, column);
		LabelStack labels = widget.getConfigLabelsByPosition(column, row);
		return labels.hasLabel(label);
	}

}
