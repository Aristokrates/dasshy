package com.kromatik.dasshy.server.thrift;

import javax.ws.rs.core.MediaType;

/**
 * Available REST media types
 */
public enum DasshyMediaType
{
    JSON            ("application", "json"),
    THRIFT_JSON     ("application", "x-thrift+json"),
	THRIFT          ("application", "x-thrift");

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