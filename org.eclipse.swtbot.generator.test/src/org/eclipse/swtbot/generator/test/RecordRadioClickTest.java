/*******************************************************************************
 * Copyright (c) 2019 Cadence Design Systems, Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Aparna Argade - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.junit.Assert;
import org.junit.Test;

public class RecordRadioClickTest extends AbstractGeneratorTest {

	@Override
	protected void contributeToDialog(Composite container) {
		Group group = new Group(container, SWT.NONE);
		group.setLayout(new RowLayout(SWT.HORIZONTAL));
		Button radio1 = new Button(group, SWT.RADIO);
		radio1.setText("One"); //$NON-NLS-1$
		Button radio2 = new Button(group, SWT.RADIO);
		radio2.setText("Two"); //$NON-NLS-1$
	}

	@Test
	public void testRadioButtonClick() {
		this.bot.radio("Two").click(); // $NON-NLS-1$
		flushEvents();
		Assert.assertEquals("bot.radio(\"Two\").click();", //$NON-NLS-1$
				recorderShellBot().text().getText().trim());
	}
}
