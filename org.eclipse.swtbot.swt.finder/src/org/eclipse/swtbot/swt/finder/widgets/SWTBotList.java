/*******************************************************************************
 * Copyright (c) 2008, 2019 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Aparna Argade - Bug 510835
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.List;
import org.eclipse.swtbot.swt.finder.ReferenceBy;
import org.eclipse.swtbot.swt.finder.SWTBotWidget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.ArrayResult;
import org.eclipse.swtbot.swt.finder.results.IntResult;
import org.eclipse.swtbot.swt.finder.results.StringResult;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.MessageFormat;
import org.eclipse.swtbot.swt.finder.utils.StringUtils;
import org.eclipse.swtbot.swt.finder.utils.internal.Assert;
import org.hamcrest.SelfDescribing;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @author Joshua Gosse &lt;jlgosse [at] ca [dot] ibm [dot] com&gt;
 * @version $Id$
 */
@SWTBotWidget(clasz = List.class, preferredName = "list", referenceBy = { ReferenceBy.LABEL })
public class SWTBotList extends AbstractSWTBotControl<List> {

	private int lastSelectionIndex;

	/**
	 * Constructs an isntance of this with the given list widget.
	 *
	 * @param list the list.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotList(List list) throws WidgetNotFoundException {
		this(list, null);
	}

	/**
	 * Constructs an isntance of this with the given list widget.
	 *
	 * @param list the list.
	 * @param description the description of the widget, this will be reported by {@link #toString()}
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotList(List list, SelfDescribing description) throws WidgetNotFoundException {
		super(list, description);
	}

	/**
	 * Selects the item matching the given text.
	 *
	 * @param item the item to select in the list.
	 */
	public void select(final String item) {
		log.debug(MessageFormat.format("Set selection {0} to text {1}", this, item)); //$NON-NLS-1$
		waitForEnabled();
		final int indexOf = indexOfChecked(item);
		processSelection(true, indexOf);
		notifySelect();
	}

	/**
	 * Selects the given index.
	 *
	 * @param index the selection index.
	 */
	public void select(final int index) {
		log.debug(MessageFormat.format("Set selection {0} to index {1}", this, index)); //$NON-NLS-1$
		waitForEnabled();
		assertIsLegalIndex(index);
		processSelection(true, index);
		notifySelect();
	}

