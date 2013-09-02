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
package org.eclipse.swtbot.generator.framework.rules.annotation;
import org.eclipse.swtbot.generator.framework.AnnotationRule;

public class TestAnnotationRule extends AnnotationRule{
	
	public TestAnnotationRule(){
		setAnnotation("Test");
		setImportText("org.junit.Test");
	}

}
