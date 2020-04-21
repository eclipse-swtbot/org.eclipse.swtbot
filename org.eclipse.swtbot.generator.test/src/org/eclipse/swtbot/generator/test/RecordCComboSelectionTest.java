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

public class RecordCComboSelectionTest extends AbstractGeneratorTest {

	@Override
	protected void contributeToDialog(Composite container) {
		CCombo ccombo = new CCombo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		ccombo.add("1");
		ccombo.add("2");
	}

	@Test
	public void testModifyCombo() {
		this.bot.ccomboBox().setSelection("2"); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals("bot.ccomboBox().setSelection(\"2\");", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}
}
