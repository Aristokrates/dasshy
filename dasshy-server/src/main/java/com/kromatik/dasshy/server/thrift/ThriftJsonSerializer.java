package com.kromatik.dasshy.server.thrift;

import org.apache.commons.io.IOUtils;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Thrift+JSON serializer for thrift entities
 *
 * @param <T> thrift entity
 */
public class ThriftJsonSerializer<T extends TBase> implements EntitySerializer<T>
{

	@Override
	public void write(final T t, final OutputStream outputStream) throws Exception
	{
		byte[] bytesEntity;
		try
		{
			bytesEntity = TUtils.serializeJson(t);
		}
		catch (final Exception e)
		{
			throw new Exception(
							"Failed to serialise Thrift entity to Thrift JSON format. Thrift entity toString(): '{" + ((
											t == null) ? "null" : t.toString()) + "'}", e);
		}
		outputStream.write(bytesEntity);
	}

	@Override
	public T read(final Class<T> tClass, final InputStream inputStream) throws Exception
	{
		T entity;
		try
		{
			entity = tClass.newInstance();
			byte[] bytesEntity = IOUtils.toByteArray(inputStream);
			try
			{
				TUtils.deserializeJson(bytesEntity, entity);
				return entity;
			}
			catch (TException e)
			{
				throw new Exception("Failed to deserialize Thrift JSON format to Thrift entity. JSON: '{" + new String(
								bytesEntity, Charset.forName("UTF-8")) + "}'." +
								" Thrift type: {" + tClass.getName() + "}", e);
			}
		}
		catch (InstantiationException e)
		{
			throw new Exception(
							"Failed to instantiate a Thrift object when deserializing Thrift JSON format to Thrift entity. Thrift type: {"
											+ tClass.getName() + "}", e);

		}
		catch (IllegalAccessException e)
		{
			throw new Exception(
							"Failed to instantiate a Thrift object (the class or its nullary constructor is not accessible) while deserializing Thrift JSON format",
							e);
		}
	}
}

