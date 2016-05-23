package com.kromatik.dasshy.server.event;

/**
 * Abstract job event representing common job events fired during job life-cycle
 */
public abstract class JobEvent
{
	/** event id */
	private String			id;

	/** its corresponding job id */
	private String			jobId;

	/** event timestamp */
	private	Long			timestamp;

	/** event type */
	private JobEventType	type;

	/***
	 * New job event
	 *
	 * @param id event id
	 * @param jobId job id
	 * @param timestamp creation timestamp
	 * @param type event type
	 */
	public JobEvent(String id, String jobId, Long timestamp, JobEventType type)
	{
		this.id = id;
		this.jobId = jobId;
		this.timestamp = timestamp;
		this.type = type;
	}

	public String getId()
	{
		return id;
	}

	public String getJobId()
	{
		return jobId;
	}

	public Long getTimestamp()
	{
		return timestamp;
	}

	public JobEventType getType()
	{
		return type;
	}
}
