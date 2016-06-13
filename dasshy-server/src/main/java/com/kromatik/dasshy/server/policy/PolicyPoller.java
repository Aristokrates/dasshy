/**
 * Dasshy - Real time Streaming and Batch Analytics Open Source System
 * Copyright (C) 2016 Kromatik Solutions
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

import com.kromatik.dasshy.server.dao.PolicyDao;
import com.kromatik.dasshy.thrift.model.TPolicy;

/**
 * Policy Poller thread
 */
public class PolicyPoller extends Thread
{

	public static final long 					DEFAULT_POLLING_INTERVAL	= 50000;

	/** polling interval */
	private long								pollingInterval;

	/** flag indicating if the thread is terminated */
	private boolean								terminate					= false;

	/** policy listener */
	private final PolicyListener				policyListener;

	/** policy dao */
	private final PolicyDao						policyDao;

	/**
	 * Default constructor
	 *
	 * @param policyListener policy listener
	 * @param dao dao
	 */
	public PolicyPoller(
					final PolicyListener policyListener,
					final PolicyDao dao
	)
	{
		this.policyListener = policyListener;
		this.policyDao = dao;
	}

	/**
	 * Constructor with pooling interval
	 *
	 * @param policyListener policy listener
	 * @param dao dao
	 * @param pollingInterval an interval
	 */
	public PolicyPoller(
					final PolicyListener policyListener,
					final PolicyDao dao,
					final Long pollingInterval
	)
	{
		this.policyListener = policyListener;
		this.policyDao = dao;
		this.pollingInterval = pollingInterval;
	}

	@Override
	public void run()
	{
		if (pollingInterval < 0)
		{
			return;
		}
		else if (pollingInterval == 0)
		{
			pollingInterval = DEFAULT_POLLING_INTERVAL;
		}

		while (!terminate)
		{

			for (final TPolicy policyModel : policyDao.list())
			{
				policyListener.onPolicySave(policyModel);
			}

			try
			{
				Thread.sleep(pollingInterval);
			}
			catch (InterruptedException e)
			{
				continue;
			}
		}
	}

	/**
	 * Terminate the thread
	 */
	public void terminate() {
		terminate = true;
	}
}
