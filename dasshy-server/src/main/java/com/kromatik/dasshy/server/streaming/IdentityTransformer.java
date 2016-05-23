package com.kromatik.dasshy.server.streaming;

import com.kromatik.dasshy.sdk.AbstractTransformer;
import com.kromatik.dasshy.sdk.StageConfiguration;
import com.kromatik.dasshy.sdk.RuntimeContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * identity pass through transformer
 */
public class IdentityTransformer extends AbstractTransformer
{
	@Override
	public Dataset<Row> transform(
					final RuntimeContext context,
					final StageConfiguration configuration,
					final Dataset<Row> input
	)
	{
		return input;
	}
}
