package com.kromatik.dasshy.server.event;

/**
 * Job paused temporary
 */
public class PolicyJobPaused extends JobEvent
{

	/***
	 * New job paused event
	 *
	 * @param id        event id
	 * @param jobId     job id
	 * @param timestamp creation timestamp
	 */
	public PolicyJobPaused(String id, String jobId, Long timestamp)
	{
		super(id, jobId, timestamp, JobEventType.JOB_PAUSED);
	}
}
