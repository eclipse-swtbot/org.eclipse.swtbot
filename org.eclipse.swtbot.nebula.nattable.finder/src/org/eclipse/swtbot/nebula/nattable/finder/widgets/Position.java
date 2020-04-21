/*******************************************************************************
 * Copyright (c) 2016 Ericsson
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Tasse - Initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.nebula.nattable.finder.widgets;

/**
 * Class that represents a NatTable position coordinate.
 *
 * @since 2.6
 */
public class Position {

	/** the row coordinate */
	public int row;
	/** the column coordinate */
	public int column;

	/**
	 * Constructor
	 *
	 * @param row the row coordinate
	 * @param column the column coordinate
	 */
	public Position(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * Copy constructor
	 *
	 * @param position a position
	 */
	public Position(Position position) {
		row = position.row;
		column = position.column;
	}

	@Override
	public String toString() {
		return String.format("[row=%d, column=%d]", row, column);
	}
}
