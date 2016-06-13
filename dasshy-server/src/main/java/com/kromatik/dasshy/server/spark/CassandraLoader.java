package com.kromatik.dasshy.server.spark;

import com.kromatik.dasshy.sdk.AbstractLoader;
import com.kromatik.dasshy.sdk.RuntimeContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Map;

/**
 * Cassandra Loader
 */
public class CassandraLoader extends AbstractLoader
{
	@Override
	public Dataset<Row> load(RuntimeContext context, Map<String, Dataset<Row>> input)
	{
	// load the data into cassandra tables

		// get from the configuration the database name to load the data in; if no such columns exists, create them
		// insert the data in the database table

		return context.getSparkSession().emptyDataFrame();
	}
}
