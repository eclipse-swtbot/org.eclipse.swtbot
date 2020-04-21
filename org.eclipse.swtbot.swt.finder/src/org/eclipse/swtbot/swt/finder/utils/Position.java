/*******************************************************************************
 * Copyright (c) 2008-2010 Ketan Padegaonkar and others.
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

/**
 * An object that represents a position in terms of line and column number.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class Position {

	/** the line number */
	public final int	line;
	/** the column number */
	public final int	column;

	/**
	 * Create a position.
	 * 
	 * @param line the line number.
	 * @param column the column number.
	 */
	public Position(int line, int column) {
		this.line = line;
		this.column = column;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + line;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		Position other = (Position) obj;
		return (column == other.column) && (line == other.line);
	}

	@Override
	public String toString() {
		return "Position: (" + line + ", " + column + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
