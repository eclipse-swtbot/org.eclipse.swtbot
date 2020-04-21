/*******************************************************************************
 * Copyright (c) 2009, 2016 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Patrick Tasse - Fix SWTBotLink.click() (Bug 337548)
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotLinkTest extends AbstractControlExampleTest {

	private SWTBotText	listeners;
	private SWTBotLink link;

	@Test
	public void clicksOnALink() throws Exception {
		link.click();
		assertEventMatches(listeners, "MouseEnter [6]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=0 stateMask=" + toStateMask(0, link.widget) + " x=0 y=0 count=0}");
		assertEventMatches(listeners, "Activate [26]: ShellEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusIn [15]: FocusEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=1 stateMask=" + toStateMask(0, link.widget) + " x=0 y=0 count=1}"); 	
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, link.widget) + " text=www.eclipse.org doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=1 stateMask=" + toStateMask(SWT.BUTTON1, link.widget) + " x=0 y=0 count=1}");
	}

	@Test
	public void clicksOnALinkAtIndexWithHREF() throws Exception {
		link.click(1);
		assertEventMatches(listeners, "MouseEnter [6]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=0 stateMask=" + toStateMask(0, link.widget) + " x=0 y=0 count=0}");
		assertEventMatches(listeners, "Activate [26]: ShellEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusIn [15]: FocusEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=1 stateMask=" + toStateMask(0, link.widget) + " x=0 y=0 count=1}"); 	
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, link.widget) + " text=www.eclipse.org\\swt doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=1 stateMask=" + toStateMask(SWT.BUTTON1, link.widget) + " x=0 y=0 count=1}");
	}

	@Test
	public void clicksOnALinkAtIndexWithNoHREF() throws Exception {
		link.click(2);
		assertEventMatches(listeners, "MouseEnter [6]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=0 stateMask=" + toStateMask(0, link.widget) + " x=0 y=0 count=0}");
		assertEventMatches(listeners, "Activate [26]: ShellEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusIn [15]: FocusEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=1 stateMask=" + toStateMask(0, link.widget) + " x=0 y=0 count=1}"); 	
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, link.widget) + " text=Foo doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=1 stateMask=" + toStateMask(SWT.BUTTON1, link.widget) + " x=0 y=0 count=1}");
	}

	@Test
	public void clicksOnALinkAtIndexOutOfRange() throws Exception {
		link.click(3);
		assertEventMatches(listeners, "MouseEnter [6]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=0 stateMask=" + toStateMask(0, link.widget) + " x=0 y=0 count=0}");
		assertEventMatches(listeners, "Activate [26]: ShellEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusIn [15]: FocusEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=1 stateMask=" + toStateMask(0, link.widget) + " x=0 y=0 count=1}"); 	
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=1 stateMask=" + toStateMask(SWT.BUTTON1, link.widget) + " x=0 y=0 count=1}");
	}

	@Test
	public void clicksOnALinkWithHyperlinkTextAndHREF() throws Exception {
		link.click("SWT");
		assertEventMatches(listeners, "MouseEnter [6]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=0 stateMask=" + toStateMask(0, link.widget) + " x=0 y=0 count=0}");
		assertEventMatches(listeners, "Activate [26]: ShellEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusIn [15]: FocusEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=1 stateMask=" + toStateMask(0, link.widget) + " x=0 y=0 count=1}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, link.widget) + " text=www.eclipse.org\\swt doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=1 stateMask=" + toStateMask(SWT.BUTTON1, link.widget) + " x=0 y=0 count=1}");
	}

	@Test
	public void clicksOnALinkWithHyperlinkTextAndNoHREF() throws Exception {
		link.click("Foo");
		assertEventMatches(listeners, "MouseEnter [6]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=0 stateMask=" + toStateMask(0, link.widget) + " x=0 y=0 count=0}");
		assertEventMatches(listeners, "Activate [26]: ShellEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null doit=true}");
		assertEventMatches(listeners, "FocusIn [15]: FocusEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=1 stateMask=" + toStateMask(0, link.widget) + " x=0 y=0 count=1}"); 	
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, link.widget) + " text=Foo doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=0 data=null button=1 stateMask=" + toStateMask(SWT.BUTTON1, link.widget) + " x=0 y=0 count=1}");
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("Link").activate();
		bot.checkBox("Listen").select();
		bot.button("Clear").click();
		this.link = bot.link();
		listeners = bot.textInGroup("Listeners");
	}

}


