package com.kromatik.dasshy.server.command;

import com.kromatik.dasshy.server.exception.DasshyBadRequestException;
import com.kromatik.dasshy.server.exception.DasshyServerErrorException;
import com.kromatik.dasshy.thrift.model.TError;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Http command
 *
 * @param <R> result
 */
public abstract class DasshyHttpCommand<R> extends DasshyCommand<R>
{

	protected WebTarget webTarget;

	protected String	serviceName;
	protected String	commandName;

	protected MediaType mediaType	=	MediaType.APPLICATION_JSON_TYPE;

	public DasshyHttpCommand(WebTarget webTarget, String serviceName, String commandName)
	{
		super(serviceName + "_" + commandName);
		this.webTarget = webTarget;
		this.serviceName = serviceName;
		this.commandName = commandName;
	}

	/**
	 * Gets the client response out of the web builder, depending on the HTTP type (GET, POST, DELETE)
	 *
	 * @return client response
	 */
	public abstract Response getResponse();

	/**
	 * Creates a web resource builder for the given http endpoint
	 *
	 * @return invocation builder
	 */
	protected abstract Invocation.Builder getWebTargetBuilder();

	/**
	 * Read the result from the response
	 *
	 * @param response response
	 *
	 * @return result
	 */
	protected abstract R readResult(final Response response);

	/**
	 * Validates Command input, prior to call actual service.
	 * In case of invalid input, method implementation should call
	 * processIllegalArguments("message_about_invalid_input_details").
	 * Which will throw runtime exception.
	 * If you can't perform client side validation just return true from this method.
	 *
	 * @return true, if validation passes
	 */
	protected abstract boolean validateInput();

	/**
	 * Called on validateInput when some of the input arguments are invalid
	 *
	 * @param message error message
	 */
	protected void processIllegalArguments(final String message) {
		throw new DasshyBadRequestException(message);
	}

	/**
	 * Set media type for both accept and entity type
	 *
	 * @param mediaType media type
	 */
	public void setMediaType(final MediaType mediaType) {
		this.mediaType = mediaType;
	}


	@Override
	public R run()
	{
		Response response = null;

		try
		{
			validateInput();
			response = this.getResponse();
			int status = response.getStatus();
			if (status >= 500 && status < 600)
			{
				TError error = obtainErrorFromResponse(response);
				throw new DasshyServerErrorException(status, error);
			}
			else if (status >= 400 && status < 500)
			{
				TError error = obtainErrorFromResponse(response);
				throw new DasshyBadRequestException(status, error);
			}
			else if (status >= 200 && status < 300)
			{
				return readResult(response);
			}

			throw new RuntimeException("Service returned unknown status code: " + status);
		}
		catch (final Exception ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			if (response != null)
			{
				response.close();
			}
		}
	}

	/**
	 * Get the error from the response
	 *
	 * @param response response
	 *
	 * @return error
	 */
	protected TError obtainErrorFromResponse(final Response response)
	{
		TError error = null;
		try
		{
			error = response.readEntity(TError.class);
			error.setMessage("serviceName: '" + serviceName + "', commandName: '" + commandName + "', error message: '"
							+ error.getMessage() + "'");
		}
		catch (ProcessingException e)
		{
			// warning
		}
		if (error == null)
		{
			error = new TError("Error: Service does not responded with TError entity. " +
							"serviceName: '" + serviceName + "', commandName: '" + commandName + "', Http status: "
							+ response.getStatus());
			error.setCode(response.getStatus());
		}
		return error;
	}
}
