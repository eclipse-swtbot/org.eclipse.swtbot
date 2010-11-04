/*******************************************************************************
 * Copyright (c) 2010 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.keyboard;

import static java.awt.event.KeyEvent.*;
import static org.eclipse.swtbot.swt.finder.keyboard.KeyStrokeMapping.mapping;
import static org.eclipse.swtbot.swt.finder.keyboard.KeyStrokeMapping.Modifier.NO_MASK;
import static org.eclipse.swtbot.swt.finder.keyboard.KeyboardLayout.*;
import static org.eclipse.swtbot.swt.finder.utils.OS.isWindows;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides for default mappings for the whitespace (\r, \n, \b, SPACE, \t) characters.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 */
final class KeyStrokeMappings {

	static Collection<KeyStrokeMapping> createMappings() {
		Set<KeyStrokeMapping> mappings = new HashSet<KeyStrokeMapping>();

		mappings.add(mapping(LF, VK_ENTER, NO_MASK));
		if (isWindows())
			mappings.add(mapping(CR, VK_ENTER, NO_MASK));

		mappings.add(mapping(TAB, VK_TAB, NO_MASK));

		mappings.add(mapping(BKSP, VK_BACK_SPACE, NO_MASK));
		mappings.add(mapping(DELETE, VK_DELETE, NO_MASK));

		mappings.add(mapping(ESC, VK_ESCAPE, NO_MASK));

		mappings.add(mapping(SPACE, VK_SPACE, NO_MASK));

		return mappings;
	}

}
