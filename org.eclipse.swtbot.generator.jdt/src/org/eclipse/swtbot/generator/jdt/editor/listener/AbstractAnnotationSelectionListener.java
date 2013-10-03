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

import java.util.List;
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
import org.eclipse.swtbot.generator.framework.AnnotationRule;
import org.eclipse.swtbot.generator.jdt.editor.document.ClassDocument;
import org.eclipse.swtbot.generator.ui.BotGeneratorEventDispatcher;

public abstract class AbstractAnnotationSelectionListener extends SelectionAdapter{
	
	protected ToolItem dropDown;
	protected Menu menu;
	protected Map<CTabItem, SourceViewer> tabViewer;
	protected CTabFolder classTabFolder;
	
	public AbstractAnnotationSelectionListener(ToolItem dropdown, BotGeneratorEventDispatcher recorder, Map<CTabItem, SourceViewer> tabViewer, CTabFolder classTabFolder) {
		this.dropDown=dropdown;
		this.menu = new Menu(dropdown.getParent());
		this.tabViewer = tabViewer;
		this.classTabFolder = classTabFolder;
		addItems(recorder.getCurrentGenerator().createAnnotationRules());
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
	
	public void addItems(List<AnnotationRule> items) {
		menu = new Menu(dropDown.getParent());
		if(items != null){
			List<AnnotationRule> filteredAnnotations = filterRules(items);
			for (AnnotationRule item : filteredAnnotations) {
				final MenuItem menuItem = new MenuItem(menu, SWT.CHECK);
				menuItem.setText(item.getAnnotation());
				menuItem.setData(item);
				menuItem.addSelectionListener(getSelectionAdapter(menuItem));
			}
		}
		update();
	}
	
	public void update(){
		SourceViewer viewer = null;
		if(classTabFolder != null && tabViewer != null){
			viewer = tabViewer.get(classTabFolder.getSelection());
		}
		if(viewer != null){
			ClassDocument doc = (ClassDocument) viewer.getDocument();
			if(doc.getActiveMethod() != null && menu.getItems().length!=0){
				dropDown.setEnabled(true);
				for (MenuItem i : menu.getItems()) {
					if (checkDocAnnotation(doc, i)) {
						i.setSelection(true);
					} else {
						i.setSelection(false);
					}
				}
			} else {
				dropDown.setEnabled(false);
			}
		} else {
			dropDown.setEnabled(false);
		}
	}
	
	protected abstract List<AnnotationRule> filterRules(List<AnnotationRule> items);
	protected abstract SelectionAdapter getSelectionAdapter(final MenuItem menuItem);
	protected abstract boolean checkDocAnnotation(ClassDocument doc, MenuItem i);

}
