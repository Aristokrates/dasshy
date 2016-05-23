package com.kromatik.dasshy.server.event;

/**
 * Job started event
 */
public class PolicyJobStarted extends JobEvent
{

	/***
	 * New job started event
	 *
	 * @param id        event id
	 * @param jobId     job id
	 * @param timestamp creation timestamp
	 */
	public PolicyJobStarted(String id, String jobId, Long timestamp)
	{
		super(id, jobId, timestamp, JobEventType.JOB_STARTED);
	}
}
