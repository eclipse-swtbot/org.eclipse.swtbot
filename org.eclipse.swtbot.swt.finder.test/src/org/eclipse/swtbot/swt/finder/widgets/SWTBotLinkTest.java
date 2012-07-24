/*******************************************************************************
 * Copyright (c) 2009 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

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
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=1561910868 data=null button=1 stateMask=" + toStateMask(0, link.widget) + " x=234 y=10 count=1}"); 	
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=1561910872 data=null button=1 stateMask=" + toStateMask(524288, link.widget) + " x=234 y=10 count=1}");
	}

	@Test
	public void clicksOnALinkWithHyperlinkTextAndHREF() throws Exception {
		link.click("SWT");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=1561910868 data=null button=1 stateMask=" + toStateMask(0, link.widget) + " x=234 y=10 count=1}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=1561910872 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, link.widget) + " text=www.eclipse.org\\swt doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=1561910872 data=null button=1 stateMask=" + toStateMask(524288, link.widget) + " x=234 y=10 count=1}");
	}

	@Test
	public void clicksOnALinkWithHyperlinkTextAndNoHREF() throws Exception {
		link.click("Foo");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=1561910868 data=null button=1 stateMask=" + toStateMask(0, link.widget) + " x=234 y=10 count=1}"); 	
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=1561910872 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, link.widget) + " text=Foo doit=true}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Link {Visit the <A HREF=\"www.eclipse.org\">Eclipse.org</A> project and the <A HREF=\"www.eclipse.org\\swt\">SWT</A> homepage or <a>Foo</a>.} time=1561910872 data=null button=1 stateMask=" + toStateMask(524288, link.widget) + " x=234 y=10 count=1}");
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


