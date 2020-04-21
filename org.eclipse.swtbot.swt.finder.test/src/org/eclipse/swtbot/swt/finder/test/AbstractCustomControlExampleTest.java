/*******************************************************************************
 * Copyright (c) 2010 Ketan Padegaonkar and others.
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
package org.eclipse.swtbot.swt.finder.test;

import org.eclipse.swt.examples.controlexample.ControlExample;
import org.eclipse.swt.examples.controlexample.CustomControlExample;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public abstract class AbstractCustomControlExampleTest extends AbstractSWTShellTest {

	@Override
	protected final void createUI(Composite parent) {
		shell.setText(ControlExample.getResourceString("custom.window.title"));
		new CustomControlExample(shell);
	}
	
}