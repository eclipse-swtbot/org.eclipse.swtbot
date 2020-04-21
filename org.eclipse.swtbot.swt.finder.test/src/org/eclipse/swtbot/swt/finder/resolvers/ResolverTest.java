/*******************************************************************************
 * Copyright (c) 2008, 2017 Ketan Padegaonkar and others.
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
package org.eclipse.swtbot.swt.finder.resolvers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class ResolverTest {

	@Test
	public void doesNotGetResolverForNonRegisteredTypes() throws Exception {
		Resolver resolver = new Resolver();
		resolver.addResolver(new CompositeResolver());

		List<IResolvable> resolvers = resolver.getResolvers(FileInputStream.class);
		assertTrue(resolvers.isEmpty());
	}

	@Test
	public void getsResolverForRegisteredTypes() throws Exception {
		Resolver resolver = new Resolver();
		resolver.addResolver(new CompositeResolver());

		List<IResolvable> resolvers = resolver.getResolvers(Shell.class);
		assertFalse(resolvers.isEmpty());
	}

}
