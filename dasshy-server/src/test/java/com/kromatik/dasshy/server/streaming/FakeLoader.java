package com.kromatik.dasshy.server.streaming;

import com.kromatik.dasshy.sdk.AbstractLoader;
import com.kromatik.dasshy.sdk.RuntimeContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Map;

/**
 * Fake loader. Does nothing
 */
public class FakeLoader extends AbstractLoader
{
	@Override
	public Dataset<Row> load(RuntimeContext context, Map<String, Dataset<Row>> input)
	{
		return context.getSparkSession().emptyDataFrame();
	}
}