	/**
	 * Gets the item count in the list
	 *
	 * @return the number of items in the list.
	 */
	public int itemCount() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getItemCount();
			}
		});
	}

	/**
	 * Gets the selection count.
	 *
	 * @return the number of selected items in the list.
	 */
	public int selectionCount() {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.getSelectionCount();
			}
		});
	}

	/**
	 * Gets the array of selected items.
	 *
	 * @return the selected items in the list.
	 */
	public String[] selection() {
		return syncExec(new ArrayResult<String>() {
			@Override
			public String[] run() {
				return widget.getSelection();
			}
		});
	}

	/**
	 * Selects the indexes provided.
	 *
	 * @param indices the indices to select in the list.
	 */
	public void select(final int... indices) {
		log.debug(MessageFormat.format("Set selection {0} to indices {1}]", this, StringUtils.join(indices, ", "))); //$NON-NLS-1$ //$NON-NLS-2$
		waitForEnabled();
		if (indices.length > 1)
			assertMultiSelect("multiple selections");
		for (int i = 0; i < indices.length; i++) {
			assertIsLegalIndex(indices[i]);
		}
		for (int i = 0; i < indices.length; i++) {
			if (i == 0) {
				processSelection(true, indices[0]);
				notifySelect(true);
			} else {
				processSelection(false, indices[i]);
				notifySelect(false);
			}
		}
	}

	/**
	 * Sets the selection to the given list of items.
	 *
	 * @param items the items to select in the list.
	 */
	public void select(final String... items) {
		log.debug(MessageFormat.format("Set selection {0} to items [{1}]", this, StringUtils.join(items, ", "))); //$NON-NLS-1$ //$NON-NLS-2$
		waitForEnabled();
		if (items.length > 1)
			assertMultiSelect("multiple selections");
		final int[] indices = new int[items.length];
		for (int i=0; i<items.length; i++) {
			indices[i] = indexOfChecked(items[i]);
		}
		select(indices);
	}

	private void assertMultiSelect(String msg) {
		Assert.isLegal(hasStyle(widget, SWT.MULTI), "Single Select List does not support " + msg); //$NON-NLS-1$
	}

	private void assertIsLegalIndex(final int index) {
		int totalCount = itemCount();
		Assert.isTrue(index < totalCount && index >= 0, java.text.MessageFormat.format(
				"The index ({0}) is more than the number of items ({1}) in the list.", index, totalCount)); //$NON-NLS-1$
	}

	/**
	 * @param item
	 * @return index of the String item
	 */
	private int indexOfChecked(final String item) {
		final int indexOf = indexOf(item);
		Assert.isTrue(indexOf != -1, "Item `" + item + "' not found in list."); //$NON-NLS-1$ //$NON-NLS-2$
		return indexOf;
	}

	/**
	 * Notifies of a selection.
	 */
	protected void notifySelect() {
		notifySelect(true);
	}

	/**
	 * Notifies of multiple selections.
	 * @param first true for the first selection; false otherwise
	 * @since 2.6
	 */
	protected void notifySelect(boolean first) {
		int stateMask1 = (first) ?  SWT.NONE : (SWT.NONE | SWT.MOD1);
		int stateMask2 = (first) ?  SWT.BUTTON1 : (SWT.BUTTON1 | SWT.MOD1);
		if (first) {
			notify(SWT.MouseEnter);
			notify(SWT.Activate);
			notify(SWT.FocusIn);
		}
		Point point = getXYOfSelectionIndex();
		notify(SWT.MouseDown, createMouseEvent(point.x, point.y, 1, stateMask1, 1));
		notify(SWT.Selection, createSelectionEvent(stateMask1));
		notify(SWT.MouseUp, createMouseEvent(point.x, point.y, 1, stateMask2, 1));
	}

	/**
	 * Unselects the selected items.
	 * The List must have the SWT.MULTI style.
	 */
	public void unselect() {
		assertMultiSelect("unselect");
		String[] selection = selection();
		for (int i = 0; i < selection.length; i++) {
			int index = indexOf(selection[i]);
			if (i == 0) {
				unselect(index);
			} else {
				processUnSelection(index);
				notifySelect(false);
			}
		}
	}

	/**
	 * Unselects the given row.
	 * The List must have the SWT.MULTI style.
	 * @param row index of the row to unselect
	 * @since 2.6
	 */
	public void unselect(final int row) {
		assertMultiSelect("unselect");
		waitForEnabled();
		processUnSelection(row);
		notify(SWT.MouseEnter);
		notify(SWT.Activate);
		notify(SWT.FocusIn);
		notifySelect(false);
	}

	/**
	 * Gets the index of the given item.
	 *
	 * @param item the search item.
	 * @return the index of the item, or -1 if the item does not exist.
	 */
	public int indexOf(final String item) {
		return syncExec(new IntResult() {
			@Override
			public Integer run() {
				return widget.indexOf(item);
			}
		});
	}

	/**
	 * Gets the item at the given index.
	 *
	 * @param index the zero based index.
	 * @return the item at the specified index.
	 */
	public String itemAt(final int index) {
		return syncExec(new StringResult() {
			@Override
			public String run() {
				return widget.getItem(index);
			}
		});
	}

	/**
	 * Gets the array of Strings from the List
	 *
	 * @return an array of Strings
	 */
	public String[] getItems() {
		return syncExec(new ArrayResult<String>() {
			@Override
			public String[] run() {
				return widget.getItems();
			}
		});
	}

	/**
	 * Double clicks on the given index.
	 *
	 * @param index the index to double click on the list.
	 * @since 2.7
	 */
	public void doubleClick(final int index) {
		log.debug(MessageFormat.format("Double clicking {0} on index {1}", this, index)); //$NON-NLS-1$
		select(index);
		notifyPostSelectDoubleClick();
	}

	/**
	 * Double clicks on the item matching the given text.
	 *
	 * @param item the item to double click on the list.
	 * @since 2.7
	 */
	public void doubleClick(final String item) {
		log.debug(MessageFormat.format("Double clicking {0} on text {1}", this, item)); //$NON-NLS-1$
		select(item);
		notifyPostSelectDoubleClick();
	}

	/**
	 * Double clicks on the list.
	 *
	 * @since 2.7
	 */
	public void doubleClick() {
		waitForEnabled();
		log.debug(MessageFormat.format("Double-clicking on {0}", this)); //$NON-NLS-1$
		notifySelect(true);
		notifyPostSelectDoubleClick();
	}

	/**
	 * Notifies of events sent on double click after selection.
	 */
	private void notifyPostSelectDoubleClick() {
		Point point = getXYOfSelectionIndex();
		notify(SWT.MouseDown, createMouseEvent(point.x, point.y, 1, SWT.NONE, 2));
		notify(SWT.MouseDoubleClick, createMouseEvent(point.x, point.y, 1, SWT.NONE, 2));
		notify(SWT.DefaultSelection);
		notify(SWT.MouseUp, createMouseEvent(point.x, point.y, 1, SWT.BUTTON1, 2));
	}

	/**
	 * Gets a point inside the selected item.
	 * @return Point which specifies the X,Y coordinates of the center of the selected item.
	 */
	private Point getXYOfSelectionIndex() {
		int y = syncExec(new IntResult() {
			@Override
			public Integer run() {
				int itemHeight = widget.getItemHeight();
				// zero-relative index
				int visibleIndex = lastSelectionIndex - widget.getTopIndex();
				return visibleIndex * itemHeight + (itemHeight / 2);
			}
		});
		Rectangle bounds = getBounds();
		return new Point(bounds.x + (bounds.width / 2), y);
	}

	/**
	 * Selects the specified index.
	 * @param first true for the first selection; false otherwise.
	 * @param index the zero based index.
	 */
	private void processSelection(final boolean first, final int index) {
		syncExec(new VoidResult() {
			@Override
			public void run() {
				if (first) {
					// removes earlier selection
					widget.setSelection(index);
				} else {
					widget.select(index);
				}
				lastSelectionIndex = index;
			}
		});
	}

	/**
	 * Unselects the specified index.
	 * @param index the zero based index.
	 */
	private void processUnSelection(final int index) {
		syncExec(new VoidResult() {
			@Override
			public void run() {
				widget.deselect(index);
				lastSelectionIndex = index;
			}
		});
	}

}
