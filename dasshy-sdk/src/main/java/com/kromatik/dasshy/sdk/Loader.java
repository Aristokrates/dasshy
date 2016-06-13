package com.kromatik.dasshy.sdk;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Map;

/**
 * Load the data frame
 */
public interface Loader extends IStage
{

	/**
	 * Load the input data and return a result.
	 * This includes querying the input data frame and returning final result.
	 * Optionally, the output data can be loaded into a persistent storage
	 *
	 * @param context runtime context
	 * @param input input data frames
	 *
	 * @return the result data
	 */
	Dataset<Row> load(final RuntimeContext context, final Map<String, Dataset<Row>> input);
}
