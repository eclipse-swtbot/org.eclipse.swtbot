/*******************************************************************************
 * Copyright (c) 2017 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
