/**
 * Copyright (C) 2010 Bonitasoft S.A.
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
package org.eclipse.swtbot.nebula.gallery.finder.test.widgets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.swtbot.nebula.gallery.finder.test.AbstractNebulaGalleryTestCase;
import org.eclipse.swtbot.nebula.gallery.finder.widgets.SWTBotGallery;
import org.eclipse.swtbot.nebula.gallery.finder.widgets.SWTBotGalleryItem;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Aurelien Pupier
 *
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class GalleryTest extends AbstractNebulaGalleryTestCase {
	
	@Test
	public void testCount() {
		SWTBotGallery gallery = bot.gallery();
		assertEquals("Three groups is attended", 3, gallery.getItemCount());
	}
	
	@Test
	public void testSelectUnselect(){
		SWTBotGallery gallery = bot.gallery();
		gallery.select(new int[]{0,2});
		assertEquals("There is not the correct number of selection", 2, gallery.getSelectionCount());
		
		gallery.unselect();
		assertEquals("Unselection doesn't work", 0, gallery.getSelectionCount());
	}
	
	@Test
	public void testSelectByText(){
		SWTBotGallery gallery = bot.gallery();
		gallery.select(new String[]{"i2"});
		assertEquals("There is not the correct number of selection", 1, gallery.getSelectionCount());
		assertEquals("This is not the correct element that is selected.","i2",gallery.selection()[0].getText());
	}
	
	@Test
	public void testMultipleSelectByText(){
		SWTBotGallery gallery = bot.gallery();
		gallery.select(new String[]{"i0","i2"});
		assertEquals("There is not the correct number of selection", 2, gallery.getSelectionCount());
		assertEquals("The first element is not the correct element selected.","i0",gallery.selection()[0].getText());
		assertEquals("The first element is not the correct element selected.","i2",gallery.selection()[1].getText());
	}
	
	@Test
	public void testGetGalleryItemByIndex(){
		SWTBotGallery gallery = bot.gallery();
		SWTBotGalleryItem galleryItemGroup = gallery.getGalleryItem(1);
		assertNotNull("Can't select a Gallery Item by index", galleryItemGroup);
		assertEquals("i1", galleryItemGroup.getText());
		SWTBotGalleryItem galleryItem = galleryItemGroup.getGalleryItem(2);
		assertEquals("si1_2", galleryItem.getText());
	}
	
	@Test
	public void testGetGalleryItemByText(){
		SWTBotGallery gallery = bot.gallery();
		SWTBotGalleryItem galleryItemGroup = gallery.getGalleryItem("i1");
		assertNotNull("Can't select a Gallery Item by text", galleryItemGroup);
		assertEquals("i1", galleryItemGroup.getText());
		SWTBotGalleryItem galleryItem = galleryItemGroup.getGalleryItem("si1_2");
		assertEquals("si1_2", galleryItem.getText());
	}

}
