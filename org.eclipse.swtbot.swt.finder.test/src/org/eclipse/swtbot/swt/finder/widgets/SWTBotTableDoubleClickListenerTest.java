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

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.UIThread;
import org.eclipse.swtbot.swt.finder.test.AbstractSWTTest;
import org.junit.Test;

public class SWTBotTableDoubleClickListenerTest extends AbstractSWTTest {

	@Test
	public void doubleClickOnCell() throws Exception {
		bot.table().doubleClick(0, 0);
		assertEquals("row 1", bot.label().getText());
		bot.table().doubleClick(1, 0);
		assertEquals("row 2", bot.label().getText());
	}

	@UIThread
	public void runUIThread() {
		Display display = Display.getDefault();

		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new TableViewerWithDoubleClickListener(shell);
		shell.open();
	}

	private static class TableViewerWithDoubleClickListener {

		public TableViewerWithDoubleClickListener(Shell shell) {
			final Label label = new Label(shell, SWT.NONE);

			final TableViewer viewer = new TableViewer(shell, SWT.BORDER
					| SWT.FULL_SELECTION);
			viewer.setContentProvider(ArrayContentProvider.getInstance());
			viewer.addDoubleClickListener(new IDoubleClickListener() {

				@Override
				public void doubleClick(DoubleClickEvent event) {

					label.setText(((StructuredSelection) event.getSelection())
							.getFirstElement().toString());
				}
			});
			viewer.setInput(new String[] { "row 1", "row 2" });
		}

	}
}
