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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default batch clock
 */
public class StreamingIntervalBatchClock extends BatchClock
{
	private static final Logger LOGGER	=	LoggerFactory.getLogger(StreamingIntervalBatchClock.class);

	private final Long intervalSeconds;

	/** batch time*/
	private Long batchTime;

	/**
	 * Default constructor
	 *
	 * @param intervalSeconds batch interval in seconds
	 */
	public StreamingIntervalBatchClock(final Long intervalSeconds)
	{
		this.intervalSeconds = intervalSeconds;
	}

	@Override
	public boolean acquire()
	{
		// loop indefinitely
		return true;
	}

	@Override
	public void increment(final Integer count)
	{
		final Long sleepTimeMillis = Math.max(getBatchTime() + intervalSeconds * 1000 - System.currentTimeMillis(), 0);
		try
		{
			Thread.sleep(sleepTimeMillis);
		}
		catch (InterruptedException e)
		{
			LOGGER.warn("Thread is interrupted while Spark Streaming Engine is running");
		}

		// calculate extract batch time
		calculateBatchTime();
	}

	@Override
	public Long getBatchTime()
	{
		if (batchTime == null)
		{
			calculateBatchTime();
		}
		return batchTime;
	}

	/**
	 * Calculates the batch time based on the processing interval
	 */
	private void calculateBatchTime()
	{
		final Long nowMs = System.currentTimeMillis();
		final Long millisSinceEpoch = nowMs - EPOCH_TIME_MS;
		final long periodNumber = millisSinceEpoch / (intervalSeconds * 1000);
		batchTime = EPOCH_TIME_MS + periodNumber * intervalSeconds * 1000;
	}
}
