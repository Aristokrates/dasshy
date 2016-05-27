package com.kromatik.dasshy.server.thrift;

import org.apache.thrift.TBase;
import org.apache.thrift.TException;

/**
 * Thrift+JSON serializer for thrift entities
 *
 * @param <T> thrift entity
 */
public class ThriftJsonSerializer<T extends TBase> extends ThriftSerializer<T>
{
	@Override
	protected byte[] serialize(final T t) throws TException
	{
		return TUtils.serializeJson(t);
	}

	@Override
	protected void deserialize(byte[] bytes, final T entity) throws TException
	{
		TUtils.deserializeJson(bytes, entity);
	}
}

