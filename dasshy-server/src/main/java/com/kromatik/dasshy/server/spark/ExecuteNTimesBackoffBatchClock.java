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
package com.kromatik.dasshy.server.spark;

/**
 * Implementation of {@link BatchClock} that is called N times and sleeps between sequential executions
 */
public class ExecuteNTimesBackoffBatchClock extends ExecuteNTimesBatchClock
{

	private final Long sleepMs;

	/**
	 * Default constructor
	 */
	public ExecuteNTimesBackoffBatchClock()
	{
		this(1, 1000L);
	}

	/**
	 * With max batches and sleep interval
	 *
	 * @param maxBatches max batches
	 * @param sleepMs    sleep interval between the batches
	 */
	public ExecuteNTimesBackoffBatchClock(Integer maxBatches, Long sleepMs)
	{
		super(maxBatches);
		this.sleepMs = sleepMs;
	}

	@Override
	public void increment(Integer count)
	{
		super.increment(count);

		try
		{
			Thread.sleep(sleepMs);
		}
		catch (InterruptedException e)
		{
			// ignore it
		}
	}
}
