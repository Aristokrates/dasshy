package com.kromatik.dasshy.sdk;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Map;

/**
 * Transform the input data frame
 */
public interface Transformer extends IStage
{

	/**
	 * Transforms the input data frame
	 *
	 * @param context		runtime context
	 * @param input			input data
	 * @return				transformed data
	 */
	Map<String, Dataset<Row>> transform(final RuntimeContext context, final Map<String, Dataset<Row>> input);
}
