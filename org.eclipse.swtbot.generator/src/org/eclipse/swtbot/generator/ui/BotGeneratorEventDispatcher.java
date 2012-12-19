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
package org.eclipse.swtbot.generator.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.generator.framework.GenerationRule;
import org.eclipse.swtbot.generator.framework.Generator;

public class BotGeneratorEventDispatcher implements Listener {

	public static interface CodeGenerationListener {
		public void handleCodeGenerated(String code);
	}

	private Generator generator;
	private List<GenerationRule> generationRules;
	private List<CodeGenerationListener> listeners = new ArrayList<CodeGenerationListener>();
	private Shell ignoredShell;
	private boolean recording;
	private Event lastModifyEvent;

	public void setGenerator(Generator generator)  {
		this.generator = generator;
		this.generationRules = generator.createRules();
	}

	public void handleEvent(Event event) {
		if (this.ignoredShell != null && event.widget instanceof Control && this.ignoredShell.equals(getShell((Control)event.widget))) {
			return;
		}
		if (event.widget instanceof Control &&
			! (((Control)event.widget).isFocusControl() && ((Control)event.widget).isVisible() && ((Control)event.widget).isEnabled())) {
			return;
		}

		/* Excpetion 1: Modify Events are a stream, only last one is interesting
		We should check whether an event was supported by another rule between 2 modifies.
		If yes => It's  a new setText, apply setText rule if any
		If no => It's still the same setText, event is stored for later
		 */
		if (this.lastModifyEvent != null) {
			// unrelated event
			if (event.type != SWT.Modify || event.widget != this.lastModifyEvent.widget) {
				processRules(this.lastModifyEvent);
				this.lastModifyEvent = null;
			}
		}
		if (event.type == SWT.Modify) {
			Control control = (Control)event.widget;
			// new event or next one on same widget
			if (this.lastModifyEvent == null || this.lastModifyEvent.widget == control) {
				this.lastModifyEvent = event;
				// Store for later usage so it can be overriden if a newer ModifyEvent on samme widget happen
				return;
			}
		}

		processRules(event);
	}

	private void processRules(Event event) {
		for (GenerationRule rule : this.generationRules) {
			if (rule.appliesTo(event)) {
				rule.initializeForEvent(event);
				//rule.setPreviousEvent()
				dispatchCodeGenerated(rule.generateCode());
			}
		}
	}

	private static Shell getShell(Control widget) {
		while (widget != null) {
			if (widget.getParent() == null) {
				return (Shell)widget;
			} else {
				widget = ((Control)widget).getParent();
			}
		}
		return null;
	}


	private void dispatchCodeGenerated(String code) {
		for (CodeGenerationListener listener : this.listeners) {
			listener.handleCodeGenerated(code);
		}
	}

	public void addListener(CodeGenerationListener listener) {
		this.listeners.add(listener);
	}

	public void ignoreShell(Shell shell) {
		this.ignoredShell = shell;
	}

	public boolean isReording() {
		return this.recording;
	}

	public void switchRecording() {
		this.recording = !this.recording;
	}

	public Generator getCurrentGenerator() {
		return this.generator;
	}

}
