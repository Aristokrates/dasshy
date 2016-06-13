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

import com.kromatik.dasshy.server.event.JobEvent;
import com.kromatik.dasshy.thrift.model.TJobState;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Abstract job
 */
public abstract class Job
{
	/** job id */
	protected String id;

	/** state of the job */
	protected TJobState jobState;

	/** start time of this job */
	protected Long startTime;

	/** end time of this job */
	protected Long endTime;

	/** latest job result */
	protected Object jobResult;

	/** error */
	protected String errorMessage;

	/** exception */
	protected Throwable exception;

	/** listener that listens on job changes */
	protected JobListener listener;

	/** whether job is aborted */
	protected boolean aborted = false;

	/**
	 * Default constructor
	 *
	 * @param jobId       job id
	 * @param jobListener listener
	 */
	public Job(final String jobId, final JobListener jobListener)
	{
		this.id = jobId;
		this.listener = jobListener;
	}

	/**
	 * Starts the job. This is the entry point of the job
	 */
	public void start()
	{
		try
		{
			startTime = System.currentTimeMillis();
			run();
			endTime = System.currentTimeMillis();
		}
		catch (Exception throwable)
		{
			endTime = System.currentTimeMillis();
			this.exception = throwable;
			jobResult = throwable.getMessage();
			errorMessage = getStackTrace(throwable);
			setJobState(TJobState.ERROR);
		}
	}

	/**
	 * Stops the job. This is the exit point of the job
	 */
	public void stop()
	{
		aborted = abort();
	}

	/**
	 * Run the job and get a result of its execution
	 */
	protected abstract void run();

	/**
	 * Abort the job
	 *
	 * @return status of the abort
	 */
	protected abstract boolean abort();

	/**
	 * Get stack trace as String
	 *
	 * @param e exception
	 * @return error message
	 */
	public static String getStackTrace(final Throwable e)
	{
		if (e == null)
		{
			return "";
		}

		Throwable cause = ExceptionUtils.getRootCause(e);
		return ExceptionUtils.getFullStackTrace(cause != null ? cause : e);
	}

	/**
	 * Fires an event
	 *
	 * @param event job event
	 */
	protected void fireEvent(final JobEvent event)
	{
		if (listener != null)
		{
			listener.onJobEvent(event);
		}
	}

	/**
	 * @param jobState new job state
	 */
	public void setJobState(final TJobState jobState)
	{
		if (this.jobState == jobState)
		{
			return;
		}

		this.jobState = jobState;
		if (listener != null)
		{
			listener.onStateChange(this, this.jobState, jobState);
		}
	}

	/**
	 * @param jobResult job result
	 */
	public void setJobResult(final Object jobResult)
	{
		this.jobResult = jobResult;
		if (listener != null)
		{
			listener.onJobResult(this, jobResult);
		}
	}

	/**
	 * @return job state
	 */
	public TJobState getJobState()
	{
		return jobState;
	}

	/**
	 * @return job listener
	 */
	public JobListener getListener()
	{
		return listener;
	}

	/**
	 * @param listener listener
	 */
	public void setListener(final JobListener listener)
	{
		this.listener = listener;
	}

	/**
	 * @return job's start time
	 */
	public Long getStartTime()
	{
		return startTime;
	}

	/**
	 * @return job's end time
	 */
	public Long getEndTime()
	{
		return endTime;
	}

	/**
	 * @return last job result
	 */
	public Object getJobResult()
	{
		return jobResult;
	}

	/**
	 * @return error message, if any
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	/**
	 * @return exception, if any
	 */
	public Throwable getException()
	{
		return exception;
	}

	/**
	 * @return true if the job is aborted, false otherwise
	 */
	public boolean isAborted()
	{
		return aborted;
	}
}
