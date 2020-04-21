/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.results;

import org.eclipse.swt.widgets.Widget;

/**
 * Usage:
 * 
 * <pre>
 * new WidgetResult&lt;Shell&gt;() {
 * 	public Shell run() {
 * 		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
 * 	}
 * }
 * </pre>
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 * @since 2.0
 */
public interface WidgetResult<T extends Widget> extends Result<T> {
}
