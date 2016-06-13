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
package com.kromatik.dasshy.server.thrift;

import javax.ws.rs.core.MediaType;

/**
 * Available REST media types
 */
public enum DasshyMediaType
{
	JSON("application", "json"),
	THRIFT_JSON("application", "x-thrift+json"),
	THRIFT("application", "x-thrift");

	private final MediaType mediaType;

	DasshyMediaType(final String type, final String subType)
	{
		this.mediaType = new MediaType(type, subType);
	}

	public MediaType getMediaType()
	{
		return mediaType;
	}

	public static DasshyMediaType fromMediaType(final MediaType mediaType)
	{
		for (final DasshyMediaType agoraType : DasshyMediaType.values())
		{
			if (agoraType.getMediaType().isCompatible(mediaType))
			{
				return agoraType;
			}
		}
		return null;
	}

	@Override
	public String toString()
	{
		return mediaType.toString();
	}
}