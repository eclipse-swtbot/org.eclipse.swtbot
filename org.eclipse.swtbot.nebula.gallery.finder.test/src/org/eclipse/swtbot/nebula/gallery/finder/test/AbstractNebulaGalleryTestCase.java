/**
 * Copyright (C) 2010-2014 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Aurelien Pupier <aurelien.pupier@bonitasoft.com> - initial API and implementation
 */
package org.eclipse.swtbot.nebula.gallery.finder.test;

import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.nebula.gallery.finder.SWTNebulaBot;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Aurelien Pupier
 */
@RunWith(Suite.class)
public class AbstractNebulaGalleryTestCase {
	
	public static SWTNebulaBot bot;
	public static Gallery gallery;
	private Shell shell;
		
	@Before
	public void setUp() {
		bot = new SWTNebulaBot();
		runInUIThread();
	}
	
	private void runInUIThread() {
		final Display display = Display.getDefault();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				shell = createShell(display, "Nebula Gallery Test");
				gallery = createGallery(shell);
				shell.open();
			}
		});
	}	
	
	protected Shell createShell(final Display display, final String text) {
		Shell shell = new Shell(display);
		shell.setText(text);
		shell.setLayout(new FillLayout());
		return shell;
	}

	private Gallery createGallery(final Shell shell) {
		/*set contents to have something to test*/
		Gallery gallery = new Gallery(shell, SWT.V_SCROLL | SWT.MULTI);

		// Renderers
		DefaultGalleryGroupRenderer gr = new DefaultGalleryGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemHeight(56);
		gr.setItemWidth(72);
		gr.setAutoMargin(true);
		gallery.setGroupRenderer(gr);

		DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
		gallery.setItemRenderer(ir);

		// Create 3 groups
		GalleryItem items[] = new GalleryItem[3];
		for (int i = 0; i < 3; i++) {
			items[i] = new GalleryItem(gallery, SWT.None);
			items[i].setText("i" + i);
		}

		// Add content in groups
		GalleryItem subItems[][] = new GalleryItem[3][3];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				subItems[i][j] = new GalleryItem(items[i], SWT.None);
				subItems[i][j].setText("si" + i + "_" + j);
			}
		}

		return gallery;
	}
	
	@After
	public void tearDown(){
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				if(shell != null){
					shell.dispose();
				}
			}
		});
	}
	
}
