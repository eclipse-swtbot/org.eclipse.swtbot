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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.generator.framework.GenerationComplexRule;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.Generator;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class BotGeneratorEventDispatcher implements Listener{


	public static interface CodeGenerationListener {
		public void handleCodeGenerated(String code);
	}

	private List<GenerationSimpleRule> listOfSimpleRules = new ArrayList<GenerationSimpleRule>();
	
	private Generator generator;
	private List<CodeGenerationListener> listeners = new ArrayList<CodeGenerationListener>();
	private Shell ignoredShell;
	private boolean recording;
	private Event lastModifyEvent;
	
	private GenerationComplexRule longestMatchedComplex;

	private boolean newGenerationComplexRules = true;
	private List<GenerationComplexRule> modifGenerationComplexRules = new ArrayList<GenerationComplexRule>();
	
	public void setGenerator(Generator generator) {
		this.generator = generator;
	}
	
	public void addListener(CodeGenerationListener listener) {
		this.listeners.add(listener);
	}

	public void ignoreShell(Shell shell) {
		this.ignoredShell = shell;
	}

	public boolean isRecording() {
		return this.recording;
	}

	public void switchRecording() {
		this.recording = !this.recording;
	}

	public Generator getCurrentGenerator() {
		return this.generator;
	}
	
	public void handleEvent(Event event) {
		if (!recording) {
			return;
		}
		if(event.widget instanceof Control){
			Shell shell = WidgetUtils.getShell((Control) event.widget);
			if(shell.getParent() instanceof Shell){
				if (this.ignoredShell != null && this.ignoredShell.equals(shell.getParent())) {
					return;
				}
			}
		}
		if (!(event.widget instanceof Shell) && event.widget instanceof Control
				&& !(((Control) event.widget).isFocusControl()
						&& ((Control) event.widget).isVisible() 
							&& ((Control) event.widget).isEnabled())) {
			return;
		}
		

		/*
		 * Excpetion 1: Modify Events are a stream, only last one is interesting
		 * We should check whether an event was supported by another rule
		 * between 2 modifies. If yes => It's a new setText, apply setText rule
		 * if any If no => It's still the same setText, event is stored for
		 * later
		 */
		if (this.lastModifyEvent != null) {
			// unrelated event
			if (event.type != SWT.Modify
					|| event.widget != this.lastModifyEvent.widget) {
				processRules(this.lastModifyEvent);
				this.lastModifyEvent = null;
			}
		}
		if (event.type == SWT.Modify) {
			if(event.widget instanceof Text){
				if(((Text)event.widget).getMessage() != null){
					if(((Text)event.widget).getMessage().equals(((Text)event.widget).getText())){
						return;
					} else if(((Text)event.widget).getText().isEmpty()){
						return;
					}
				}
			}
 			Control control = (Control) event.widget;
			// new event or next one on same widget
			if (this.lastModifyEvent == null
					|| this.lastModifyEvent.widget == control) {
				this.lastModifyEvent = event;
	 			// Store for later usage so it can be overriden if a newer
				// ModifyEvent on samme widget happen
				return;
			}
		}
		
		processRules(event);
	}
	
	private void processRules(Event event) {
		for (GenerationSimpleRule rule : generator.createSimpleRules()) {
			if (rule.appliesTo(event)) {
				rule.initializeForEvent(event);
				listOfSimpleRules.add(rule); //store simple rule for further processing
			}
		}
		if(!listOfSimpleRules.isEmpty()){  //find complex rules			
			
			if(newGenerationComplexRules){
				modifGenerationComplexRules.addAll(generator.createComplexRules());
				Collections.sort(modifGenerationComplexRules, new Comparator<GenerationComplexRule>() {
					public int compare(GenerationComplexRule g1, GenerationComplexRule g2) {
						return g2.getRules().size()-g1.getRules().size();
					}
				});
			}
			newGenerationComplexRules = true;
			Set<GenerationComplexRule> toDelete = matchComplexRules(modifGenerationComplexRules);
			modifGenerationComplexRules.removeAll(toDelete);
			if(longestMatchedComplex != null && ((modifGenerationComplexRules.size() == 0)||
					(modifGenerationComplexRules.size() == 1 && longestMatchedComplex.equals(modifGenerationComplexRules.get(0))))){ //match
					processComplexMatch();
			} else if(modifGenerationComplexRules.size() == 0 && longestMatchedComplex == null){//no match,
				processComplexNoMatch(); //keep looking for match without first simple rule 
			} else if(modifGenerationComplexRules.size() > 0){ //there's possibility to find better match
				newGenerationComplexRules = false; //get next event
				return;
			}
		}
			
	}
	
	private void processComplexMatch(){
		List<GenerationSimpleRule> rulesToKeep = new ArrayList<GenerationSimpleRule>();
		int toKeep = listOfSimpleRules.size()-longestMatchedComplex.getRules().size();
		for(int i=toKeep; i>0; i--){
			rulesToKeep.add(listOfSimpleRules.get(listOfSimpleRules.size()-i));
		}
		listOfSimpleRules.removeAll(rulesToKeep);
		longestMatchedComplex.initializeForRules(listOfSimpleRules);
		dispatchCodeGenerated(WidgetUtils.cleanText(longestMatchedComplex.generateCode()));
		longestMatchedComplex=null;
		listOfSimpleRules.clear();
		listOfSimpleRules.addAll(rulesToKeep);
	}
	
	private void processComplexNoMatch(){
		dispatchCodeGenerated(WidgetUtils.cleanText(listOfSimpleRules.get(0).generateCode())); //generate code for first simple rule
		listOfSimpleRules.remove(0);
	}
	
	private Set<GenerationComplexRule> matchComplexRules(List<GenerationComplexRule> modifGenerationComplexRules){
		Set<GenerationComplexRule> toDelete= new HashSet<GenerationComplexRule>();
		for(int i=0; i<listOfSimpleRules.size(); i++){
			for(GenerationComplexRule complexRule: modifGenerationComplexRules){
				if(complexRule.getRules().size() < listOfSimpleRules.size()){ //complex rule has less rules that listofRules
					toDelete.add(complexRule);
				}else{
					if(!complexRule.appliesTo(listOfSimpleRules.get(i), i)){
						toDelete.add(complexRule);
					} else if(complexRule.getRules().size()-1==i){
						longestMatchedComplex=complexRule;
					}
				}
			}
		}
		return toDelete;
	}

	private void dispatchCodeGenerated(String code) {
		if(code != null){
			for (CodeGenerationListener listener : this.listeners) {
				listener.handleCodeGenerated(code);
			}
		}
	}

}	