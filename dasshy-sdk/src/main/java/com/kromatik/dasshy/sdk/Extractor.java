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
package com.kromatik.dasshy.sdk;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.streaming.Time;

import java.util.Map;

/**
 * Extract the data from a source.
 *
 * The data needs to be extracted in transactional way
 * to make sure it's processed exactly once in case of a streaming policy
 */
public interface Extractor extends IStage
{
	/**
	 * Calculates the extracted data set
	 *
	 * @param context runtime context
	 * @param time    time, valid in case of a streaming job
	 * @return extracted data frames by name
	 */
	Map<String, Dataset<Row>> extract(final RuntimeContext context, final Time time);

	/**
	 * Commits the extracted data in case of a streaming job
	 */
	void commit();

	/**
	 * Rollback the extracted data in case of a streaming job
	 */
	void rollback();
}
