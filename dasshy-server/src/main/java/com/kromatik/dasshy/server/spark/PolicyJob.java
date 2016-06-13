/**
 * Dasshy - Real time and Batch Analytics Open Source System
 * Copyright (C) 2016 Kromatik Solutions (http://kromatiksolutions.com)
 *
 * This file is part of Dasshy
 *
 * Dasshy is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Dasshy is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Dasshy.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kromatik.dasshy.server.spark;

import com.kromatik.dasshy.sdk.StageConfiguration;
import com.kromatik.dasshy.sdk.RuntimeContext;
import com.kromatik.dasshy.server.event.PolicyBatchCancelled;
import com.kromatik.dasshy.server.event.PolicyBatchEnded;
import com.kromatik.dasshy.server.event.PolicyBatchStarted;
import com.kromatik.dasshy.server.event.PolicyJobEnded;
import com.kromatik.dasshy.server.event.PolicyJobStarted;
import com.kromatik.dasshy.server.policy.Policy;
import com.kromatik.dasshy.server.scheduler.Job;
import com.kromatik.dasshy.server.scheduler.JobListener;
import com.kromatik.dasshy.thrift.model.TJobState;
import org.apache.spark.SparkEnv;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.StreamingContextState;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Job created for the given policy instance.
 */
public class PolicyJob extends Job
{

	private static final Integer MAX_RESULTS = 1000;

	/** runtime context for execution of the policy job */
	private final RuntimeContext runtimeContext;

	/** policy instance */
	private final Policy policy;

	/** spark env */
	private final SparkEnv sparkEnvironment;

	/** id of the current running batch */
	private String currentBatchId;

	/**
	 * Default constructor
	 *
	 * @param policy         policy instance
	 * @param runtimeContext runtime context
	 * @param listener       listener
	 */
	public PolicyJob(final Policy policy, final RuntimeContext runtimeContext, final JobListener listener)
	{
		super(policy.getModel().getId(), listener);
		this.policy = policy;
		this.runtimeContext = runtimeContext;
		sparkEnvironment = SparkEnv.get();

		setJobState(TJobState.READY);
	}

	@Override
	protected void run()
	{
		final JavaStreamingContext javaStreamingContext = runtimeContext.getJavaStreamingContext();
		final JavaSparkContext sparkContext = javaStreamingContext.sparkContext();

		try
		{
			SparkEnv.set(sparkEnvironment);

			StageConfiguration extractorConfig = policy.getExtractor().configuration();
			StageConfiguration transformerConfig = policy.getTransformer().configuration();
			StageConfiguration loaderConfig = policy.getLoader().configuration();

			// initialize the extractors; transformers and loaders
			policy.getExtractor().stage().init(runtimeContext, extractorConfig);
			policy.getTransformer().stage().init(runtimeContext, transformerConfig);
			policy.getLoader().stage().init(runtimeContext, loaderConfig);

			fireEvent(new PolicyJobStarted(UUID.randomUUID().toString(), id, System.currentTimeMillis()));

			// start receiving the data
			Time batchTime;
			int i;

			for (i = 0, batchTime = getBatchTime(); policy.getClock().acquire(); policy.getClock()
							.increment(i), i++, batchTime = getBatchTime())
			{
				final String batchId = getBatchId(batchTime);
				currentBatchId = batchId;

				fireEvent(new PolicyBatchStarted(UUID.randomUUID().toString(), id, System.currentTimeMillis(),
								currentBatchId));

				// set job group without interruption on cancel
				sparkContext.setJobGroup(batchId, "Job group for Id", false);

				final Map<String, Dataset<Row>> inputDF = policy.getExtractor().stage()
								.extract(runtimeContext, batchTime);

				final Map<String, Dataset<Row>> transformedDF = policy.getTransformer().stage()
								.transform(runtimeContext, inputDF);

				Dataset<Row> resultDF = policy.getLoader().stage().load(runtimeContext, transformedDF);

				// TODO (pai) get the batch information
				fireEvent(new PolicyBatchEnded(UUID.randomUUID().toString(), id, System.currentTimeMillis()));
				evaluateExtractorCheckpoint(currentBatchId, true);

				// return the result from the resultDF
				final List<Map<String, Object>> result = new ArrayList<>();

				// TODO (pai) specify max results per policy
				final List<Row> rows = resultDF.takeAsList(MAX_RESULTS);

				final StructType schema = resultDF.queryExecution().analyzed().schema();
				for (final Row row : rows)
				{
					final Map<String, Object> rowMap = new HashMap<>();
					for (final String fieldName : schema.fieldNames())
					{
						final Object value = row.get(schema.fieldIndex(fieldName));
						rowMap.put(fieldName, value);
					}
					result.add(rowMap);
				}

				setJobResult(result);
			}
		}
		catch (Exception e)
		{
			fireEvent(new PolicyBatchCancelled(UUID.randomUUID().toString(), id, System.currentTimeMillis(),
							currentBatchId));
			evaluateExtractorCheckpoint(currentBatchId, false);
			throw e;
		}
		finally
		{
			// policy job has ended; fire events and clean the execution stages
			fireEvent(new PolicyJobEnded(UUID.randomUUID().toString(), id, System.currentTimeMillis()));
			policy.getExtractor().stage().clean(runtimeContext);
			policy.getTransformer().stage().clean(runtimeContext);
			policy.getLoader().stage().clean(runtimeContext);
		}
	}

	/**
	 * Checkpoint the batch
	 *
	 * @param batchId current batch id
	 * @param success success flag
	 */
	private void evaluateExtractorCheckpoint(String batchId, boolean success)
	{
		if (batchId == null)
		{
			// no batch exists
			return;
		}

		if (success)
		{
			policy.getExtractor().stage().commit();
		}
		else
		{
			policy.getExtractor().stage().rollback();
		}
	}

	@Override
	protected boolean abort()
	{
		if (currentBatchId != null)
		{
			JavaStreamingContext streamingContext = runtimeContext.getJavaStreamingContext();
			if (streamingContext.getState().equals(StreamingContextState.ACTIVE))
			{
				runtimeContext.getJavaStreamingContext().sparkContext().cancelJobGroup(currentBatchId);
			}
			return true;
		}
		return false;
	}

	/**
	 * Next batch time
	 *
	 * @return streaming batch time
	 */
	private Time getBatchTime()
	{
		final Long start = policy.getClock().getBatchTime();
		return new Time(start);
	}

	/**
	 * Get the id of the batch
	 *
	 * @param batchTime batch time
	 * @return batch Id
	 */
	private String getBatchId(final Time batchTime)
	{
		return "name" + "_" + batchTime.milliseconds();
	}

	/**
	 * Get policy
	 *
	 * @return policy
	 */
	public Policy getPolicy()
	{
		return policy;
	}
}
