/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
package org.eclipse.swt.examples.paint;

import java.util.Random;

import org.eclipse.swt.graphics.Point;

/**
 * An airbrush tool.
 */
public class AirbrushTool extends ContinuousPaintSession implements PaintTool {
	private int				cachedNumPoints;
	private int				cachedRadiusSquared;
	private Random			random;
	private ToolSettings	settings;

	/**
	 * Constructs a Tool.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 * @param paintSurface
	 *            the PaintSurface we will render on.
	 */
	public AirbrushTool(ToolSettings toolSettings, PaintSurface paintSurface) {
		super(paintSurface);
		random = new Random();
		setRetriggerTimer(10);
		set(toolSettings);
	}

	/**
	 * Returns the name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintExample.getResourceString("tool.Airbrush.label");
	}

	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 */
	public void set(ToolSettings toolSettings) {
		// compute things we need to know for drawing
		settings = toolSettings;
		cachedRadiusSquared = settings.airbrushRadius * settings.airbrushRadius;
		cachedNumPoints = 314 * settings.airbrushIntensity * cachedRadiusSquared / 250000;
		if (cachedNumPoints == 0 && settings.airbrushIntensity != 0)
			cachedNumPoints = 1;
	}

	/*
	 * Template method for drawing
	 */
	protected void render(Point point) {
		// Draws a bunch (cachedNumPoints) of random pixels within a specified circle (cachedRadiusSquared).
		ContainerFigure cfig = new ContainerFigure();

		for (int i = 0; i < cachedNumPoints; ++i) {
			int randX, randY;
			do {
				randX = (int) ((random.nextDouble() - 0.5) * settings.airbrushRadius * 2.0);
				randY = (int) ((random.nextDouble() - 0.5) * settings.airbrushRadius * 2.0);
			} while (randX * randX + randY * randY > cachedRadiusSquared);
			cfig.add(new PointFigure(settings.commonForegroundColor, point.x + randX, point.y + randY));
		}
		getPaintSurface().drawFigure(cfig);
	}
}
