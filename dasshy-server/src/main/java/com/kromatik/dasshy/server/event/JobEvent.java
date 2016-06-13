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
package com.kromatik.dasshy.server.event;

/**
 * Abstract job event representing common job events fired during job life-cycle
 */
public abstract class JobEvent
{
	/** event id */
	private String			id;

	/** its corresponding job id */
	private String			jobId;

	/** event timestamp */
	private	Long			timestamp;

	/** event type */
	private JobEventType	type;

	/***
	 * New job event
	 *
	 * @param id event id
	 * @param jobId job id
	 * @param timestamp creation timestamp
	 * @param type event type
	 */
	public JobEvent(String id, String jobId, Long timestamp, JobEventType type)
	{
		this.id = id;
		this.jobId = jobId;
		this.timestamp = timestamp;
		this.type = type;
	}

	public String getId()
	{
		return id;
	}

	public String getJobId()
	{
		return jobId;
	}

	public Long getTimestamp()
	{
		return timestamp;
	}

	public JobEventType getType()
	{
		return type;
	}
}
