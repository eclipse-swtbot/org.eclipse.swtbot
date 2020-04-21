/*******************************************************************************
 * Copyright (c) 2014 Kalray and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Xavier Raynaud <xavier.raynaud@kalray.eu> - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.waits;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.ICondition;

/**
 * Waits until all {@link Job}s of the given family are finished.
 * 
 * @author Xavier Raynaud <xavier.raynaud@kalay.eu>
 * @since 2.3
 */
public class WaitForJobs implements ICondition {

    private final Object mFamily;
	private final String mHumanReadableJobFamily;
    
	/**
	 * Creates a condition that waits until all jobs of the given family are done.
	 * 
	 * @param family a job family.
	 * @param humanReadableJobFamily a human readable name for the job family.
	 *   It may be null, it serves only for the failure message.
	 */
    WaitForJobs(Object family, String humanReadableJobFamily) {
        this.mFamily = family;
        this.mHumanReadableJobFamily = humanReadableJobFamily;
    }
    
    @Override
	public boolean test() throws Exception {
        Job[] allJobs = Job.getJobManager().find(mFamily);
        return allJobs.length == 0;
    }
    
    @Override
	public void init(SWTBot bot) {
    }

    @Override
	public String getFailureMessage() {
    	String errMsg = "Wait for jobs failed: ";
    	if (mHumanReadableJobFamily != null) {
    		return mHumanReadableJobFamily + " jobs are still running.";
    	}
        return errMsg;
    }

}
