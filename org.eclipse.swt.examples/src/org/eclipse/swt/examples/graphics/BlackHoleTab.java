/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;

public class BlackHoleTab extends AnimatedGraphicsTab {

	int	size	= 1;

	public BlackHoleTab(GraphicsExample example) {
		super(example);
	}

	public String getDescription() {
		return GraphicsExample.getResourceString("BlackHoleDescription"); //$NON-NLS-1$
	}

	public String getText() {
		return GraphicsExample.getResourceString("BlackHole"); //$NON-NLS-1$
	}

	public void next(int width, int height) {
		if (size > width * 3 / 2)
			size = 0;
		else
			size += 10;
	}

	public void paint(GC gc, int width, int height) {
		Device device = gc.getDevice();
		gc.setBackground(device.getSystemColor(SWT.COLOR_BLACK));
		gc.fillOval((width - size) / 2, (height - size) / 2, size, size);
	}
}
