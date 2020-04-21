/*******************************************************************************
 * Copyright (c) 2017 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Aparna Argade - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.junit.Assert;
import org.junit.Test;

public class RecordCComboTextTest extends AbstractGeneratorTest {

	@Override
	protected void contributeToDialog(Composite container) {
		new CCombo(container, SWT.DROP_DOWN);
	}

	@Test
	public void testModifyCCombo() {
		this.bot.ccomboBox().setText("kikoo"); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals("bot.ccomboBox().setText(\"kikoo\");", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}

}
