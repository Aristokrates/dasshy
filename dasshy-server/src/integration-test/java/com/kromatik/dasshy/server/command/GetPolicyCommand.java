package com.kromatik.dasshy.server.command;

import com.kromatik.dasshy.thrift.model.TPolicy;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Get policy by Id command
 */
public class GetPolicyCommand extends DasshyHttpCommand<TPolicy>
{
	private String policyId;

	/**
	 * Default constructor
	 *
	 * @param webTarget web target
	 * @param policyId policy id
	 */
	public GetPolicyCommand(final WebTarget webTarget, final String policyId)
	{
		super(webTarget, "dasshy-server", "get-policy");
		this.policyId = policyId;
	}

	@Override
	public Response getResponse()
	{
		return getWebTargetBuilder().get();
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
	protected TPolicy readResult(final Response response)
	{
		return response.readEntity(TPolicy.class);
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
