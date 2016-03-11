/*******************************************************************************
	 * Copyright (c) 2012, 2013, 2015 Original authors and others.
	 * All rights reserved. This program and the accompanying materials
	 * are made available under the terms of the Eclipse Public License v1.0
	 * which accompanies this distribution, and is available at
	 * http://www.eclipse.org/legal/epl-v10.html
	 *
	 * Contributors:
	 *     Original authors and others - initial API and implementation
	 *     Aparna Argade (Cadence Design Systems, Inc.) - Simplification for SWTBotNatTable tests
	 ******************************************************************************/
package org.eclipse.swtbot.nebula.nattable.finder.test2;

public class Person {
	private final int id;
	private String firstName;
	private String lastName;

	public Person(int id) {
		this.id = id;
	}

	public Person(int id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
