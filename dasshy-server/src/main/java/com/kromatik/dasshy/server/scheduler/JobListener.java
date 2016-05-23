package com.kromatik.dasshy.server.scheduler;

import com.kromatik.dasshy.server.event.JobEvent;
import com.kromatik.dasshy.thrift.model.TJobState;

/**
 * Job listener on any job changes
 */
public interface JobListener
{
	/**
	 * Reports a job progress update
	 *
	 * @param job job
	 * @param progress new progress
	 */
	void onProgressUpdate(final Job job, int progress);

	/**
	 * Called whenever job state changes
	 *
	 * @param job a job
	 * @param before previous state
	 * @param after new state
	 */
	void onStateChange(final Job job, final TJobState before, TJobState after);

	/**
	 * Called when a new job event in fired
	 *
	 * @param jobEvent job event
	 */
	void onJobEvent(final JobEvent jobEvent);
}
