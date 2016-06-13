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
package com.kromatik.dasshy.server.scheduler;

import com.kromatik.dasshy.server.dao.InMemoryPolicyDao;
import com.kromatik.dasshy.server.dao.PolicyDao;
import com.kromatik.dasshy.server.policy.Policy;
import com.kromatik.dasshy.server.spark.PolicyJob;
import com.kromatik.dasshy.thrift.model.TJobState;
import com.kromatik.dasshy.thrift.model.TPolicy;
import org.fest.assertions.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the Job listener
 */
@Test(groups = { "listener" })
public class JobUpdateListenerTest
{
	private PolicyDao policyDao;

	private JobUpdateListener jobUpdateListener;

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
