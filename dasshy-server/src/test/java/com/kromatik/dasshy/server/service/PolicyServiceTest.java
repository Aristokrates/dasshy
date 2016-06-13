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
package com.kromatik.dasshy.server.service;

import com.kromatik.dasshy.server.dao.PolicyDao;
import com.kromatik.dasshy.server.exception.InvalidPolicyException;
import com.kromatik.dasshy.server.exception.PolicyNotFoundException;
import com.kromatik.dasshy.server.policy.Policy;
import com.kromatik.dasshy.server.policy.PolicyFactory;
import com.kromatik.dasshy.server.policy.PolicyListener;
import com.kromatik.dasshy.thrift.model.TPolicy;
import org.fest.assertions.api.Assertions;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for policy service
 */
@Test(groups = { "service" })
public class PolicyServiceTest
{

	private PolicyDao policyDao;

	private PolicyListener policyListener;

	private PolicyFactory policyFactory;

	private PolicyService policyService;

	@BeforeMethod
	public void setup() throws Exception
	{
		policyDao = Mockito.mock(PolicyDao.class);
		policyListener = Mockito.mock(PolicyListener.class);
		policyFactory = Mockito.mock(PolicyFactory.class);

		policyService = new PolicyService(policyDao, policyListener, policyFactory);
	}

	public void createPolicy() throws Exception
	{
		TPolicy policyModel = new TPolicy();
		policyModel.setId("create_id");

		Policy policyInstance = new Policy();
		policyInstance.setModel(policyModel);

		Mockito.when(policyFactory.buildPolicy(policyModel)).thenReturn(policyInstance);
		policyService.createPolicy(policyModel);
	}

	public void createPolicyValidationFails() throws Exception
	{
		TPolicy policyModel = new TPolicy();
		Mockito.doThrow(new RuntimeException()).when(policyFactory).buildPolicy(policyModel);

		try
		{
			policyService.createPolicy(policyModel);
			Assertions.failBecauseExceptionWasNotThrown(InvalidPolicyException.class);
		}
		catch (InvalidPolicyException ipe)
		{
			// ok
		}
		catch (Exception e)
		{
			Assertions.fail("Exception was not thrown", e);
		}
	}

	public void updatePolicy() throws Exception
	{
		TPolicy policyModel = new TPolicy();
		policyModel.setId("update_id");

		Policy policyInstance = new Policy();
		policyInstance.setModel(policyModel);

		Mockito.when(policyFactory.buildPolicy(policyModel)).thenReturn(policyInstance);

		policyService.updatePolicy(policyModel);

	}

	public void deletePolicy() throws Exception
	{

		String policyId = "delete_id";
		TPolicy policyModel = new TPolicy();
		policyModel.setId(policyId);

		Mockito.when(policyDao.get(policyId)).thenReturn(policyModel);
		Mockito.doNothing().when(policyDao).delete(policyModel);

		boolean isDeleted = policyService.deletePolicy(policyId);
		Assertions.assertThat(isDeleted).isTrue();
	}

	public void deleteNonExistingPolicy() throws Exception
	{

		String policyId = "delete_id_not_found";

		Mockito.when(policyDao.get(policyId)).thenReturn(null);
		boolean isDeleted = policyService.deletePolicy(policyId);
		Assertions.assertThat(isDeleted).isFalse();

		Mockito.verify(policyDao, Mockito.times(1)).get(policyId);
	}

	public void getPolicy() throws Exception
	{

		String policyId = "policy_id";
		TPolicy policyModel = new TPolicy();
		policyModel.setId(policyId);

		Mockito.when(policyDao.get(policyId)).thenReturn(policyModel);

		TPolicy existingPolicy = policyService.getPolicy(policyId);
		Assertions.assertThat(existingPolicy).isEqualsToByComparingFields(policyModel);
	}

	public void getPolicyNotFound() throws Exception
	{

		String policyId = "policy_id";
		Mockito.when(policyDao.get(policyId)).thenReturn(null);

		try
		{
			policyService.getPolicy(policyId);
			Assertions.failBecauseExceptionWasNotThrown(PolicyNotFoundException.class);
		}
		catch (PolicyNotFoundException nfe)
		{
			// ok
		}
		catch (Exception e)
		{
			Assertions.failBecauseExceptionWasNotThrown(PolicyNotFoundException.class);
		}
	}
}
