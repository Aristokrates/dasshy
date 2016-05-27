package com.kromatik.dasshy.server.exception.mapper;

import com.kromatik.dasshy.thrift.model.TError;

import javax.ws.rs.core.Response;

/**
 * Mapper from {@link Exception} to a {@link com.kromatik.dasshy.thrift.model.TError} response
 */
public class CommonExceptionMapper extends AbstractExceptionMapper<Exception>
{

	@Override
	public Response toResponse(final Exception exception)
	{

		final Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
		final TError responseErrorMessage = new TError(exception.getMessage());

		final Response.ResponseBuilder responseBuilder = Response.status(status).entity(
						buildResponsePayload(responseErrorMessage, exception, status));

		return buildResponseWithContentType(responseBuilder);
	}
}
