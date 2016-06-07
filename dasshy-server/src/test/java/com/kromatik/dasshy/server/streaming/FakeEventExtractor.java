package com.kromatik.dasshy.server.streaming;

import com.kromatik.dasshy.sdk.AbstractExtractor;
import com.kromatik.dasshy.sdk.RuntimeContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.streaming.Time;

/**
 * Fake extractor. Does nothing
 */
public class FakeEventExtractor extends AbstractExtractor
{
	@Override
	public Dataset<Row> next(RuntimeContext context, Time time)
	{
		return null;
	}

	@Override
	public void commit()
	{

	}

	@Override
	public void rollback()
	{

	}
}
