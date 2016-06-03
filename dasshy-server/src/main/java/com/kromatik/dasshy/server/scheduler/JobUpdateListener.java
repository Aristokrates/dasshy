package com.kromatik.dasshy.server.scheduler;

import com.kromatik.dasshy.server.dao.PolicyDao;
import com.kromatik.dasshy.server.event.JobEvent;
import com.kromatik.dasshy.server.streaming.PolicyJob;
import com.kromatik.dasshy.thrift.model.TJobState;
import com.kromatik.dasshy.thrift.model.TPolicy;

/**
 * Policy job update listener
 */
public class JobUpdateListener implements JobListener
{
	/** policy dao */
	private final PolicyDao			policyDao;

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
}
