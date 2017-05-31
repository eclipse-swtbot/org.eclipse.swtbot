/*******************************************************************************
 * Copyright (c) 2008, 2017 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.matchers;

import org.eclipse.ui.IPerspectiveDescriptor;

public abstract class WidgetMatcherFactory extends org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory {

  /**
   * Matches a workbench part (view/editor) with the specified name.
   * 
   * @param text the label of the part.
   * @return a matcher.
   * @since 2.0
   */
  public static <T extends org.eclipse.ui.IWorkbenchPartReference> org.hamcrest.Matcher<T> withPartName(java.lang.String text) {
    return org.eclipse.swtbot.eclipse.finder.matchers.WithPartName.withPartName(text);
  }

  /**
   * Matches a workbench part (view/editor) with the specified name.
   * 
   * @param nameMatcher the part name matcher.
   * @return a matcher.
   * @since 2.0
   */
  public static <T extends org.eclipse.ui.IWorkbenchPartReference> org.hamcrest.Matcher<T> withPartName(org.hamcrest.Matcher<java.lang.String> nameMatcher) {
    return org.eclipse.swtbot.eclipse.finder.matchers.WithPartName.withPartName(nameMatcher);
  }

  /**
   * Matches a workbench part (view/editor) with the specified id.
   * 
   * @param id the id of the part.
   * @return a matcher.
   * @since 2.0
   */
  public static <T extends org.eclipse.ui.IWorkbenchPartReference> org.hamcrest.Matcher<T> withPartId(java.lang.String id) {
    return org.eclipse.swtbot.eclipse.finder.matchers.WithPartId.withPartId(id);
  }

  /**
   * Matches a workbench part (view/editor) with the specified id.
   * 
   * @param idMatcher the part id matcher.
   * @return a matcher.
   * @since 2.0
   */
  public static <T extends org.eclipse.ui.IWorkbenchPartReference> org.hamcrest.Matcher<T> withPartId(org.hamcrest.Matcher<java.lang.String> idMatcher) {
    return org.eclipse.swtbot.eclipse.finder.matchers.WithPartId.withPartId(idMatcher);
  }

  /**
   * Matches a workbench part (view/editor) with the specified title.
   * 
   * @param title the title of the part.
   * @return a matcher.
   * @since 2.2
   */
  public static <T extends org.eclipse.ui.IWorkbenchPartReference> org.hamcrest.Matcher<T> withTitle(java.lang.String title) {
	  return org.eclipse.swtbot.eclipse.finder.matchers.WithTitle.withTitle(title);
  }
  
  /**
   * Matches a workbench part (view/editor) with the specified id.
   * 
   * @param titleMatcher the part title matcher.
   * @return a matcher.
   * @since 2.2
   */
  public static <T extends org.eclipse.ui.IWorkbenchPartReference> org.hamcrest.Matcher<T> withTitle(org.hamcrest.Matcher<java.lang.String> titleMatcher) {
	  return org.eclipse.swtbot.eclipse.finder.matchers.WithTitle.withTitle(titleMatcher);
  }

  /**
   * Matches a perspective with the specified id.
   * 
   * @param id the id of the perspective.
   * @return a matcher.
   * @since 2.0
   */
  public static org.hamcrest.Matcher<IPerspectiveDescriptor> withPerspectiveId(java.lang.String id) {
    return org.eclipse.swtbot.eclipse.finder.matchers.WithPerspectiveId.withPerspectiveId(id);
  }

  /**
   * Matches a perspective with the specified id.
   * 
   * @param idMatcher the matcher that matches the id of the perspective.
   * @return a matcher.
   * @since 2.0
   */
  public static org.hamcrest.Matcher<IPerspectiveDescriptor> withPerspectiveId(org.hamcrest.Matcher<java.lang.String> idMatcher) {
    return org.eclipse.swtbot.eclipse.finder.matchers.WithPerspectiveId.withPerspectiveId(idMatcher);
  }

  /**
   * Matches a perspective with the specified label.
   * 
   * @param label the label of the perspective.
   * @return a matcher.
   * @since 2.0
   */
  public static org.hamcrest.Matcher<IPerspectiveDescriptor> withPerspectiveLabel(java.lang.String label) {
    return org.eclipse.swtbot.eclipse.finder.matchers.WithPerspectiveLabel.withPerspectiveLabel(label);
  }

  /**
   * Matches a perspective with the specified label.
   * 
   * @param labelMatcher the matcher that matches the perspective label.
   * @return a matcher.
   * @since 2.0
   */
  public static org.hamcrest.Matcher<IPerspectiveDescriptor> withPerspectiveLabel(org.hamcrest.Matcher<java.lang.String> labelMatcher) {
    return org.eclipse.swtbot.eclipse.finder.matchers.WithPerspectiveLabel.withPerspectiveLabel(labelMatcher);
  }
  

}
