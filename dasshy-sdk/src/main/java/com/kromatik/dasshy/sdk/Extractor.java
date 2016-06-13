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
	 * @param time time, valid in case of a streaming job
	 *
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
