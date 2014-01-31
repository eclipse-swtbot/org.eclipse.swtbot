/*******************************************************************************
 * Copyright (c) 2013 Red Hat Inc..
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.junit.Assert;
import org.junit.Test;

public class RecordToolbarButtonTest extends AbstractGeneratorTest {

	private Image image;

	@Override
	protected void contributeToDialog(Composite container) {
		ToolBar toolbar = new ToolBar(container, SWT.NONE);
		ToolItem item = new ToolItem(toolbar, SWT.PUSH);
		item.setText("button1"); //$NON-NLS-1$
		item.setToolTipText("button1.tooltip"); //$NON-NLS-1$
		ToolItem item2 = new ToolItem(toolbar, SWT.PUSH);
		this.image = new Image(container.getDisplay(), new Rectangle(0, 0, 16, 16));
		item2.setImage(this.image);
		item2.setText("button2"); //$NON-NLS-1$
		item2.setToolTipText("button2.tooltip"); //$NON-NLS-1$
		ToolItem item3 = new ToolItem(toolbar, SWT.PUSH);
		item3.setImage(this.image);
		item3.setToolTipText("button3.tooltip"); //$NON-NLS-1$
	}

	@Test
	public void testClickToolbar() {
		this.bot.toolbarButton("button1").click(); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals(
			"bot.toolbarButton(\"button1\").click();", //$NON-NLS-1$
			recorderShellBot().text().getText().trim());
	}

	@Test
	public void testClickToolbarWithImage() {
		this.bot.toolbarButton("button2").click(); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals(
			"bot.toolbarButton(\"button2\").click();", //$NON-NLS-1$
			recorderShellBot().text().getText().trim());
	}

	@Test
	public void testClickToolbarWithImageAndNoText() {
		this.bot.toolbarButtonWithTooltip("button3.tooltip").click(); //$NON-NLS-1$
		flushEvents();
		Assert.assertEquals(
			"bot.toolbarButtonWithTooltip(\"button3.tooltip\").click();", //$NON-NLS-1$
			recorderShellBot().text().getText().trim());
	}

	@Override
	public void tearDown() {
		super.tearDown();
		this.image.dispose();
	}

}
