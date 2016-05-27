package com.kromatik.dasshy.server.thrift;

import com.kromatik.dasshy.server.exception.SerializationException;
import org.apache.thrift.TBase;
import org.apache.thrift.protocol.TSimpleJSONProtocol;
import org.apache.thrift.transport.TMemoryBuffer;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
	public void write(final T t, final OutputStream outputStream)
	{
		byte[] bytesEntity;
		TMemoryBuffer memoryBuffer = null;
		try
		{
			memoryBuffer = new TMemoryBuffer(1);
			t.write(new TSimpleJSONProtocol(memoryBuffer));
			memoryBuffer.flush();

			bytesEntity = memoryBuffer.toString("UTF-8").getBytes("UTF-8");

			outputStream.write(bytesEntity);

		}
		catch (IOException e)
		{
			throw new SerializationException("Cannot write base entity", e);
		}
		catch (final Exception e)
		{
			throw new SerializationException(
							"Failed to serialise Thrift entity to Simple JSON format. Thrift entity toString(): '" + ((t
											== null) ? "null" : t.toString()) + "'", e);
		}
		finally
		{
			if (memoryBuffer != null)
			{
				memoryBuffer.close();
			}
		}
	}

	@Override
	public T read(final Class<T> tClass, final InputStream inputStream)
	{
		try
		{
			return objectMapper.readValue(inputStream, tClass);
		}
		catch (final Exception e)
		{
			throw new SerializationException("Failed to deserialize Simple JSON format to Thrift entity. Thrift type: " + tClass
							.getName(), e);
		}
	}
}
