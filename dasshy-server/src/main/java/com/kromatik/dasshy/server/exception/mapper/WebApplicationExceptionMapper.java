package com.kromatik.dasshy.server.exception.mapper;

import com.kromatik.dasshy.thrift.model.TError;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Exception mapper for {@link WebApplicationException}
 */
public class WebApplicationExceptionMapper extends AbstractExceptionMapper<WebApplicationException>
{

	@Override
	public Response toResponse(final WebApplicationException exception)
	{
		final Response.Status status = Response.Status.fromStatusCode(exception.getResponse().getStatus());
		final TError responseErrorMessage = new TError(exception.getMessage());

		final Response.ResponseBuilder responseBuilder = Response.status(status).entity(
						buildResponsePayload(responseErrorMessage, exception, status));

		return buildResponseWithContentType(responseBuilder);

	}
}
