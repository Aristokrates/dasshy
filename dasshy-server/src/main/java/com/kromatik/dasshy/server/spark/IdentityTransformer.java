package com.kromatik.dasshy.server.spark;

import com.kromatik.dasshy.sdk.AbstractTransformer;
import com.kromatik.dasshy.sdk.RuntimeContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Map;

/**
 * identity pass through transformer
 */
public class IdentityTransformer extends AbstractTransformer
{
	@Override
	public Map<String, Dataset<Row>> transform(final RuntimeContext context, final Map<String, Dataset<Row>> input)
	{
		return input;
	}
}
