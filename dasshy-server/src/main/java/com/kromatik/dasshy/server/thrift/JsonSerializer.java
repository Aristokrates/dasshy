package com.kromatik.dasshy.server.thrift;

import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TSimpleJSONProtocol;
import org.apache.thrift.transport.TMemoryBuffer;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * JSON serializer for thrift entities
 *
 * @param <T> thrift entity
 */
public class JsonSerializer<T extends TBase> implements EntitySerializer<T>
{
	/** object mapper */
	private final ObjectMapper			objectMapper;

	/**
	 * Default constructor
	 */
	public JsonSerializer()
	{
		this.objectMapper = new ObjectMapper()
						.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
						.configure(SerializationConfig.Feature.AUTO_DETECT_IS_GETTERS, false);
	}

	@Override
	public void write(final T t, final OutputStream outputStream) throws Exception
	{
		byte[] bytesEntity;
		try
		{
			final TMemoryBuffer memoryBuffer = new TMemoryBuffer(1);
			t.write(new TSimpleJSONProtocol(memoryBuffer));
			memoryBuffer.flush();
			try
			{
				bytesEntity = memoryBuffer.toString("UTF-8").getBytes();
			}
			catch (final UnsupportedEncodingException e)
			{
				throw new TException(e);
			}
			finally
			{
				memoryBuffer.close();
			}
		}
		catch (final Exception e)
		{
			throw new Exception(
							"Failed to serialise Thrift entity to Simple JSON format. Thrift entity toString(): '" + ((t
											== null) ? "null" : t.toString()) + "'", e);
		}
		outputStream.write(bytesEntity);
	}

	@Override
	public T read(final Class<T> tClass, final InputStream inputStream) throws Exception
	{
		try
		{
			return objectMapper.readValue(inputStream, tClass);
		}
		catch (final Exception e)
		{
			throw new Exception("Failed to deserialize Simple JSON format to Thrift entity. Thrift type: " + tClass
							.getName(), e);
		}
	}
}
