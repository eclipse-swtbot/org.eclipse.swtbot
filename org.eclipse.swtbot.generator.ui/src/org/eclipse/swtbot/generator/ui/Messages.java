/*******************************************************************************
 * Copyright (c) 2013 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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