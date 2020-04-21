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
package org.eclipse.swtbot.swt.finder.waits;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.core.AllOf;
import org.hamcrest.number.OrderingComparison;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class WaitForWidgetInParentTest extends AbstractControlExampleTest {

	@Test
	public void waitsForWidgetToAppearInParent() throws Exception {
		long start = System.currentTimeMillis();

		WaitForObjectCondition<Widget> condition = Conditions.waitForWidget(new EvaluateTrueAfterAWhile(500), shell);
		new SWTBot().waitUntil(condition);
		long end = System.currentTimeMillis();

		int time = (int) (end - start);
		assertThat(time, AllOf.allOf(OrderingComparison.lessThan(1000), OrderingComparison.greaterThanOrEqualTo(500)));
		assertFalse(condition.getAllMatches().isEmpty());
	}

	private final class EvaluateTrueAfterAWhile extends BaseMatcher<Widget> {
		private final long	start;
		private final int	i;

		private EvaluateTrueAfterAWhile(int i) {
			this.start = System.currentTimeMillis();
			this.i = i;
		}

		@Override
		public boolean matches(Object item) {
			long diff = System.currentTimeMillis() - start;
			if (diff >= i)
				return true;
			return false;
		}

		@Override
		public void describeTo(Description description) {

		}
	}

}
