package com.kromatik.dasshy.server.policy;

import com.kromatik.dasshy.sdk.RuntimeContext;
import com.kromatik.dasshy.server.scheduler.Job;
import com.kromatik.dasshy.server.scheduler.JobListener;
import com.kromatik.dasshy.server.streaming.PolicyJob;
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
