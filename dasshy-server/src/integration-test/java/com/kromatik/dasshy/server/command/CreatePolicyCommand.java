package com.kromatik.dasshy.server.command;

import com.kromatik.dasshy.thrift.model.TPolicy;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Command for creating policies
 */
public class CreatePolicyCommand extends DasshyHttpCommand<TPolicy>
{
	private TPolicy		policy;

	/**
	 * Default constructor
	 *
	 * @param webTarget eeb target
	 * @param policy policy to be created
	 */
	public CreatePolicyCommand(final WebTarget webTarget, final TPolicy policy)
	{
		super(webTarget, "dasshy-server", "create-policy");
		this.policy = policy;
	}

	@Override
	public Response getResponse()
	{
		return getWebTargetBuilder().post(Entity.entity(policy, mediaType));
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
