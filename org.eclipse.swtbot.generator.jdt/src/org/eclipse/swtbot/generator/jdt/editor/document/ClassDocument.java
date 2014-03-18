/*******************************************************************************
 * Copyright (c) 2014 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Rastislav Wagner (Red Hat)
 *    Mickael Istria (Red Hat Inc.)
 *******************************************************************************/
package org.eclipse.swtbot.generator.jdt.editor.document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.generator.framework.AnnotationRule;
import org.eclipse.swtbot.generator.framework.GenerationRule;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.MultiTextEdit;

public class ClassDocument extends Document {

	private Set<String> imports;
	private List<Method> methods;
	private int lastOffset;
	private List<AnnotationRule> classAnnotations;
	private SourceViewer viewer;

	public ClassDocument(String className, String parentClass) {
		super();
		if (parentClass != null) {
			String[] segments = parentClass.split("\\.");
			String simpleName = segments[segments.length - 1];
			set("import " + parentClass + ";\n\n" +
					"public class " + className + " extends " + simpleName + " {\n\n}");
		} else {
			set("public class " + className + " {\n\n}");
		}
		imports = new HashSet<String>();
		imports.add("org.eclipse.swtbot.eclipse.finder.SWTBotEclipseTestCase");
		methods = new ArrayList<Method>();
		classAnnotations = new ArrayList<AnnotationRule>();
	}
	
	/**
	 * Adds rules action to document
	 * @param rule to get action from
	 */
	public void addGenerationRule(GenerationRule rule){
		List<String> codes = rule.getActions();
		if(getActiveMethod() == null){
			return;
		}
		int offset = this.imports.size()+ 4 + this.classAnnotations.size();
		
		offset = offset+ computeOffsetUntilActiveIsMet();
		offset = offset + computeActiveMethodOffset();
		
		List<String> text = new ArrayList<String>();
		for(String code : codes){
			text.add("		"+code + ";\n");
		}
		getActiveMethod().addCode(rule);
		
		addText(offset, text);
		if(rule.getImports() != null){
			for(String im : rule.getImports()){
				addImport(im);
			}
		}
		
	}
	
