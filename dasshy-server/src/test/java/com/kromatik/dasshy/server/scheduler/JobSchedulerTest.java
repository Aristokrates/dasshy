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

import com.kromatik.dasshy.thrift.model.TJobState;
import jersey.repackaged.com.google.common.util.concurrent.MoreExecutors;
import org.fest.assertions.api.Assertions;
import org.testng.annotations.Test;

import java.util.Collection;

/**
 * Tests for job scheduler
 */
@Test(groups = {"scheduler"})
public class JobSchedulerTest
{

	public void jobSubmitFinished() throws Exception
	{
		JobScheduler jobScheduler = new ConcurrentJobScheduler(MoreExecutors.newDirectExecutorService());

		String jobId = "id_finished";
		Job job = new Job(jobId, null)
		{
			@Override
			protected void run()
			{
				return;
			}

			@Override
			protected boolean abort()
			{
				return false;
			}
		};

		jobScheduler.submit(job);

		Job runningJob = jobScheduler.get(job.id);
		Assertions.assertThat(runningJob).isNotNull().isEqualTo(job);

		Collection<Job> runningJobs = jobScheduler.getRunningJobs();
		Assertions.assertThat(runningJobs).containsOnly(runningJob);

		Assertions.assertThat(runningJob.getJobState()).isEqualTo(TJobState.FINISHED);
		Assertions.assertThat(runningJob.aborted).isFalse();

	}

	public void jobSubmitError() throws Exception
	{
		JobScheduler jobScheduler = new ConcurrentJobScheduler(MoreExecutors.newDirectExecutorService());

		String jobId = "id_error";
		Job job = new Job(jobId, null)
		{
			@Override
			protected void run()
			{
				throw new RuntimeException("Job error out");
			}

			@Override
			protected boolean abort()
			{
				return false;
			}
		};

		jobScheduler.submit(job);

		Job runningJob = jobScheduler.get(job.id);
		Assertions.assertThat(runningJob).isNotNull().isEqualTo(job);

		Collection<Job> runningJobs = jobScheduler.getRunningJobs();
		Assertions.assertThat(runningJobs).containsOnly(runningJob);

		Assertions.assertThat(runningJob.getJobState()).isEqualTo(TJobState.ERROR);
		Assertions.assertThat(runningJob.aborted).isFalse();

	}

	public void jobAbort() throws Exception
	{
		JobScheduler jobScheduler = new ConcurrentJobScheduler(MoreExecutors.newDirectExecutorService());

		String jobId = "id_abort";
		Job job = new Job(jobId, null)
		{
			@Override
			protected void run()
			{
				return;
			}

			@Override
			protected boolean abort()
			{
				return true;
			}
		};

		jobScheduler.submit(job);

		Job runningJob = jobScheduler.get(job.id);
		Assertions.assertThat(runningJob).isNotNull().isEqualTo(job);

		Job stoppedJob = jobScheduler.stop(jobId);
		Assertions.assertThat(stoppedJob.aborted).isTrue();

		Collection<Job> runningJobs = jobScheduler.getRunningJobs();
		Assertions.assertThat(runningJobs).doesNotContain(stoppedJob);
	}
}
