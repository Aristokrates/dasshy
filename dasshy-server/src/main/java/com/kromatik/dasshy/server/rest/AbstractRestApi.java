package com.kromatik.dasshy.server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Abstract Jersey resource that all other resource classes should extend from
 */
@Produces({"application/json","application/x-thrift+json","application/x-thrift"})
@Consumes({"application/json","application/x-thrift+json","application/x-thrift"})
public abstract class AbstractRestApi
{

	@GET
	@Path("/ping")
	public Response ping()
	{
		return Response.status(Response.Status.OK).entity(System.currentTimeMillis()).build();
	}
}
