package com.kromatik.dasshy.server.event;

/**
 * Batch cancelled event
 */
public class PolicyBatchCancelled extends JobEvent
{

	private final String		batchId;

	/***
	 * New batch cancelled event
	 *
	 * @param id        event id
	 * @param jobId     job id
	 * @param timestamp creation timestamp
	 * @param batchId	batch id
	 */
	public PolicyBatchCancelled(String id, String jobId, Long timestamp, String batchId)
	{
		super(id, jobId, timestamp, JobEventType.BATCH_CANCELLED);
		this.batchId = batchId;
	}

	public String getBatchId()
	{
		return batchId;
	}
}
