package com.kromatik.dasshy.server.exception.mapper;

import com.kromatik.dasshy.thrift.model.TError;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import java.text.MessageFormat;

/**
 * Map exception to a response type
 */
public abstract class AbstractExceptionMapper<T extends Throwable> implements ExceptionMapper<T>
{

	/**HTTP headers*/
	@Context HttpHeaders			headers;

	/**uri context*/
	@Context UriInfo				uriInfo;

	/**
	 * Adds content type of request to response builder if accept type is not provided, cause if accept type IS provided
	 * then Jersey default behavior kicks in.
	 * If request content type also missing, then falling back to "application/json" to prevent server failure.
	 *
	 * @param builder	response builder
	 *
	 * @return	http response
	 */
	protected Response buildResponseWithContentType(final Response.ResponseBuilder builder)
	{
		return buildResponseWithContentType(builder, null);
	}

	/**
	 * Adds content type of request to response builder if accept type is not provided, cause if accept type IS provided
	 * then Jersey default behavior kicks in.
	 * If request content type also missing, then falling back to "application/json" to prevent server failure.
	 *
	 * @param builder	response builder
	 * @param status	status code
	 *
	 * @return http response
	 */
	protected Response buildResponseWithContentType(
					final Response.ResponseBuilder builder,
					final Response.Status status
	)
	{
		final MediaType acceptType = headers.getAcceptableMediaTypes().isEmpty() ?
						headers.getAcceptableMediaTypes().get(0) :
						null;

		// check the status
		if (status != null)
		{
			if (Response.Status.NOT_ACCEPTABLE.getStatusCode() == status.getStatusCode())
			{
				return builder.type(MediaType.APPLICATION_JSON_TYPE).build();
			}
		}
		if (acceptType == null || acceptType.isWildcardType())
		{
			final MediaType contentType = headers.getMediaType();
			if (contentType == null || contentType.isWildcardType())
			{
				// fallback
				builder.type(MediaType.APPLICATION_JSON_TYPE);
			}
			else
			{
				builder.type(contentType);
			}
		}
		return builder.build();
	}

	/**
	 * Builds the response payload.
	 *
	 * @param error		error message
	 * @param e			exception
	 * @param status	status code
	 *
	 * @return	detailed error entity
	 */
	protected TError buildResponsePayload(final TError error, final Throwable e, final Response.Status status)
	{
		if (error == null)
		{
			return null;
		}

		error.setDebug(buildDebugMessage(error, e, status));

		return error;
	}

	/**
	 * Builds debug message depending on thrown exception and passed ResponseErrorMessage.
	 *
	 * @param error 	error message
	 * @param e			exception
	 * @param status	status code
	 *
	 * @return debug message
	 */
	public String buildDebugMessage(final TError error, final Throwable e, final Response.Status status)
	{
		return MessageFormat.format("Service Host: {1}{0}" + "Stack Trace: {2}",
						System.lineSeparator(),
						uriInfo.getBaseUri().getHost(),
						ExceptionUtils.getFullStackTrace(e));
	}

}