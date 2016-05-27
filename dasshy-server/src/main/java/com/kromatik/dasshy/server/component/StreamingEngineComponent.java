package com.kromatik.dasshy.server.component;

import com.kromatik.dasshy.core.engine.IEngineComponent;
import com.kromatik.dasshy.server.dao.PolicyDao;
import com.kromatik.dasshy.server.policy.PolicyListener;
import com.kromatik.dasshy.server.policy.PolicyPoller;
import com.kromatik.dasshy.server.scheduler.PolicyScheduler;

/**
 * Streaming engine component
 */
public class StreamingEngineComponent implements IEngineComponent
{
	/** scheduler */
	private final PolicyScheduler			policyScheduler;

	/** policy poller */
	private final PolicyPoller				policyPoller;

	/**
	 * Default constructor
	 *
	 * @param policyListener policy listener
	 * @param policyScheduler scheduler
	 * @param policyDao policy dao
	 */
	public StreamingEngineComponent(
					final PolicyListener policyListener,
					final PolicyScheduler policyScheduler,
					final PolicyDao policyDao
	)
	{
		this.policyScheduler = policyScheduler;
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
		policyScheduler.stopAll();
	}
}
