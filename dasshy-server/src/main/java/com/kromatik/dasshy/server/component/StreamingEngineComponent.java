package com.kromatik.dasshy.server.component;

import com.kromatik.dasshy.core.engine.IEngineComponent;
import com.kromatik.dasshy.server.dao.PolicyDao;
import com.kromatik.dasshy.server.policy.PolicyListener;
import com.kromatik.dasshy.server.policy.PolicyPoller;
import com.kromatik.dasshy.server.scheduler.JobScheduler;

/**
 * Streaming engine component
 */
public class StreamingEngineComponent implements IEngineComponent
{
	/** scheduler */
	private final JobScheduler				jobScheduler;

	/** policy poller */
	private final PolicyPoller				policyPoller;

	/**
	 * Default constructor
	 *
	 * @param policyListener policy listener
	 * @param jobScheduler scheduler
	 * @param policyDao policy dao
	 */
	public StreamingEngineComponent(
					final PolicyListener policyListener,
					final JobScheduler jobScheduler,
					final PolicyDao policyDao
	)
	{
		this.jobScheduler = jobScheduler;
		policyPoller = new PolicyPoller(policyListener, policyDao);
	}

	@Override
	public void start()
	{
		policyPoller.start();
	}

	@Override
	public void stop()
	{
		policyPoller.terminate();
		jobScheduler.stopAll();
	}
}