	private void addText(int offset, List<String> text){
		MultiTextEdit edit = new MultiTextEdit();
		
		try {
			for(String t: text){
				edit.addChild(new InsertEdit(getLineOffset(offset), t));
			}
			edit.apply(this);
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		updateColoring();
	}
	
	
	private void removeText(int offset){
		MultiTextEdit edit = new MultiTextEdit();
		try {
			edit.addChild(new DeleteEdit(getLineOffset(offset),getLineLength(offset)));
			edit.apply(this);
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		updateColoring();
	}
	
	/**
	 * Adds method to document
	 * @param methodName
	 */
	public void addMethod(String methodName){
		int offset = this.imports.size()+3;
		offset = offset + this.classAnnotations.size();
		for(Method m: methods){
			offset = offset+ m.getAllLinesSize()+3;
			offset = offset+ m.getAnnotations().size();
		}
		Method method = new Method(methodName);
		this.methods.add(method);
		
		List<String> text = new ArrayList<String>();
		text.add("	public void "+methodName+"(){" + "\n");
		text.add("	}\n\n");
		
		addText(offset, text);
		setActiveMethod(methodName);
	}
	
	private void addImport(String importCode) {
		if (this.imports.add(importCode)) {
			List<String> text = new ArrayList<String>();
			text.add("import "+importCode + ";\n");
			addText(this.imports.size() - 1, text);
		}
	}
	
	private void removeImport(String importCode){
		if(this.imports.contains(importCode)){
			String[] lines = this.get().split("\n");
			for(int i=0;i<lines.length;i++){
				if(lines[i].contains(importCode)){
					this.imports.remove(importCode);
					removeText(i);
					return;
				}
			}
		}
	}
	
	/**
	 * Sets active method
	 * @param methodName
	 */
	public void setActiveMethod(String methodName){
		getActiveMethod().setActive(false);
		for(Method m: methods){
			if(m.getName().equals(methodName)){
				m.setActive(true);
			}
		}
		updateColoring();
	}
	
	/**
	 * Removes specified annotation
	 * @param rule
	 */
	public void removeAnnotation(AnnotationRule rule){
		int offset=this.imports.size()+3 + this.classAnnotations.size();
		offset = offset + computeOffsetUntilActiveIsMet();
		offset = offset + getActiveMethod().getAnnotations().size()- getActiveMethod().getAnnotations().indexOf(rule)-1;
		
		if(getActiveMethod().removeAnnotation(rule)){
			removeText(offset);
			if(!isAnnotationPresent(rule)){
				removeImport(rule.getImportText());
			}
		}
	}
	
	/**
	 * Removes specified class annotation
	 * @param rule
	 */
	public void removeClassAnnotation(AnnotationRule rule){
		if(classAnnotations.contains(rule)){
			int offset=this.imports.size()+1 + classAnnotations.indexOf(rule);
			removeText(offset);
			classAnnotations.remove(rule);
			if(!isAnnotationPresent(rule)){
				removeImport(rule.getImportText());
			}
		}
	}

	/**
	 * Adds specified class annotation
	 * @param rule
	 */
	public void addClassAnnotation(AnnotationRule rule){
		addImport(rule.getImportText());
		int offset=this.imports.size()+1 + this.classAnnotations.size();
		List<String> text = new ArrayList<String>();
		text.add("@"+rule.getAnnotation()+"\n");
		classAnnotations.add(rule);
		addText(offset, text);
	}
	
	/**
	 * Adss specified annotation
	 * @param rule
	 */
	public void addAnnotation(AnnotationRule rule){
		int offset=computeOffsetUntilActiveIsMet();
		getActiveMethod().addAnnotation(rule);
		addImport(rule.getImportText());

		offset = offset+this.imports.size()+3 + this.classAnnotations.size();
		
		List<String> text = new ArrayList<String>();
		text.add("	@"+rule.getAnnotation()+"\n");
		
		addText(offset, text);
	}
	

	public Method getActiveMethod(){
		for(Method m: methods){
			if(m.isActive()){
				return m;
			}
		}
		return null;
	}

	public int getLastOffset() {
		return lastOffset;
	}

	private void setLastOffset(int lastOffset) {
		this.lastOffset = lastOffset;
	}
	
	
	/**
	 * Returns all currently used class annotations
	 */
	public List<AnnotationRule> getClassAnnotations() {
		return classAnnotations;
	}

	private boolean isAnnotationPresent(AnnotationRule rule){
		if(this.classAnnotations.contains(rule)){
			return true;
		}
		for(Method m: methods){
			for(AnnotationRule r: m.getAnnotations()){
				if(r.equals(rule)){
					return true;
				}
			}
		}
		return false;
	}

	private int getMethodLinesB() {
		int i =0;
		int methodLinesB = this.imports.size()+3 + this.classAnnotations.size();
		
		Method activeMethod = methods.get(i);
		while(!activeMethod.isActive()){
			i++;
			methodLinesB = methodLinesB+ activeMethod.getAllLinesSize()+3;
			methodLinesB = methodLinesB+ activeMethod.getAnnotations().size();
			activeMethod = methods.get(i);
		}
		return methodLinesB;
	}
	
	private void updateColoring(){
		if(viewer != null){
			if(getActiveMethod() != null){
				viewer.getTextWidget().setLineBackground(0,viewer.getTextWidget().getLineCount() , viewer.getTextWidget().getLineBackground(0));
				Color orange = new Color(Display.getCurrent(), 205, 205, 201);
				
				int methodsCodeSize = getActiveMethod().getAllLinesSize();
				
				viewer.getTextWidget().setLineBackground(getMethodLinesB(), getActiveMethod().getAnnotations().size() + methodsCodeSize +2, orange);
					setLastOffset(getMethodLinesB()+getActiveMethod().getAnnotations().size() + methodsCodeSize);
			}
		}
	}
	
	private int computeOffsetUntilActiveIsMet(){
		int i =0;
		int offset=0;
		Method activeMethod = methods.get(i);
		while(!activeMethod.isActive()){
			i++;
			offset = offset+ activeMethod.getAllLinesSize()+3;
			offset = offset+ activeMethod.getAnnotations().size();
			activeMethod = methods.get(i);
		}
		return offset;
	}
	
	private int computeActiveMethodOffset(){
		return getActiveMethod().getAllLinesSize() + getActiveMethod().getAnnotations().size();
	}

	public void setViewer(SourceViewer generatedCode) {
		viewer = generatedCode;
	}
	
	/**
	 * Returns all methods in document
	 * @return
	 */
	public List<Method> getMethods(){
		return methods;
	}
	
}
