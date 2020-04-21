/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
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
package org.eclipse.swtbot.swt.finder.utils.internal;

import java.lang.reflect.Method;

import org.eclipse.swtbot.swt.finder.results.StringResult;


/**
 * This is an object use to invoke a method using reflections.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 */
public final class ReflectionInvoker implements StringResult {
	/**
	 * The object to invoke a method on.
	 */
	private final Object	w;
	/**
	 * The method to invoke.
	 */
	private final String	methodName;

	/**
	 * Constructs this object.
	 *
	 * @param o the object to be invoked on.
	 * @param methodName the method to invoke on the object.
	 */
	public ReflectionInvoker(Object o, String methodName) {
		w = o;
		this.methodName = methodName;
	}

	/**
	 * Runs the processing to trigger the method to be invoked.
	 *
	 * @see org.eclipse.swtbot.swt.finder.results.StringResult#run()
	 * @return The results of the invoke.
	 */
	@Override
	public String run() {
		String result = ""; //$NON-NLS-1$
		try {
			Method method = w.getClass().getMethod(methodName, new Class[]{});
			Object invoke = method.invoke(w, new Object[0]);
			if (invoke != null)
				result = invoke.toString();
		} catch (Exception e) {
			// do nothing
		}
		return result;
	}
}
