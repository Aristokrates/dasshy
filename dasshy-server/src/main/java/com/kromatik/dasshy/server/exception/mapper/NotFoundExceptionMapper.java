package com.kromatik.dasshy.server.exception.mapper;

import com.kromatik.dasshy.thrift.model.TError;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

/**
 * Mapper for Jersey's {@link NotFoundException} called when a resource cannot be found in Jersey
 */
public class NotFoundExceptionMapper extends AbstractExceptionMapper<NotFoundException>
{
	@Override
	public Response toResponse(final NotFoundException exception)
	{
		// get client response status
		final Response.Status status = Response.Status.fromStatusCode(exception.getResponse().getStatus());
		// create the response error message
		final TError responseErrorMessage = new TError(exception.getMessage());

		// build the response payload
		final Response.ResponseBuilder responseBuilder = Response.status(status)
						.header("dasshy.error-message", responseErrorMessage.getMessage())
						.entity(buildResponsePayload(responseErrorMessage, exception, status));

		// build the response with the correct content type
		return buildResponseWithContentType(responseBuilder, status);
	}
}
