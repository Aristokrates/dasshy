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
package com.kromatik.dasshy.server.spark;

/**
 * Implementation of {@link BatchClock} that is called N times
 */
public class ExecuteNTimesBatchClock extends BatchClock
{
	/**maximum allowed batches*/
	private final Integer		maximumBatches;

	/**current batch*/
	private Integer				currentBatchNumber;

	/**
	 * Default constructor
	 */
	public ExecuteNTimesBatchClock()
	{
		this(1);
	}

	/**
	 * With max number of batches
	 *
	 * @param maxBatches max batches
	 */
	public ExecuteNTimesBatchClock(final Integer maxBatches)
	{
		currentBatchNumber = 0;
		maximumBatches = maxBatches;
	}

	@Override
	public boolean acquire()
	{
		synchronized (this)
		{
			return currentBatchNumber < maximumBatches;
		}
	}

	@Override
	public void increment(Integer count)
	{
		synchronized (this)
		{
			currentBatchNumber++;
		}
	}

	@Override
	public Long getBatchTime()
	{
		synchronized (this)
		{
			return System.currentTimeMillis();
		}
	}
}
