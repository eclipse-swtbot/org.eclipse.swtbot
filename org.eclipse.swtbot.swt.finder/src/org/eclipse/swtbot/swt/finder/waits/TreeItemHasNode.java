/*******************************************************************************
 * Copyright (c) 2017 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Patrick Tasse - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.waits;

import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.utils.internal.Assert;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

/**
 * A condition that returns <code>false</code> until the tree item has a node
 * with the specified text. While the node is not found, if the tree item is
 * expanded it is collapsed and re-expanded to attempt to make the node appear.
 *
 * @see Conditions
 */
class TreeItemHasNode extends DefaultCondition {

	/**
	 * The text of the child node to look for.
	 */
	private final String	text;

	/**
	 * The tree item (SWTBotTreeItem) instance to check.
	 */
	private final SWTBotTreeItem	treeItem;

	/**
	 * Constructs an instance of the condition for the given tree item.
	 *
	 * @param treeItem the treeItem
	 * @param text the text of the child node to look for
	 * @throws NullPointerException Thrown if the tree item is <code>null</code>.
	 */
	TreeItemHasNode(SWTBotTreeItem treeItem, String text) {
		Assert.isNotNull(treeItem, "The tree item can not be null"); //$NON-NLS-1$
		this.treeItem = treeItem;
		this.text = text;
	}

	/**
	 * Performs the check to see if the condition is satisfied.
	 *
	 * @see org.eclipse.swtbot.swt.finder.waits.ICondition#test()
	 * @return <code>true</code> if the tree item has a node with the given
	 *         text. Otherwise <code>false</code> is returned.
	 */
	public boolean test() {
		boolean hasNode = UIThreadRunnable.syncExec(new BoolResult() {
			public Boolean run() {
				for (TreeItem item : treeItem.widget.getItems()) {
					if (item.getText().equals(text)) {
						return true;
					}
				}
				return false;
			}
		});
		if (!hasNode && treeItem.isExpanded()) {
			/*
			 * Sometimes in a TreeViewer, the model is not yet ready when
			 * expanding a tree item. Collapse and re-expand the tree item to
			 * refresh the tree item and attempt to make the node appear.
			 */
			treeItem.collapse();
			treeItem.expand();
		}
		return hasNode;
	}

	/**
	 * Gets the failure message if the test is not satisfied.
	 *
	 * @see org.eclipse.swtbot.swt.finder.waits.ICondition#getFailureMessage()
	 * @return The failure message.
	 */
	public String getFailureMessage() {
		return "Timed out waiting for " + treeItem + " to contain a node with text: " + text; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
