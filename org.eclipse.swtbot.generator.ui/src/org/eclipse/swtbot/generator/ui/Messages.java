/*******************************************************************************
 * Copyright (c) 2013 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Rastislav Wagner (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
  private static final String BUNDLE_NAME
    = "org.eclipse.swtbot.generator.ui.messages"; //$NON-NLS-1$
  public static String newEclipseInstance;
  public static String currentEclipseInstance;
  public static String eclipseGroup;
  public static String recorderDialog;
  public static String recorderDialogTitle;
  public static String recorderDescription;
  public static String errorWhileConfiguratingRuntime;

  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {
  }
}