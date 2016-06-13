package com.kromatik.dasshy.server.streaming;

import java.util.Calendar;

/**
 * Batch clock for managing the streaming processing
 */
public abstract class StreamingClock
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
	 * Acquires a permit to continue with the next batch processing
	 *
	 * @return true, if permit is granted; false otherwise
	 */
	public abstract boolean acquire();

	/**
	 * Increment the batch and advance to the next batch
	 *
	 * @param count batches processed so far
	 */
	public abstract void increment(final Integer count);

	/**
	 * Gets the batch time
	 *
	 * @return next batch time
	 */
	public abstract Long getBatchTime();
}
