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
 *    Mickael Istria (Red Hat Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.junit.Assert;
import org.junit.Test;

public class RecordComboTextTest extends AbstractGeneratorTest {

	@Override
	protected void contributeToDialog(Composite container) {
		new Combo(container, SWT.DROP_DOWN);
	}

	@Test
	public void testModifyCombo() {
		this.bot.comboBox().setText("kikoo"); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals(
			"bot.comboBox().setText(\"kikoo\");", //$NON-NLS-1$
			recorderShellBot().text().getText().trim());
	}

}
