/*******************************************************************************
	 * Copyright (c) 2012, 2013, 2015 Original authors and others.
	 * All rights reserved. This program and the accompanying materials
	 * are made available under the terms of the Eclipse Public License 2.0
	 * which accompanies this distribution, and is available at
	 * https://www.eclipse.org/legal/epl-2.0/
	 *
	 * SPDX-License-Identifier: EPL-2.0
	 *
	 * Contributors:
	 *     Original authors and others - initial API and implementation
	 *     Aparna Argade (Cadence Design Systems, Inc.) - Simplification for SWTBotNatTable tests
	 ******************************************************************************/
package org.eclipse.swtbot.nebula.nattable.finder.test2;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that acts as service for accessing numerous {@link Person}s.
 *
 */
public class PersonService {

	static String[] names = { "mno", "ghi", "jkl", "abc", "def", "ghi", "jkl", "mno", "pqr", "stu", "vwx", "yz", "abc",
			"def", "jkl", "mno", "ghi", "jkl", "abc", "def", "ghi", "jkl", "mno", "pqr", "stu", "vwx", "yz", "abc",
			"def", "jkl" };
	static String[] lastNames = { "ccdd", "kkll", "iijj", "aabb", "ggff", "eeff", "gghh", "iijj", "kkll", "mmnn",
			"oopp", "pprr", "sstt", "uuvv", "wwxx", "ccdd", "kkll", "iijj", "aabb", "ggff", "eeff", "gghh", "iijj",
			"kkll", "mmnn", "oopp", "pprr", "sstt", "uuvv", "wwxx" };

	/**
	 * Creates a list of {@link Person}s.
	 *
	 * @param numberOfPersons
	 *            The number of {@link Person}s that should be generated.
	 * @return
	 */
	public static List<Person> getPersons(int numberOfPersons) {
		List<Person> result = new ArrayList<Person>();

		for (int i = 0; i < numberOfPersons; i++) {
			result.add(createPerson(i));
		}

		return result;
	}

	/**
	 * Creates a person
	 *
	 * @return Person
	 */
	public static Person createPerson(int id) {
		Person result = new Person(id);
		result.setFirstName(names[id]);
		result.setLastName(lastNames[id]);
		return result;
	}
}
