package com.kromatik.dasshy.server.event;

/**
 * Job ended event
 */
public class PolicyJobEnded extends JobEvent
{

	/***
	 * New job ended event
	 *
	 * @param id        event id
	 * @param jobId     job id
	 * @param timestamp creation timestamp
	 */
	public PolicyJobEnded(String id, String jobId, Long timestamp)
	{
		super(id, jobId, timestamp, JobEventType.JOB_ENDED);
	}
}
