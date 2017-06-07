/*******************************************************************************
 * Copyright (c) 2013 Kristine Jetzke and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kristine Jetzke - initial implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.UIThread;
import org.eclipse.swtbot.swt.finder.test.AbstractSWTTest;
import org.junit.Test;

public class SWTBotTreeItemDoubleClickListenerTest extends AbstractSWTTest {

	@Test
	public void doubleClickOnCell() throws Exception {
		final SWTBotTreeItem treeItem1 = bot.tree().getTreeItem("1");
		treeItem1.doubleClick();
		assertEquals("1", bot.label().getText());
		treeItem1.expand().getNode(0).doubleClick();
		assertEquals("1 child 1", bot.label().getText());

		final SWTBotTreeItem treeItem2 = bot.tree().getTreeItem("2");
		treeItem2.doubleClick();
		assertEquals("2", bot.label().getText());
		treeItem2.expand().getNode(1).doubleClick();
		assertEquals("2 child 2", bot.label().getText());

	}

	@UIThread
	public void runUIThread() {
		Display display = Display.getDefault();

		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new TreeViewerWithDoubleClickListener(shell);
		shell.open();
	}

	private static class TreeViewerWithDoubleClickListener {

		public TreeViewerWithDoubleClickListener(Shell shell) {
			final Label label = new Label(shell, SWT.NONE);

			final TreeViewer viewer = new TreeViewer(shell, SWT.BORDER
					| SWT.FULL_SELECTION);
			viewer.setContentProvider(new ITreeContentProvider() {

				@Override
				public void inputChanged(Viewer viewer, Object oldInput,
						Object newInput) {
				}

				@Override
				public void dispose() {
				}

				@Override
				public boolean hasChildren(Object element) {
					return !(element instanceof String);
				}

				@Override
				public Object getParent(Object element) {
					return null;
				}

				@Override
				public Object[] getElements(Object inputElement) {
					if (inputElement instanceof Model[]) {
						return (Object[]) inputElement;
					}
					if (inputElement instanceof Model) {
						return ((Model) inputElement).children.toArray();
					}
					return new Object[0];
				}

				@Override
				public Object[] getChildren(Object parentElement) {
					if (parentElement instanceof Model[]) {
						return (Object[]) parentElement;
					}
					if (parentElement instanceof Model) {
						return ((Model) parentElement).children.toArray();
					}
					return new Object[0];
				}
			});

			viewer.addDoubleClickListener(new IDoubleClickListener() {

				@Override
				public void doubleClick(DoubleClickEvent event) {

					label.setText(((StructuredSelection) event.getSelection())
							.getFirstElement().toString());
				}
			});

			viewer.setInput(new Model[] { new Model("1"), new Model("2") });
		}
	}

	private static class Model {

		private List<String> children = new ArrayList<String>();

		private String name;

		public Model(String name) {
			this.name = name;
			children.add(name + " child 1");
			children.add(name + " child 2");
		}

		@Override
		public String toString() {
			return name;
		}

	}
}
