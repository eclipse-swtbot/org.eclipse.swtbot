/*******************************************************************************
 * Copyright (c) 2010, 2017 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileUtilsTest {

	private static final File	FILE	= new File("somefile.txt");

	@Before
	public void setUp() {
		if (!FILE.exists()) {
			FileUtils.write("some text", FILE);
		}
	}

	@Test
	public void canWriteToAFile() throws Exception {
		// the write part is execute @Before
		String read = FileUtils.read(FILE);
		assertEquals("some text", read);

		FileUtils.write("some other text", FILE);
		read = FileUtils.read(FILE);
		assertEquals("some other text", read);
	}

	@Test
	public void canReadFromFile() throws Exception {
		String read = FileUtils.read("somefile.txt");
		assertEquals("some text", read);
	}

	@Test
	public void canReadFromURL() throws Exception {
		String read = FileUtils.read(FILE);
		assertEquals("some text", read);
	}

	@After
	public void cleanup() {
		if (FILE.exists()) {
			FILE.delete();
		}
	}
}
