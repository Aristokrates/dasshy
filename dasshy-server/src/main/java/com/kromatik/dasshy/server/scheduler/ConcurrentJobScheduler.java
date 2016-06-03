package com.kromatik.dasshy.server.scheduler;

import com.kromatik.dasshy.thrift.model.TJobState;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Runs the jobs concurrently
 */
public class ConcurrentJobScheduler implements JobScheduler
{

	/** underlying executor service used to run the jobs */
	private final ExecutorService				executorService;

	/** job map */
	private Map<String, Tuple2<Job, Future<?>>>	jobsMap	=	new ConcurrentHashMap<>();

	/**
	 * Default constructor
	 *
	 * @param service executor service to run the jobs
	 */
	public ConcurrentJobScheduler(final ExecutorService service)
	{
		executorService = service;
	}

	@Override
	public Collection<Job> getRunningJobs()
	{
		final Collection<Job> jobs = new ArrayList<>();
		for (final Tuple2<Job, Future<?>> jobTuple : jobsMap.values())
		{
			jobs.add(jobTuple._1());
		}
		return jobs;
	}

	@Override
	public void submit(final Job job)
	{
		final Tuple2<Job, Future<?>> jobTuple = jobsMap.get(job.id);
		if (jobTuple == null)
		{
			job.setJobState(TJobState.PENDING);
			jobsMap.put(job.id, new Tuple2<Job, Future<?>>(job, executorService.submit(new JobRunner(job))));
		}
	}

	@Override
	public Job stop(final String jobId)
	{
		final Tuple2<Job, Future<?>> jobTuple = jobsMap.get(jobId);
		if (jobTuple != null)
		{
			final Job job = jobTuple._1();
			final Future<?> result = jobTuple._2();

			job.stop();
			result.cancel(true);

			jobsMap.remove(jobId);
			return job;
		}
		return null;
	}

	@Override
	public void stopAll()
	{
		for (final String jobId : jobsMap.keySet())
		{
			stop(jobId);
		}
	}

	@Override
	public Job get(final String jobId)
	{
		final Tuple2<Job, Future<?>> jobTuple = jobsMap.get(jobId);
		return jobTuple != null ? jobTuple._1() : null;
	}

	/**
	 * Runner for jobs
	 */
	private static class JobRunner implements Runnable
	{
		/** job */
		private final Job				job;

		/**
		 * Default constructor
		 *
		 * @param job a job
		 */
		public JobRunner(final Job job)
		{
			this.job = job;
		}

		@Override
		public void run()
		{

			if (job.aborted)
			{
				job.setJobState(TJobState.ABORT);
				job.aborted = false;
			}

			job.setJobState(TJobState.RUNNING);
			job.start();

			if (job.aborted)
			{
				job.setJobState(TJobState.ABORT);
			}
			else
			{
				if (job.exception != null)
				{
					job.setJobState(TJobState.ERROR);
				}
				else
				{
					job.setJobState(TJobState.FINISHED);
				}
			}
			job.aborted = false;
		}
	}
}
