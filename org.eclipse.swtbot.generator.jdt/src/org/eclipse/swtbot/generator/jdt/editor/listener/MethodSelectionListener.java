/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Rastislav Wagner (Red Hat)
 *******************************************************************************/
package org.eclipse.swtbot.generator.jdt.editor.listener;

import java.util.Map;

import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swtbot.generator.ui.BotGeneratorEventDispatcher;
import org.eclipse.swtbot.generator.jdt.editor.document.ClassDocument;

public class MethodSelectionListener extends SelectionAdapter {
	private ToolItem dropdown;
	private Map<CTabItem, SourceViewer> tabViewer;
	private CTabFolder classTabFolder;
	private Menu menu;
	private ToolItem annotationsToolItem;

	public MethodSelectionListener(ToolItem dropdown, BotGeneratorEventDispatcher recorder, Map<CTabItem, SourceViewer> tabViewer, CTabFolder classTabFolder, ToolItem annotationsToolItem) {
		this.dropdown = dropdown;
		this.menu = new Menu(dropdown.getParent());
		this.classTabFolder = classTabFolder;
		this.tabViewer = tabViewer;
		this.annotationsToolItem = annotationsToolItem;
	}

	public void add(String item) {
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText(item);
		menuItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				MenuItem selected = (MenuItem) event.widget;
				dropdown.setText(selected.getText());
				SourceViewer viewer = tabViewer.get(classTabFolder.getSelection());
				ClassDocument doc = (ClassDocument) viewer.getDocument();
				doc.setActiveMethod(selected.getText());
				((AnnotationSelectionListener)annotationsToolItem.getData()).update();
				viewer.setTopIndex(((ClassDocument) viewer.getDocument()).getLastOffset()-4);
			}
		});
		dropdown.setText(item);
		SourceViewer viewer = tabViewer.get(classTabFolder.getSelection());
		ClassDocument doc = (ClassDocument) viewer.getDocument();
		doc.setActiveMethod(menuItem.getText());
		((AnnotationSelectionListener)annotationsToolItem.getData()).update();

	}
	@Override
	public void widgetSelected(SelectionEvent event) {
		if (event.detail == SWT.ARROW) {
			ToolItem item = (ToolItem) event.widget;
			Rectangle rect = item.getBounds();
			Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
			menu.setLocation(pt.x, pt.y + rect.height);
			menu.setVisible(true);
		}
	}
}
