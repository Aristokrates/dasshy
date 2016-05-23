package com.kromatik.dasshy.sdk;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.streaming.Time;

/**
 * Extract the data from a source.
 * The data needs to be extracted in transactional way
 * to make sure it's processed exactly once
 */
public interface Extractor extends IStage
{
	/**
	 * Calculates the next data set
	 *
	 * @param context runtime context
	 * @param configuration configuration
	 * @param time time
	 *
	 * @return the next, extracted data frame
	 */
	Dataset<Row> next(final RuntimeContext context, final StageConfiguration configuration, final Time time);

	/**
	 * Commits the extracted data
	 */
	void commit();

	/**
	 * Rollback the extracted data
	 */
	void rollback();
}
