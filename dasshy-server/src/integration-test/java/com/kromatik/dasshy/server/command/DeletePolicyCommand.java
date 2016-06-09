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
