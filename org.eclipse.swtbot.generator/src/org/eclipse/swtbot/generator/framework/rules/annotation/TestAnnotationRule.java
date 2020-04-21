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
package org.eclipse.swtbot.generator.framework.rules.annotation;
import org.eclipse.swtbot.generator.framework.AnnotationRule;

public class TestAnnotationRule extends AnnotationRule{
	
	public TestAnnotationRule(){
		setAnnotation("Test");
		setImportText("org.junit.Test");
	}

}
