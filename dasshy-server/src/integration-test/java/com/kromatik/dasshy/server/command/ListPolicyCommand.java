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
package com.kromatik.dasshy.server.command;

import com.kromatik.dasshy.thrift.model.TPolicyList;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * List all policies
 */
public class ListPolicyCommand extends DasshyHttpCommand<TPolicyList>
{

	/**
	 * Default constructor
	 *
	 * @param webTarget web target
	 */
	public ListPolicyCommand(final WebTarget webTarget)
	{
		super(webTarget, "dasshy-server", "list-policy");
	}

	@Override
	public Response getResponse()
	{
		return getWebTargetBuilder().get();
	}

	@Override
	protected Invocation.Builder getWebTargetBuilder()
	{
		return webTarget.path("policy").request().accept(mediaType);
	}

	@Override
	protected TPolicyList readResult(Response response)
	{
		return response.readEntity(TPolicyList.class);
	}

	@Override
	protected boolean validateInput()
	{
		return true;
	}
}
