package com.kromatik.dasshy.server.streaming;

import com.kromatik.dasshy.sdk.AbstractLoader;
import com.kromatik.dasshy.sdk.RuntimeContext;
import com.kromatik.dasshy.server.config.DasshyConfiguration;
import com.kromatik.dasshy.server.config.DasshyProperties;
import com.kromatik.dasshy.server.config.SparkConfiguration;
import com.kromatik.dasshy.server.dao.InMemoryPolicyDao;
import com.kromatik.dasshy.server.dao.PolicyDao;
import com.kromatik.dasshy.server.policy.ExtractorHolder;
import com.kromatik.dasshy.server.policy.LoaderHolder;
import com.kromatik.dasshy.server.policy.Policy;
import com.kromatik.dasshy.server.policy.TransformerHolder;
import com.kromatik.dasshy.server.scheduler.JobListener;
import com.kromatik.dasshy.server.scheduler.JobUpdateListener;
import com.kromatik.dasshy.server.spark.DasshySparkContextFactory;
import com.kromatik.dasshy.server.spark.ExecuteNTimesBatchClock;
import com.kromatik.dasshy.server.spark.IdentityTransformer;
import com.kromatik.dasshy.server.spark.PolicyJob;
import com.kromatik.dasshy.server.spark.SparkContextFactory;
import com.kromatik.dasshy.thrift.model.TJobState;
import com.kromatik.dasshy.thrift.model.TPolicy;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.fest.assertions.api.Assertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test for policy job streaming
 */
@Test(groups = {"streaming"})
public class PolicyJobTest
{

	private RuntimeContext	runtimeContext;

	private PolicyDao		policyDao;

	@BeforeClass
	public void setup() throws Exception
	{
		policyDao = new InMemoryPolicyDao();

		SparkConfiguration sparkConfiguration = new SparkConfiguration(new DasshyConfiguration());
		sparkConfiguration.setSparkProperty(DasshyProperties.SPARK_MASTER.getPropertyName(), "local[2]");

		SparkContextFactory factory = new DasshySparkContextFactory(sparkConfiguration);

		final SparkSession sparkSession = factory.createSparkSession();
		final JavaStreamingContext streamingContext = factory.createStreamingContext(sparkSession);

		runtimeContext = new RuntimeContext(streamingContext, sparkSession);
	}

	public void runPolicyJobSuccess() throws Exception
	{
		String policyId = "id_success";
		TPolicy policyModel = new TPolicy();
		policyModel.setId(policyId);
		policyDao.create(policyModel);

		Policy policyInstance = new Policy();
		policyInstance.setModel(policyModel);
		policyInstance.setClock(new ExecuteNTimesBatchClock(2));

		policyInstance.setExtractor(new ExtractorHolder(new FakeEventExtractor(), null));
		policyInstance.setTransformer(new TransformerHolder(new IdentityTransformer(), null));
		policyInstance.setLoader(new LoaderHolder(new FakeLoader(), null));

		JobListener jobListener = new JobUpdateListener(policyDao);

		PolicyJob job = new PolicyJob(policyInstance, runtimeContext, jobListener);

		// verify that the job state is READY to run
		Assertions.assertThat(job.getJobState()).isEqualTo(TJobState.READY);

		// start the job
		job.start();

		// check that the job has finished successfully
		Assertions.assertThat(job.getStartTime()).isNotNull().isLessThanOrEqualTo(System.currentTimeMillis());
		Assertions.assertThat(job.getEndTime()).isNotNull().isLessThanOrEqualTo(System.currentTimeMillis());
		Assertions.assertThat(job.getErrorMessage()).isNullOrEmpty();
		Assertions.assertThat(job.getException()).isNull();
		Assertions.assertThat(job.getJobResult()).isNotNull().isInstanceOf(List.class);

		// stop the job
		job.stop();
		Assertions.assertThat(job.isAborted()).isTrue();
	}

	public void runPolicyJobFails() throws Exception
	{
		String policyId = "id_fails";
		TPolicy policyModel = new TPolicy();
		policyModel.setId(policyId);
		policyDao.create(policyModel);

		Policy policyInstance = new Policy();
		policyInstance.setModel(policyModel);
		policyInstance.setClock(new ExecuteNTimesBatchClock(2));

		policyInstance.setExtractor(new ExtractorHolder(new FakeEventExtractor(), null));
		policyInstance.setTransformer(new TransformerHolder(new IdentityTransformer(), null));

		final String exceptionMessage = "Exception on saving the data";
		// deliberately throw an exception on data loading
		policyInstance.setLoader(new LoaderHolder(new AbstractLoader()
		{
			@Override
			public Dataset<Row> load(RuntimeContext context, Map<String, Dataset<Row>> input)
			{
				throw new RuntimeException(exceptionMessage);
			}
		}, null));

		JobListener jobListener = new JobUpdateListener(policyDao);

		PolicyJob job = new PolicyJob(policyInstance, runtimeContext, jobListener);

		// verify that the job state is READY to run
		Assertions.assertThat(job.getJobState()).isEqualTo(TJobState.READY);

		// start the job
		job.start();

		// check that the job has handled the exception correctly
		Assertions.assertThat(job.getStartTime()).isNotNull().isLessThanOrEqualTo(System.currentTimeMillis());
		Assertions.assertThat(job.getEndTime()).isNotNull().isLessThanOrEqualTo(System.currentTimeMillis());
		Assertions.assertThat(job.getErrorMessage()).isNotNull().contains(exceptionMessage);
		Assertions.assertThat(job.getException()).isNotNull().isInstanceOf(RuntimeException.class);
		Assertions.assertThat(job.getJobState()).isEqualTo(TJobState.ERROR);

		// check that the error has been saved correctly
		TPolicy updatedPolicy = policyDao.get(policyId);
		Assertions.assertThat(updatedPolicy.getEndTime()).isEqualTo(job.getEndTime());
		Assertions.assertThat(updatedPolicy.getStartTime()).isEqualTo(job.getStartTime());
		Assertions.assertThat(updatedPolicy.getState()).isEqualTo(job.getJobState());
		Assertions.assertThat(updatedPolicy.getError()).contains(exceptionMessage);
	}

	@AfterClass
	public void destroy()
	{
		final JavaStreamingContext javaStreamingContext = runtimeContext.getJavaStreamingContext();
		javaStreamingContext.ssc().scheduler().stop(true);
		javaStreamingContext.stop(true, true);
	}
}
