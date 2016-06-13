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
package com.kromatik.dasshy.server.spark;

import java.util.Calendar;

/**
 * Batch clock for managing the streaming processing
 */
public abstract class BatchClock
{
	/**
	 * epoch time in miliseconds
	 */
	protected static final Long EPOCH_TIME_MS;

	static
	{
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(0);
		EPOCH_TIME_MS = c.getTimeInMillis();
	}

	/**
	 * Acquires a permit to continue with the extract batch processing
	 *
	 * @return true, if permit is granted; false otherwise
	 */
	public abstract boolean acquire();

	/**
	 * Increment the batch and advance to the extract batch
	 *
	 * @param count batches processed so far
	 */
	public abstract void increment(final Integer count);

	/**
	 * Gets the batch time
	 *
	 * @return extract batch time
	 */
	public abstract Long getBatchTime();
}
