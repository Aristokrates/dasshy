package com.kromatik.dasshy.server.rest;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Root api endpoint
 */
@Path("/")
public class DasshyRestApi extends AbstractRestApi
{

	/**
	 * Default constructor. Used for injecting dependencies
	 */
	public DasshyRestApi()
	{
		// no-arg
	}

	@GET
	@Path("version")
	public Response getVersion() {
		return Response.status(Response.Status.OK).entity("Version").build();
	}
}
