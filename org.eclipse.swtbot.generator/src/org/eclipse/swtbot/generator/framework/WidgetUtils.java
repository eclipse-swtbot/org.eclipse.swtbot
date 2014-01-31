/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *    Rastislav Wagner (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework;

import java.util.Arrays;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class WidgetUtils {

	/**
	 *
	 * @param control which index should be found
	 * @return index of control
	 */
	public static int getIndex(Control control) {
		// This is the reverse method of
		//    Matcher matcher = allOf(widgetOfType(Button.class), withStyle(SWT.PUSH, "SWT.PUSH"));
		//    return new SWTBotButton((Button) widget(matcher, index), matcher);
		// TODO? Evaluate reusing matchers here too

		int index = 0;
		Composite parent = null;
		do {
			parent = control.getParent();
			for (Control c : parent.getChildren()) {
				if(c.equals(control)){
					return index;
				} else if (c.getClass().equals(control.getClass())){
					index++;
				}
			}
		} while(!(parent instanceof Shell));

		throw new RuntimeException("Could not determine index for widget " + control);
	}

	/**
	 *
	 * @param control which group should be found
	 * @return group text or null if group was not found
	 */
	public static String getGroup(Control control){
		Composite parent = control.getParent();
		while(parent != null){
			if(parent instanceof Group){
				return ((Group)parent).getText();
			}
			parent = parent.getParent();
		}

		return null;
	}

	/**
	 *
	 * @param widget which parent shell should be found
	 * @return shell which contains widget or null
	 */
	public static Shell getShell(Control widget) {
		while (widget != null) {
			if (widget instanceof Shell) {
				return ((Shell)widget);
			} else {
				widget = ((Control)widget).getParent();
			}
		}
		return null;
	}

	/**
	 *
	 * @param widget widget which label should be found
	 * @return label text or null if no label was found
	 */
	public static String getLabel(Control widget) {
		Control[] controls = widget.getParent().getChildren();
		for (int i = 0; i < controls.length; i++) {
			if (controls[i] instanceof Label && controls[i + 1].equals(widget)) {
				return ((Label) controls[i]).getText();
			}
		}
		return null;
	}

	/**
	 * Clean text
	 * @param text text to clean
	 * @return cleaned text
	 */
	public static String cleanText(String text) {
		if(text	!= null){
			return text.replaceAll("&", "").split("\t")[0];
		}
		return null;
	}

	private static Control getPreviousControl(Control control) {
		int i = Arrays.asList(control.getParent().getChildren()).indexOf(control);
		if (i == 0) {
			return null;
		}
		return control.getParent().getChildren()[i - 1];
	}

	public static String widgetLocator(Widget widget) {
		if (widget instanceof Text) {
			return widgetLocator((Text)widget);
		} else if (widget instanceof StyledText) {
			return widgetLocator((StyledText)widget);
		}
		return "Widget not supported: " + widget.getClass().getSimpleName(); //$NON-NLS-1$
	}

	public static String widgetLocator(Text text) {
		StringBuilder res = new StringBuilder("bot."); //$NON-NLS-1$
		Control previous = getPreviousControl(text);
		if (previous instanceof Label && !((Label)previous).getText().isEmpty()) {
			res.append("textWithLabel(\""); //$NON-NLS-1$
			res.append(((Label)previous).getText());
			res.append("\")"); //$NON-NLS-1$
		} else {
			res.append("text("); //$NON-NLS-1$
			int index = getIndex(text);
			if (index != 0) {
				res.append(index);
			}
			res.append(")"); //$NON-NLS-1$
		}
		return res.toString();
	}

	public static String widgetLocator(StyledText text) {
		StringBuilder res = new StringBuilder("bot."); //$NON-NLS-1$
		Control previous = getPreviousControl(text);
		if (previous instanceof Label && !((Label)previous).getText().isEmpty()) {
			res.append("styledTextWithLabel(\""); //$NON-NLS-1$
			res.append(((Label)previous).getText());
			res.append("\")"); //$NON-NLS-1$
		} else {
			res.append("styledText("); //$NON-NLS-1$
			int index = getIndex(text);
			if (index != 0) {
				res.append(index);
			}
			res.append(")"); //$NON-NLS-1$
		}
		return res.toString();
	}

}