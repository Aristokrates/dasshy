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
	private final JobScheduler jobScheduler;

	/** policy poller */
	private final PolicyPoller policyPoller;

	/**
	 * Default constructor
	 *
	 * @param policyListener policy listener
	 * @param jobScheduler   scheduler
	 * @param policyDao      policy dao
	 */
	public StreamingEngineComponent(final PolicyListener policyListener, final JobScheduler jobScheduler,
					final PolicyDao policyDao)
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
