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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swtbot.generator.framework.AnnotationRule;
import org.eclipse.swtbot.generator.ui.BotGeneratorEventDispatcher;
import org.eclipse.swtbot.generator.jdt.editor.document.ClassDocument;

public class AnnotationSelectionListener extends AbstractAnnotationSelectionListener {

	public AnnotationSelectionListener(ToolItem dropdown, BotGeneratorEventDispatcher recorder, Map<CTabItem, SourceViewer> tabViewer, CTabFolder classTabFolder) {
		super(dropdown,recorder,tabViewer,classTabFolder);
	}

	@Override
	protected List<AnnotationRule> filterRules(List<AnnotationRule> items) {
		List<AnnotationRule> methodAnnotations = new ArrayList<AnnotationRule>();
		for(AnnotationRule item: items){
			if(!item.isClassAnnotation()){
				methodAnnotations.add(item);
			}
		}
		return methodAnnotations;
	}
	@Override
	protected SelectionAdapter getSelectionAdapter(final MenuItem menuItem) {
		return new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent event) {
				MenuItem selected = (MenuItem) event.widget;
				SourceViewer viewer = tabViewer.get(classTabFolder.getSelection());
				ClassDocument doc = (ClassDocument) viewer.getDocument();
				if (selected.getSelection()) {
					doc.addAnnotation((AnnotationRule) menuItem.getData());
				} else {
					doc.removeAnnotation((AnnotationRule) menuItem.getData());
				}
				update();
			}
		};
	}
	@Override
	protected boolean checkDocAnnotation(ClassDocument doc, MenuItem i) {
		return doc.getActiveMethod().getAnnotations().contains(i.getData());
	}
}
