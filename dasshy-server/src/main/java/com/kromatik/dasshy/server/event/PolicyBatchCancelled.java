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
 * Batch cancelled event
 */
public class PolicyBatchCancelled extends JobEvent
{

	private final String		batchId;

	/***
	 * New batch cancelled event
	 *
	 * @param id        event id
	 * @param jobId     job id
	 * @param timestamp creation timestamp
	 * @param batchId	batch id
	 */
	public PolicyBatchCancelled(String id, String jobId, Long timestamp, String batchId)
	{
		super(id, jobId, timestamp, JobEventType.BATCH_CANCELLED);
		this.batchId = batchId;
	}

	public String getBatchId()
	{
		return batchId;
	}
}
