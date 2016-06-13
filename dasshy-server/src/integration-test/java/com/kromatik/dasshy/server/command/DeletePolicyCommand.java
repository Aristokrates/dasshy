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

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Delete policy by id
 */
public class DeletePolicyCommand extends DasshyHttpCommand<Void>
{

	private String	policyId;

	/**
	 * Default constructor
	 *
	 * @param webTarget web target
	 * @param policyId policy id to be deleted
	 */
	public DeletePolicyCommand(final WebTarget webTarget, final String policyId)
	{
		super(webTarget, "dasshy-server", "delete-policy");
		this.policyId = policyId;
	}

	@Override
	public Response getResponse()
	{
		return getWebTargetBuilder().delete();
	}

	@Override
	protected Invocation.Builder getWebTargetBuilder()
	{
		return webTarget.path("policy")
						.path(policyId)
						.request()
						.accept(mediaType);
	}

	@Override
	protected Void readResult(Response response)
	{
		return null;
	}

	@Override
	protected boolean validateInput()
	{
		if (policyId == null)
		{
			processIllegalArguments("Policy Id is missing");
		}

		return true;
	}
}
