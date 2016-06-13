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
package com.kromatik.dasshy.server.rest;

import com.kromatik.dasshy.server.exception.InvalidPolicyException;
import com.kromatik.dasshy.server.exception.PolicyExistsException;
import com.kromatik.dasshy.server.exception.PolicyNotFoundException;
import com.kromatik.dasshy.server.exception.mapper.CommonExceptionMapper;
import com.kromatik.dasshy.server.exception.mapper.EngineExceptionMapper;
import com.kromatik.dasshy.server.exception.mapper.NotFoundExceptionMapper;
import com.kromatik.dasshy.server.exception.mapper.WebApplicationExceptionMapper;
import com.kromatik.dasshy.server.service.PolicyService;
import com.kromatik.dasshy.server.thrift.SimpleJsonPayLoadProvider;
import com.kromatik.dasshy.server.thrift.ThriftJsonPayLoadProvider;
import com.kromatik.dasshy.server.thrift.ThriftPayLoadProvider;
import com.kromatik.dasshy.thrift.model.TPolicy;
import com.kromatik.dasshy.thrift.model.TPolicyList;
import org.fest.assertions.api.Assertions;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTestNg;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

/**
 * Tests the policy rest api
 */
@Test(groups = { "rest" })
public class PolicyRestApiTest extends JerseyTestNg.ContainerPerMethodTest
{
	private PolicyService policyService;

	@Override
	protected Application configure()
	{
		policyService = Mockito.mock(PolicyService.class);

		ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.registerInstances(new PolicyRestApi(policyService));

		resourceConfig.register(SimpleJsonPayLoadProvider.class);
		resourceConfig.register(ThriftJsonPayLoadProvider.class);
		resourceConfig.register(ThriftPayLoadProvider.class);

		resourceConfig.register(new CommonExceptionMapper());
		resourceConfig.register(new EngineExceptionMapper());
		resourceConfig.register(new NotFoundExceptionMapper());
		resourceConfig.register(new WebApplicationExceptionMapper());

		return resourceConfig;
	}

	@Override
	protected void configureClient(ClientConfig config)
	{
		config.register(SimpleJsonPayLoadProvider.class);
		config.register(ThriftJsonPayLoadProvider.class);
		config.register(ThriftPayLoadProvider.class);

		config.register(new LoggingFilter());
	}

	@Override
	protected TestContainerFactory getTestContainerFactory() throws TestContainerException
	{
		return new GrizzlyTestContainerFactory();
	}

	public void createPolicyOk() throws Exception
	{
		TPolicy policyModel = new TPolicy();
		Mockito.when(policyService.createPolicy(Mockito.eq(policyModel))).thenReturn(policyModel);

		Response response = getPolicyWebTarget().request().accept(MediaType.APPLICATION_JSON_TYPE)
						.post(Entity.entity(policyModel, MediaType.APPLICATION_JSON_TYPE));

		TPolicy responsePolicyModel = response.readEntity(TPolicy.class);

		Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Assertions.assertThat(responsePolicyModel).isEqualTo(policyModel);
	}

	public void createPolicyInvalid() throws Exception
	{
		TPolicy policyModel = new TPolicy();
		Mockito.when(policyService.createPolicy(Mockito.eq(policyModel)))
						.thenThrow(new InvalidPolicyException("Invalid policy model: " + policyModel));

		Response response = getPolicyWebTarget().request().accept(MediaType.APPLICATION_JSON_TYPE)
						.post(Entity.entity(policyModel, MediaType.APPLICATION_JSON_TYPE));

		Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
	}

