package com.kromatik.dasshy.server.streaming;

import com.kromatik.dasshy.sdk.AbstractLoader;
import com.kromatik.dasshy.sdk.RuntimeContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Fake loader. Does nothing
 */
public class FakeLoader extends AbstractLoader
{
	@Override
	public void load(RuntimeContext context, Dataset<Row> input)
	{

	}
}
