package com.kromatik.dasshy.server.spark;

import java.util.Calendar;

/**
 * Batch clock for managing the streaming processing
 */
public abstract class BatchClock
{
	/**
	 * epoch time in miliseconds
	 */
	protected static final Long EPOCH_TIME_MS;

	static
	{
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(0);
		EPOCH_TIME_MS = c.getTimeInMillis();
	}

	/**
	 * Acquires a permit to continue with the extract batch processing
	 *
	 * @return true, if permit is granted; false otherwise
	 */
	public abstract boolean acquire();

	/**
	 * Increment the batch and advance to the extract batch
	 *
	 * @param count batches processed so far
	 */
	public abstract void increment(final Integer count);

	/**
	 * Gets the batch time
	 *
	 * @return extract batch time
	 */
	public abstract Long getBatchTime();
}
