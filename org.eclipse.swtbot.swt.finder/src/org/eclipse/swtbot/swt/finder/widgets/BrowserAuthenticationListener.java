/*******************************************************************************
 * Copyright (c) 2010 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Mariot Chauvin, Obeo - initial API and implementation
 *******************************************************************************/

package org.eclipse.swtbot.swt.finder.widgets;

import org.eclipse.swt.browser.AuthenticationEvent;
import org.eclipse.swt.browser.AuthenticationListener;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.Credentials;

/**
 * A browser authentication listener.
 * This implementation requires SWT 3.5 or later version.
 * @author mchauvin
 */
final class BrowserAuthenticationListener implements AuthenticationListener {
	
	private Credentials	credentials;

	public void init(final Browser widget) {
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
				widget.addAuthenticationListener(BrowserAuthenticationListener.this);
			}
		});
	}
	
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public Credentials getCredentials() {
		return this.credentials;
	}

	@Override
	public void authenticate(AuthenticationEvent event) {
		if (credentials == null) {
			event.doit = false;
			return;
		}
		event.doit = true;
		event.user = credentials.username();
		event.password = credentials.password();
	}

}