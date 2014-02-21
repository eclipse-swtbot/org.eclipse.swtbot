/*******************************************************************************
 * Copyright (c) 2013=2014 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.junit.Assert;
import org.junit.Test;

public class RecordComboSelectionTest extends AbstractGeneratorTest {

	@Override
	protected void contributeToDialog(Composite container) {
		Combo combo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.add("1");
		combo.add("2");
	}

	@Test
	public void testModifyCombo() {
		this.bot.comboBox().setSelection("2"); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals(
			"bot.comboBox().setSelection(\"2\");", //$NON-NLS-1$
			recorderShellBot().text().getText().trim());
	}
	
}
