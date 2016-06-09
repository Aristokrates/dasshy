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
		return webTarget.path("policy")
						.request()
						.accept(mediaType);
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
