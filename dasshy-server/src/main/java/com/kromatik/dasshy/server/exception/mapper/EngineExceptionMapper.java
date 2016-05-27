package com.kromatik.dasshy.server.exception.mapper;

import com.kromatik.dasshy.core.exception.EngineException;
import com.kromatik.dasshy.thrift.model.TError;

import javax.ws.rs.core.Response;

/**
 * Exception mapper for {@link EngineException}
 */
public class EngineExceptionMapper extends AbstractExceptionMapper<EngineException>
{
	@Override
	public Response toResponse(final EngineException exception)
	{

		final Response.Status status = Response.Status.fromStatusCode(exception.getStatus());

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
