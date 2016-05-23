package com.kromatik.dasshy.server.event;

/**
 * Job aborted
 */
public class PolicyJobAborted extends JobEvent
{

	/***
	 * New job aborted event
	 *
	 * @param id        event id
	 * @param jobId     job id
	 * @param timestamp creation timestamp
	 */
	public PolicyJobAborted(String id, String jobId, Long timestamp)
	{
		super(id, jobId, timestamp, JobEventType.JOB_ABORTED);
	}
}
