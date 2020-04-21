/*******************************************************************************
 * Copyright (c) 2010 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 */
public class TreePathTest {

	@Test
	public void canPopLastElementFromTreePath() throws Exception {
		TreePath treePath = new TreePath(1, 2, 3, 4);
		assertEquals(4, treePath.pop());
		assertEquals(3, treePath.getSegmentCount());
		assertEquals(new TreePath(1, 2, 3), treePath);
	}
	
	@Test
	public void popsNothingIfTreePathIsEmpty() throws Exception {
		TreePath treePath = new TreePath();
		assertNull(treePath.pop());
		assertEquals(0, treePath.getSegmentCount());
	}

}
