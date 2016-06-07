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
			protected Object run()
			{
				return null;
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
			protected Object run()
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
			protected Object run()
			{
				return null;
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
