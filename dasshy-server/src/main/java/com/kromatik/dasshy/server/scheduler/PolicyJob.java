package com.kromatik.dasshy.server.scheduler;

import com.kromatik.dasshy.sdk.StageConfiguration;
import com.kromatik.dasshy.sdk.RuntimeContext;
import com.kromatik.dasshy.server.event.PolicyBatchCancelled;
import com.kromatik.dasshy.server.event.PolicyBatchEnded;
import com.kromatik.dasshy.server.event.PolicyBatchStarted;
import com.kromatik.dasshy.server.event.PolicyJobEnded;
import com.kromatik.dasshy.server.event.PolicyJobStarted;
import com.kromatik.dasshy.server.policy.Policy;
import com.kromatik.dasshy.thrift.model.TJobState;
import org.apache.spark.SparkEnv;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.streaming.StreamingContextState;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.util.UUID;

/**
 * Job created for the given policy instance.
 */
public class PolicyJob extends Job
{
	/** runtime context for execution of the policy job */
	private final RuntimeContext			runtimeContext;

	/** policy instance */
	private final Policy					policy;

	/** spark env */
	private final SparkEnv					sparkEnvironment;

	/** id of the current running batch */
	private String 							currentBatchId;

	/**
	 * Default constructor
	 *
	 * @param policy policy instance
	 * @param runtimeContext runtime context
	 * @param listener listener
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
	protected Object run()
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

			for (i = 0, batchTime = getBatchTime();
				 policy.getClock().acquire();
				 policy.getClock().increment(i), i++, batchTime = getBatchTime())
			{
				final String batchId = getBatchId(batchTime);
				currentBatchId = batchId;

				fireEvent(new PolicyBatchStarted(
								UUID.randomUUID().toString(),
								id, System.currentTimeMillis(),
								currentBatchId));

				// set job group without interruption on cancel
				sparkContext.setJobGroup(batchId, "Job group for Id", false);

				final Dataset<Row> inputDF = policy.getExtractor().stage().next(
								runtimeContext, extractorConfig, batchTime);

				final Dataset<Row> transformedDF = policy.getTransformer().stage()
								.transform(runtimeContext, transformerConfig, inputDF);

				policy.getLoader().stage().load(runtimeContext, loaderConfig, transformedDF);

				// TODO (pai) get the batch information
				fireEvent(new PolicyBatchEnded(UUID.randomUUID().toString(), id, System.currentTimeMillis()));
				evaluateExtractorCheckpoint(currentBatchId, true);

			}
		}
		catch (Exception e)
		{
			fireEvent(new PolicyBatchCancelled(
							UUID.randomUUID().toString(),
							id,
							System.currentTimeMillis(),
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

		return null;
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
	 *
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
