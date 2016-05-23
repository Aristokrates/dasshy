package com.kromatik.dasshy.sdk;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Load the data frame
 */
public interface Loader extends IStage
{
	/**
	 * Load the input data into an output destination
	 *
	 * @param context context
	 * @param configuration stage configuration
	 * @param input input data
	 */
	void load(final RuntimeContext context, final StageConfiguration configuration, final Dataset<Row> input);
}
