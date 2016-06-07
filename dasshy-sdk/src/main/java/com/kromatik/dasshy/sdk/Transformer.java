package com.kromatik.dasshy.sdk;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Transform the input data frame
 */
public interface Transformer extends IStage
{

	/**
	 * Transforms the input data frame
	 *
	 * @param context       runtime context
	 * @param input			input data
	 * @return transformed data
	 */
	Dataset<Row> transform(
					final RuntimeContext context,
					final Dataset<Row> input
	);
}
