/*******************************************************************************
 * Copyright (c) 2009 Obeo
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Mariot Chauvin <mariot.chauvin@obeo.fr> - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.logic.test.messages;

import org.eclipse.osgi.util.NLS;

public class EclipseUIMessages extends NLS {

	   private static final String BUNDLE_NAME = "org.eclipse.gef.examples.logic.test.messages.eclipseUIMessages"; //$NON-NLS-1$

	    static {
	        // initialize resource bundle
	        NLS.initializeMessages(BUNDLE_NAME, EclipseUIMessages.class);
	    }
	    
	    public static String menuFile;
	    
	    public static String menuFile_New;
	    
	    public static String menuFile_New_Other;
	    
	    public static String wizardNext;
	    
	    public static String wizardFinish;
	    
}
