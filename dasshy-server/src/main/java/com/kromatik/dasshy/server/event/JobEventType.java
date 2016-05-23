package com.kromatik.dasshy.server.event;

/**
 * Type of job event
 */
public enum JobEventType
{
	JOB_STARTED,
	JOB_ENDED,
	JOB_ABORTED,
	JOB_PAUSED,

	BATCH_STARTED,
	BATCH_ENDED,
	BATCH_CANCELLED
}
