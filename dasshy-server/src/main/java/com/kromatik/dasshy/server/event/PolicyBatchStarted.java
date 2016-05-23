package com.kromatik.dasshy.server.event;

/**
 * Policy batch started event
 */
public class PolicyBatchStarted extends JobEvent
{
	/** batch id */
	private final String		batchId;

	/***
	 * New batch started event
	 *
	 * @param id        event id
	 * @param jobId     job id
	 * @param timestamp creation timestamp
	 */
	public PolicyBatchStarted(final String id, final String jobId, final Long timestamp, final String batchId)
	{
		super(id, jobId, timestamp, JobEventType.BATCH_STARTED);
		this.batchId = batchId;
	}

	public String getBatchId()
	{
		return batchId;
	}
}
