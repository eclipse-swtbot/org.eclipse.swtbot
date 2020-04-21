/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Rastislav Wagner (Red Hat)
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework;
/**
 * This class represents annotation rule
 *
 */
public class AnnotationRule {
	
	private String annotation;
	private String importText;
	private boolean classAnnotation;
	
	public String getAnnotation() {
		return annotation;
	}
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	public String getImportText() {
		return importText;
	}
	public void setImportText(String importText) {
		this.importText = importText;
	}
	public boolean isClassAnnotation() {
		return classAnnotation;
	}
	public void setClassAnnotation(boolean classAnnotation) {
		this.classAnnotation = classAnnotation;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((annotation == null) ? 0 : annotation.hashCode());
		result = prime * result + (classAnnotation ? 1231 : 1237);
		result = prime * result
				+ ((importText == null) ? 0 : importText.hashCode());
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
		AnnotationRule other = (AnnotationRule) obj;
		if (annotation == null) {
			if (other.annotation != null)
				return false;
		} else if (!annotation.equals(other.annotation))
			return false;
		if (classAnnotation != other.classAnnotation)
			return false;
		if (importText == null) {
			if (other.importText != null)
				return false;
		} else if (!importText.equals(other.importText))
			return false;
		return true;
	}

}
