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
package org.eclipse.swtbot.generator.jdt.editor.document;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.generator.framework.AnnotationRule;
import org.eclipse.swtbot.generator.framework.GenerationRule;

public class Method {
	
	private List<GenerationRule> code;
	private String name;
	private boolean active;
	private List<AnnotationRule> annotations;
	
	public Method(String name){
		this.name = name;
		code = new ArrayList<GenerationRule>();
		active = true;
		this.annotations = new ArrayList<AnnotationRule>();
	}
	
	public void addCode(GenerationRule rule){
		this.code.add(rule);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public List<GenerationRule> getCode(){
		return code;
	}

	public String getName() {
		return name;
	}

	public List<AnnotationRule> getAnnotations() {
		return annotations;
	}

	public void addAnnotation(AnnotationRule annotation) {
		this.annotations.add(annotation);
	}
	
	public boolean removeAnnotation(AnnotationRule annotation){
		return this.annotations.remove(annotation);
	}
	
	public int getAllLinesSize(){
		int methodsCodeSize = 0;
		for(GenerationRule gr: code){
			methodsCodeSize = methodsCodeSize + gr.getActions().size();
		}
		return methodsCodeSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Method other = (Method) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
	
	

}
