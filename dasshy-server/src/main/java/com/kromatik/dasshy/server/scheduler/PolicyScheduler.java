package com.kromatik.dasshy.server.scheduler;

import java.util.Collection;

/**
 * Scheduler for the streaming policy jobs
 */
public interface PolicyScheduler
{
	/**
	 * Gets the running jobs
	 *
	 * @return list of Jobs
	 */
	Collection<Job> getRunningJobs();

	/**
	 * Submit a job to the scheduler
	 *
	 * @param job a job
	 */
	void submit(final Job job);

	/**
	 * Remove a job from the scheduler
	 *
	 * @param jobId id of the job
	 *
	 * @return removed policy job; null if not found
	 */
	Job stop(final String jobId);

	/**
	 * Terminates the scheduler and stops all running jobs
	 */
	void stopAll();

	/**
	 * Get the job by its id
	 *
	 * @param jobId  job id
	 *
	 * @return Job
	 */
	Job get(final String jobId);
}
