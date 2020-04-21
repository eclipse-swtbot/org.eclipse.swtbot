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
 *     Ketan Padegaonkar - http://swtbot.org/bugzilla/show_bug.cgi?id=126
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.matchers;


import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.finders.PathGenerator;
import org.eclipse.swtbot.swt.finder.utils.ClassUtils;
import org.eclipse.swtbot.swt.finder.utils.MessageFormat;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.utils.TreePath;
import org.hamcrest.BaseMatcher;
import org.hamcrest.StringDescription;

/**
 * A matcher that logs the result of matches. The match is done by subclasses.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public abstract class AbstractMatcher<T> extends BaseMatcher<T> {

	public static final Logger	log	= Logger.getLogger(AbstractMatcher.class);

	@Override
	public boolean matches(Object item) {
		boolean result = false;
		try {
			result = doMatch(item);
			String text = ""; //$NON-NLS-1$
			if (log.isDebugEnabled()) {
				text = SWTUtils.getText(item);
				try {
					if (text.length() > 20)
						text = text.substring(0, 20) + "..."; //$NON-NLS-1$
				} catch (Exception e) {
					// do nothing
				}
				if (result) {
					log.debug(MessageFormat.format("matched {0} with text \"{1}\", using matcher: {2}", ClassUtils.simpleClassName(item), text, StringDescription.toString(this))); //$NON-NLS-1$
				} else
					log.trace(MessageFormat.format("did not match {0} with text \"{1}\", using matcher: {2}", ClassUtils.simpleClassName(item), text, StringDescription.toString(this))); //$NON-NLS-1$
			}

			if (log.isTraceEnabled() && (item instanceof Widget)) {
				PathGenerator pathGenerator = new PathGenerator();
				TreePath path = pathGenerator.getPath((Widget) item);
				int segmentCount = path.getSegmentCount();

				StringBuilder line = new StringBuilder();
				// Indentation
				for (int i = 0; i < segmentCount - 1; i++) {
					line.append("    "); //$NON-NLS-1$
				}
				line.append("+---"); //$NON-NLS-1$
				line.append("Widget: "); //$//$NON-NLS-1$
				line.append(ClassUtils.simpleClassName(item));
				line.append("{"); //$NON-NLS-1$
				line.append(text);
				line.append("}"); //$NON-NLS-1$
				log.trace(line.toString());
			}
		} catch (Exception e) {
			log.warn("Matcher threw an exception: ", e); //$NON-NLS-1$
		}
		return result;
	}

	/**
	 * Subclasses must override, this is the actual method that does the matching
	 *
	 * @param item the item to match.
	 * @return <code>true</code> if the item matches, <code>false</code> otherwise.
	 */
	protected abstract boolean doMatch(Object item);

}
