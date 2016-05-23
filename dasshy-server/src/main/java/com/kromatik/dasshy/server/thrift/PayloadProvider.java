package com.kromatik.dasshy.server.thrift;

import org.apache.thrift.TBase;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Payload provider
 *
 * @param <T> thrift entity
 */
public abstract class PayloadProvider<T extends TBase> implements MessageBodyReader<T>, MessageBodyWriter<T>
{
	/** media type */
	protected final DasshyMediaType			providerMediaType;

	/** serializer */
	protected final EntitySerializer<T> 	serializer;

	/**
	 * Default constructor
	 *
	 * @param providerMediaType media type
	 * @param serializer serializer per media type
	 */
	public PayloadProvider(final DasshyMediaType providerMediaType, final EntitySerializer<T> serializer)
	{
		this.providerMediaType = providerMediaType;
		this.serializer = serializer;
	}

	@Override
	public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType)
	{
		return TBase.class.isAssignableFrom(aClass) && providerMediaType.getMediaType().isCompatible(mediaType);
	}

	@Override
	public long getSize(T t, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType)
	{
		return -1;
	}

	@Override
	public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
					MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
					throws IOException, WebApplicationException
	{
		try
		{
			serializer.write(t, entityStream);
		}
		catch (final Exception e)
		{
			if (e instanceof org.eclipse.jetty.io.EofException)
			{
				throw new IOException(e);
			}
			else
			{
				throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@Override
	public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType)
	{
		return TBase.class.isAssignableFrom(aClass) && providerMediaType.getMediaType().isCompatible(mediaType);
	}

	@Override
	public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
					MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
					throws IOException, WebApplicationException
	{
		try
		{
			return serializer.read(type, entityStream);
		}
		catch (final Exception e)
		{
			throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
	}
}
