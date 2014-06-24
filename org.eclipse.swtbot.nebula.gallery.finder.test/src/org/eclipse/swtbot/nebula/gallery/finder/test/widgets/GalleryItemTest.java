/**
 * Copyright (C) 2010-2011 Bonitasoft S.A.
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
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class GalleryItemTest extends AbstractNebulaGalleryTestCase {
	
	@Test
	public void testGetItemByText(){
		SWTBotGalleryItem galleryItemGroup = getGalleryItem();		
		assertNotNull(galleryItemGroup.getGalleryItem("si1_2"));		
	}
	
	@Test
	public void testGetItemByIndex(){
		SWTBotGalleryItem galleryItemGroup = getGalleryItem();		
		assertNotNull(galleryItemGroup.getGalleryItem(2));
	}
	
	@Test
	public void testSelectByText(){
		SWTBotGalleryItem galleryItemGroup = getGalleryItem();		
		final String itemNameSelect = "si1_1";
		galleryItemGroup.select(itemNameSelect);
		SWTBotGallery gallery = bot.gallery();
		assertEquals("The last selected item is not the right one", gallery.selection()[0].getText(), itemNameSelect);
	}
	
	@Test
	public void testMultipleSelectByText(){		
		SWTBotGalleryItem galleryItemGroup = getGalleryItem();
		final String[] itemNameSelect = new String[]{"si1_1","si1_2"};
		galleryItemGroup.select(itemNameSelect);
		SWTBotGallery gallery = bot.gallery();
		assertEquals("Not the right number of selected elements", gallery.getSelectionCount(), itemNameSelect.length);
		SWTBotGalleryItem[] selection = gallery.selection();
		assertEquals("The first selected element is not the right one.", itemNameSelect[0], selection[0].getText());
		assertEquals("The second selected element is not the right one.", itemNameSelect[1], selection[1].getText());
	}
	
	protected SWTBotGalleryItem getGalleryItem(){
		SWTBotGallery gallery = bot.gallery();
		return gallery.getGalleryItem(1);
	}
	
}
