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
 *    Mickael Istria (Red Hat) - initial API and implementation
 *    Rastislav Wagner (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
/**
 * This class represents very basic simple rule which is being matched to one event
 *
 */
public abstract class GenerationSimpleRule extends GenerationRule{

	/**
	 * Checks whether event applies to this rule
	 * @param event to check if it applies to rule
	 * @return true if rules applies to given rule, false otherwise
	 */
	public abstract boolean appliesTo(Event event);

	/**
	 * Initializes rule for given event
	 * @param event to get needed data from
	 */
	public abstract void initializeForEvent(Event event);

	/**
	 * @return the widget which triggered the event for this rule
	 * Widget may be disposed.
	 * Only applies if appliesTo() == true and after initializeForEvent
	 */
	public abstract Widget getWidget();

}
