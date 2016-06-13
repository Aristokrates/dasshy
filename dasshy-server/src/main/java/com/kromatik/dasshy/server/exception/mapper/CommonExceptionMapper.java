/**
 * Dasshy - Real time and Batch Analytics Open Source System
 * Copyright (C) 2016 Kromatik Solutions (http://kromatiksolutions.com)
 *
 * This file is part of Dasshy
 *
 * Dasshy is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Dasshy is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Dasshy.  If not, see <http://www.gnu.org/licenses/>.
 */
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

		final Response.ResponseBuilder responseBuilder = Response.status(status)
						.entity(buildResponsePayload(responseErrorMessage, exception, status));

		return buildResponseWithContentType(responseBuilder);
	}
}
