package com.kromatik.dasshy.server.command;

import com.kromatik.dasshy.thrift.model.TPolicy;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Command for updating policies
 */
public class UpdatePolicyCommand extends DasshyHttpCommand<TPolicy>
{
	private TPolicy		policy;

	/**
	 * Default constructor
	 *
	 * @param webTarget web target
	 * @param policy policy to be updated
	 */
	public UpdatePolicyCommand(final WebTarget webTarget, final TPolicy policy)
	{
		super(webTarget, "dasshy-server", "update-policy");
		this.policy = policy;
	}

	@Override
	public Response getResponse()
	{
		return getWebTargetBuilder().put(Entity.entity(policy, mediaType));
	}

	@Override
	protected Invocation.Builder getWebTargetBuilder()
	{
		return webTarget.path("policy")
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
		if (policy == null)
		{
			processIllegalArguments("Policy is not provided");
		}

		return true;
	}
}
