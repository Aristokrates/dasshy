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
package com.kromatik.dasshy.server.policy;

import com.kromatik.dasshy.sdk.RuntimeContext;
import com.kromatik.dasshy.server.scheduler.Job;
import com.kromatik.dasshy.server.scheduler.JobListener;
import com.kromatik.dasshy.server.spark.PolicyJob;
import com.kromatik.dasshy.server.scheduler.JobScheduler;
import com.kromatik.dasshy.thrift.model.TPolicy;

/**
 * Manage jobs per policy
 */
public class JobsOnPolicyListener implements PolicyListener
{
	/** runtime context */
	private final RuntimeContext			runtimeContext;

	/** policy factory */
	private final PolicyFactory				policyFactory;

	/** scheduler */
	private final JobScheduler				jobScheduler;

	/** job update listener */
	private final JobListener				jobListener;

	/**
	 * Default constructor
	 *
	 * @param runtimeContext runtime context
	 * @param policyFactory policy factory
	 * @param jobScheduler scheduler
	 * @param jobListener job update listener
	 */
	public JobsOnPolicyListener(
					final RuntimeContext runtimeContext,
					final PolicyFactory policyFactory,
					final JobScheduler jobScheduler,
					final JobListener jobListener
	)
	{
		this.runtimeContext = runtimeContext;
		this.policyFactory = policyFactory;
		this.jobScheduler = jobScheduler;
		this.jobListener = jobListener;
	}

	@Override
	public void onPolicySave(TPolicy policyModel)
	{
		String policyId = policyModel.getId();

		Job existingPolicyJob = jobScheduler.get(policyId);
		if (existingPolicyJob != null)
		{
			// TODO (pai) stop and restart only if the job has been updated
			jobScheduler.stop(policyId);
		}

		Policy policy = policyFactory.buildPolicy(policyModel);
		PolicyJob policyJob = new PolicyJob(policy, runtimeContext, jobListener);

		jobScheduler.submit(policyJob);
	}

	@Override
	public void onPolicyDelete(TPolicy policyModel)
	{
		jobScheduler.stop(policyModel.getId());
	}
}
