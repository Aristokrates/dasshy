package com.kromatik.dasshy.server.rest;

import com.kromatik.dasshy.server.streaming.DasshyRuntime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Root api endpoint
 */
@Path("/")
public class DasshyRestApi extends AbstractRestApi
{
	/** dasshy runtime */
	private final DasshyRuntime		runtime;

	/**
	 * Default constructor. Used for injecting dependencies
	 *
	 * @param dasshyRuntime dasshy server runtime
	 */
	public DasshyRestApi(final DasshyRuntime dasshyRuntime)
	{
		runtime = dasshyRuntime;
	}

	@GET
	@Path("version")
	public Response getVersion() {
		return Response.status(Response.Status.OK).entity("Version").build();
	}
}
