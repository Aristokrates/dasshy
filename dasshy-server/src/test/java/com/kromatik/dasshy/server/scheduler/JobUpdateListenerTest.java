package com.kromatik.dasshy.server.scheduler;

import com.kromatik.dasshy.server.dao.InMemoryPolicyDao;
import com.kromatik.dasshy.server.dao.PolicyDao;
import com.kromatik.dasshy.server.policy.Policy;
import com.kromatik.dasshy.server.streaming.PolicyJob;
import com.kromatik.dasshy.thrift.model.TJobState;
import com.kromatik.dasshy.thrift.model.TPolicy;
import org.fest.assertions.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the Job listener
 */
@Test(groups = {"listener"})
public class JobUpdateListenerTest
{
	private PolicyDao			policyDao;

	private JobUpdateListener	jobUpdateListener;

	@BeforeMethod
	public void setup() throws Exception
	{
		policyDao = new InMemoryPolicyDao();
		jobUpdateListener = new JobUpdateListener(policyDao);
	}

	public void stateChanged() throws Exception
	{

		String policyId = "id";
		TPolicy policyModel = new TPolicy();
		policyModel.setId(policyId);

		Policy policyInstance = new Policy();
		policyInstance.setModel(policyModel);

		policyDao.create(policyModel);

		Job policyJob = new PolicyJob(policyInstance, null, null);
		policyJob.startTime = System.currentTimeMillis();
		policyJob.endTime = policyJob.startTime + (10 * 1000);
		policyJob.errorMessage = null;

		jobUpdateListener.onStateChange(policyJob, TJobState.RUNNING, TJobState.FINISHED);

		TPolicy updatedPolicyModel = policyDao.get(policyId);
		Assertions.assertThat(updatedPolicyModel.getStartTime()).isEqualTo(policyJob.startTime);
		Assertions.assertThat(updatedPolicyModel.getEndTime()).isEqualTo(policyJob.endTime);
		Assertions.assertThat(updatedPolicyModel.getError()).isEqualTo(policyJob.errorMessage);
		Assertions.assertThat(updatedPolicyModel.getState()).isEqualTo(TJobState.FINISHED);
	}
}
