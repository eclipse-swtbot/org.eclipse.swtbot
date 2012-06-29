/*******************************************************************************
 * Copyright (c) 2008 Cedric Chabanois and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cedric Chabanois - initial API and implementation
 *     Mickael Istria (PetalsLink) - Fix bug 362476
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.junit.Assert.assertEquals;

import org.eclipse.jface.snippets.viewers.Snippet009CellEditors;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.UIThread;
import org.eclipse.swtbot.swt.finder.test.AbstractSWTTest;
import org.junit.Test;

/**
 * @author Cedric Chabanois &lt;cchabanois [at] no-log [dot] org&gt;
 * @version $Id$
 * @since 1.2
 */
public class SWTBotTableClickTest extends AbstractSWTTest {

	@Test
	public void clickOnCell() throws Exception {
		SWTBotTable table = bot.table();
		table.click(0, 0);
		bot.sleep(1000);
		bot.text("0", 0).setText("101");
		bot.sleep(1000);
		table.click(1, 0);
		bot.sleep(1000);
		assertEquals("Item 101", table.cell(0, 0));
	}
	
	@UIThread
	public void runUIThread() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new Snippet009CellEditors(shell);
		shell.open ();
	}

}
