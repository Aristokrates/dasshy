/**
 * Dasshy - Real time and Batch Analytics Open Source System
 * Copyright (C) 2016 Kromatik Solutions (http://kromatiksolutions.com)
 *
 * This file is part of Dasshy
 *
 * Dasshy is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Dasshy is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Dasshy.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kromatik.dasshy.server.scheduler;

import com.kromatik.dasshy.server.dao.PolicyDao;
import com.kromatik.dasshy.server.event.JobEvent;
import com.kromatik.dasshy.server.spark.PolicyJob;
import com.kromatik.dasshy.thrift.model.TJobState;
import com.kromatik.dasshy.thrift.model.TPolicy;

/**
 * Policy job update listener
 */
public class JobUpdateListener implements JobListener
{
	/** policy dao */
	private final PolicyDao policyDao;

	/**
	 * Default constructor
	 *
	 * @param policyDao policy dao
	 */
	public JobUpdateListener(final PolicyDao policyDao)
	{
		this.policyDao = policyDao;
	}

	@Override
	public void onProgressUpdate(final Job job, int progress)
	{
		// no action
	}

	@Override
	public void onStateChange(final Job job, final TJobState before, final TJobState after)
	{
		if (job instanceof PolicyJob)
		{

			// save job
			final PolicyJob policyJob = (PolicyJob) job;

			final TPolicy policy = policyJob.getPolicy().getModel();

			if (policyJob.startTime != null)
			{
				policy.setStartTime(policyJob.startTime);
			}

			if (policyJob.endTime != null)
			{
				policy.setEndTime(policyJob.endTime);
			}

			policy.setError(policyJob.errorMessage);
			policy.setState(after);

			policyDao.update(policy);
		}
	}

	@Override
	public void onJobEvent(final JobEvent jobEvent)
	{
		// save the event
	}

	@Override
	public void onJobResult(final Job job, final Object jobResult)
	{
		// save/add the job results

	}
}
