package com.kromatik.dasshy.server.thrift;

import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.transport.TMemoryBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Utility class for Thrift (de) serialization
 */
public class TUtils
{

	/**
	 * Serialize the thrift entity using Json protocol
	 *
	 * @param tEntity thrift entity
	 *
	 * @return byte[]
	 *
	 * @throws TException
	 */
	public static byte[] serializeJson(final TBase tEntity) throws TException
	{
		final TMemoryBuffer memoryBuffer = new TMemoryBuffer(1);
		tEntity.write(new TJSONProtocol(memoryBuffer));
		memoryBuffer.flush();
		try
		{
			return memoryBuffer.toString("UTF-8").getBytes();
		}
		catch (UnsupportedEncodingException e)
		{
			throw new TException(e);
		}
		finally
		{
			memoryBuffer.close();
		}
	}

	/**
	 * Deserialize into thrift entity using JSON protocol
	 *
	 * @param bytesEntity byte[] to deserialize
	 *
	 * @param tBase thrift entity
	 *
	 * @throws TException
	 */
	public static void deserializeJson(byte[] bytesEntity, final TBase tBase) throws TException
	{
		final TMemoryBuffer memoryBuffer = new TMemoryBuffer(bytesEntity.length);
		memoryBuffer.write(bytesEntity);
		tBase.read(new TJSONProtocol(memoryBuffer));
		memoryBuffer.close();
	}

	/**
	 * Serialize the thrift entity using Compact protocol
	 *
	 * @param tEntity thrift entity
	 *
	 * @return byte[]
	 *
	 * @throws TException
	 */
	public static byte[] serializeCompact(final TBase tEntity) throws TException
	{
		final TMemoryBuffer memoryBuffer = new TMemoryBuffer(1);
		tEntity.write(new TCompactProtocol(memoryBuffer));
		memoryBuffer.flush();
		try
		{
			return memoryBuffer.getArray();
		}
		finally
		{
			memoryBuffer.close();
		}
	}

	/**
	 * Deserialize into thrift entity using Compact protocol
	 *
	 * @param bytesEntity byte[] to deserialize
	 *
	 * @param tBase thrift entity
	 *
	 * @throws TException
	 */
	public static void deserializeCompact(byte[] bytesEntity, final TBase tBase) throws TException
	{
		final TMemoryBuffer memoryBuffer = new TMemoryBuffer(bytesEntity.length);
		memoryBuffer.write(bytesEntity);
		tBase.read(new TCompactProtocol(memoryBuffer));
		memoryBuffer.close();
	}
}

