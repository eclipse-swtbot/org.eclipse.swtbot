/*******************************************************************************
 * Copyright (c) 2014 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.junit.Assert;
import org.junit.Test;

public class RecordStyledTextModifiedTest extends AbstractGeneratorTest {

	@Override
	protected void contributeToDialog(Composite container) {
		new StyledText(container, SWT.NONE);
	}

	@Test
	public void testModifySyledText() {
		this.bot.styledText().setText("kikoo"); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals(
			"bot.styledText().setText(\"kikoo\");", //$NON-NLS-1$
			recorderShellBot().text().getText().trim());
	}

	@Test
	public void testModifyStyledText() {
		this.bot.styledText().setText("kikoo"); //$NON-NLS-1$
		this.bot.styledText().setText("kikoolol"); //$NON-NLS-1$
		// Those 2 successive events should be merged
		flushEvents();
		Assert.assertEquals(
			"bot.styledText().setText(\"kikoolol\");", //$NON-NLS-1$
			recorderShellBot().text().getText().trim());
	}

}