	public void createPolicyAlreadyExisting() throws Exception
	{
		TPolicy policyModel = new TPolicy();
		Mockito.when(policyService.createPolicy(Mockito.eq(policyModel)))
						.thenThrow(new PolicyExistsException("Policy with the same Id already exists"));

		Response response = getPolicyWebTarget().request().accept(MediaType.APPLICATION_JSON_TYPE)
						.post(Entity.entity(policyModel, MediaType.APPLICATION_JSON_TYPE));

		Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.CONFLICT.getStatusCode());
	}

	public void createPolicyInternalServerError() throws Exception
	{
		TPolicy policyModel = new TPolicy();
		Mockito.when(policyService.createPolicy(Mockito.eq(policyModel)))
						.thenThrow(new RuntimeException("Internal Server error"));

		Response response = getPolicyWebTarget().request().accept(MediaType.APPLICATION_JSON_TYPE)
						.post(Entity.entity(policyModel, MediaType.APPLICATION_JSON_TYPE));

		Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
	}

	public void updatePolicyInvalid() throws Exception
	{
		TPolicy policyModel = new TPolicy();
		Mockito.when(policyService.updatePolicy(Mockito.eq(policyModel)))
						.thenThrow(new InvalidPolicyException("Invalid policy model: " + policyModel));

		Response response = getPolicyWebTarget().request().accept(MediaType.APPLICATION_JSON_TYPE)
						.put(Entity.entity(policyModel, MediaType.APPLICATION_JSON_TYPE));

		Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
	}

	public void updatePolicyNonExisting() throws Exception
	{
		TPolicy policyModel = new TPolicy();
		Mockito.when(policyService.updatePolicy(Mockito.eq(policyModel)))
						.thenThrow(new PolicyNotFoundException("Policy not found for update"));

		Response response = getPolicyWebTarget().request().accept(MediaType.APPLICATION_JSON_TYPE)
						.put(Entity.entity(policyModel, MediaType.APPLICATION_JSON_TYPE));

		Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
	}

	public void updatePolicyInternalServerError() throws Exception
	{
		TPolicy policyModel = new TPolicy();
		Mockito.when(policyService.updatePolicy(Mockito.eq(policyModel)))
						.thenThrow(new RuntimeException("Internal Server error"));

		Response response = getPolicyWebTarget().request().accept(MediaType.APPLICATION_JSON_TYPE)
						.put(Entity.entity(policyModel, MediaType.APPLICATION_JSON_TYPE));

		Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
	}

	public void updatePolicyOk() throws Exception
	{
		TPolicy policyModel = new TPolicy();
		Mockito.when(policyService.updatePolicy(Mockito.eq(policyModel))).thenReturn(policyModel);

		Response response = getPolicyWebTarget().request().accept(MediaType.APPLICATION_JSON_TYPE)
						.put(Entity.entity(policyModel, MediaType.APPLICATION_JSON_TYPE));

		TPolicy responsePolicyModel = response.readEntity(TPolicy.class);

		Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Assertions.assertThat(responsePolicyModel).isEqualTo(policyModel);
	}

	public void deletePolicyOk() throws Exception
	{
		String policyId = "id";
		Mockito.when(policyService.deletePolicy(Mockito.eq(policyId))).thenReturn(true);

		Response response = getPolicyWebTarget().path(policyId).request().accept(MediaType.APPLICATION_JSON_TYPE)
						.delete();

		Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
	}

	public void getPolicyOk() throws Exception
	{
		String policyId = "id";
		TPolicy policyModel = new TPolicy();

		Mockito.when(policyService.getPolicy(Mockito.eq(policyId))).thenReturn(policyModel);

		Response response = getPolicyWebTarget().path(policyId).request().accept(MediaType.APPLICATION_JSON_TYPE).get();

		TPolicy responsePolicyModel = response.readEntity(TPolicy.class);

		Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Assertions.assertThat(responsePolicyModel).isEqualTo(policyModel);
	}

	public void getPolicyNonExisting() throws Exception
	{
		String policyId = "id";
		Mockito.when(policyService.getPolicy(Mockito.eq(policyId)))
						.thenThrow(new PolicyNotFoundException("Policy with id: " + policyId + " not found"));

		Response response = getPolicyWebTarget().path(policyId).request().accept(MediaType.APPLICATION_JSON_TYPE).get();

		Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
	}

	public void getPolicyList() throws Exception
	{
		TPolicy policyModel = new TPolicy();
		Mockito.when(policyService.listPolicies()).thenReturn(new TPolicyList(Arrays.asList(policyModel)));

		Response response = getPolicyWebTarget().request().accept(MediaType.APPLICATION_JSON_TYPE).get();

		TPolicyList responsePolicyList = response.readEntity(TPolicyList.class);

		Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Assertions.assertThat(responsePolicyList.getPoliciesSize()).isEqualTo(1);
		Assertions.assertThat(responsePolicyList.getPolicies()).contains(policyModel);
	}

	private WebTarget getPolicyWebTarget()
	{
		return target().path("policy");
	}
}
