package com.kromatik.dasshy.server.spark;

/**
 * Implementation of {@link BatchClock} that is called N times and sleeps between sequential executions
 */
public class ExecuteNTimesBackoffBatchClock extends ExecuteNTimesBatchClock
{

	private final Long	sleepMs;

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
	 * @param sleepMs sleep interval between the batches
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
