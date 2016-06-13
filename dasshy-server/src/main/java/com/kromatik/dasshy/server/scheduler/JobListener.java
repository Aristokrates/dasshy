/**
 * Dasshy - Real time Streaming and Batch Analytics Open Source System
 * Copyright (C) 2016 Kromatik Solutions
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

	/**
	 * Called when a new job result is available
	 *
	 * @param job a job
	 * @param jobResult job result
	 */
	void onJobResult(final Job job, final Object jobResult);
}
