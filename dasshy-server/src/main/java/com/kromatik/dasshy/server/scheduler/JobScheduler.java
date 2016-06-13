/**
 * Dasshy - Real time and Batch Analytics Open Source System
 * Copyright (C) 2016 Kromatik Solutions (http://kromatiksolutions.com)
 *
 * This file is part of Dasshy
 *
 * Dasshy is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Dasshy is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Dasshy.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kromatik.dasshy.server.scheduler;

import java.util.Collection;

/**
 * Scheduler for the jobs
 */
public interface JobScheduler
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
