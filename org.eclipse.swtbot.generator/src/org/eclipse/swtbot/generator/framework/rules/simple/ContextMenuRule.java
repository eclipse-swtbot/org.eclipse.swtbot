package org.eclipse.swtbot.generator.framework.rules.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class ContextMenuRule extends GenerationSimpleRule{
	
	private List<String> path;
	private String menu;

	@Override
	public boolean appliesTo(Event event) {
		boolean menu = event.widget instanceof MenuItem;
		int style = 0;
		if(menu){
			MenuItem currentItem = ((MenuItem)event.widget);
			Menu parent = null;
			while (currentItem != null && (parent = currentItem.getParent()) != null) {
				style = parent.getStyle();
				currentItem = parent.getParentItem();
			}
		}
		return event.type == SWT.Selection && menu && (style & SWT.POP_UP)!=0;
	}

	@Override
	public void initializeForEvent(Event event) {
		MenuItem item = (MenuItem) event.widget;
		menu = WidgetUtils.cleanText(item.getText());
		path = new ArrayList<String>();
		MenuItem currentItem = item;
		Menu parent = null;
		while (currentItem != null && (parent = currentItem.getParent()) != null) {
			currentItem = parent.getParentItem();
			if (currentItem != null && currentItem.getText() != null) {
				path.add(WidgetUtils.cleanText(currentItem.getText()));
			}
		}
		Collections.reverse(path);
	}
	
	public List<String> getPath() {
		return path;
	}

	public void setPath(List<String> path) {
		this.path = path;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	@Override
	public List<String> getActions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
