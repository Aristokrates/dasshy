package com.kromatik.dasshy.server.streaming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default batch clock
 */
public class DefaultBatchClock extends BatchClock
{
	private static final Logger LOGGER	=	LoggerFactory.getLogger(DefaultBatchClock.class);

	private final Long interval;

	/** batch time*/
	private Long batchTime;

	/**
	 * Default constructor
	 *
	 * @param interval batch interval
	 */
	public DefaultBatchClock(Long interval)
	{
		this.interval = interval;
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
		final Long sleepTimeMillis = Math.max(getBatchTime() + interval * 1000 - System.currentTimeMillis(), 0);
		try
		{
			Thread.sleep(sleepTimeMillis);
		}
		catch (InterruptedException e)
		{
			LOGGER.warn("Thread is interrupted while Spark Streaming Engine is running");
		}

		// calculate next batch time
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
		final long periodNumber = millisSinceEpoch / interval;
		batchTime = EPOCH_TIME_MS + periodNumber * interval;
	}
}
