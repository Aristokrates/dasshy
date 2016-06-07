package com.kromatik.dasshy.server.streaming;

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
