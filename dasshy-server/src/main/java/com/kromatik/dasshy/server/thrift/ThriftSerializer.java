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

import com.kromatik.dasshy.server.exception.SerializationException;
import org.apache.commons.io.IOUtils;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Thrift serializer for thrift entities
 *
 * @param <T> thrift entity
 */
public class ThriftSerializer<T extends TBase> implements EntitySerializer<T>
{

	@Override
	public void write(final T t, final OutputStream outputStream)
	{
		try
		{
			byte[] bytesEntity = serialize(t);
			outputStream.write(bytesEntity);
		}
		catch (final IOException e)
		{
			throw new SerializationException("Cannot write base entity", e);
		}
		catch (final Exception e)
		{
			throw new SerializationException(
							"Failed to serialise Thrift entity to Thrift JSON format. Thrift entity toString(): '{" + ((
											t == null) ? "null" : t.toString()) + "'}", e);
		}
	}

	@Override
	public T read(final Class<T> tClass, final InputStream inputStream)
	{
		T entity;
		byte[] bytesEntity = null;
		try
		{
			entity = tClass.newInstance();
			bytesEntity = IOUtils.toByteArray(inputStream);
			deserialize(bytesEntity, entity);
			return entity;
		}
		catch (final TException e)
		{
			throw new SerializationException("Failed to deserialize Thrift JSON format to Thrift entity. JSON: '{" + new String(
							bytesEntity, Charset.forName("UTF-8")) + "}'." +
							" Thrift type: {" + tClass.getName() + "}", e);
		}
		catch (final InstantiationException e)
		{
			throw new SerializationException(
							"Failed to instantiate a Thrift object when deserializing Thrift JSON format to Thrift entity. Thrift type: {"
											+ tClass.getName() + "}", e);
		}
		catch (final IllegalAccessException e)
		{
			throw new SerializationException(
							"Failed to instantiate a Thrift object (the class or its nullary constructor is not accessible) while deserializing Thrift JSON format",
							e);
		}
		catch (final IOException e)
		{
			throw new SerializationException("Cannot read thrift object", e);
		}
	}

	/**
	 * Deserializes a thrift entity
	 *
	 * @param bytes byte array
	 * @param entity  thrift entity
	 *
	 * @throws TException exception
	 */
	protected void deserialize(byte[] bytes, final T entity) throws TException
	{
		TUtils.deserializeCompact(bytes, entity);
	}

	/**
	 * Serializes a thrift entity
	 *
	 * @param t thrift entity
	 *
	 * @return byte array
	 *
	 * @throws TException exception
	 */
	protected byte[] serialize(final T t) throws TException
	{
		return TUtils.serializeCompact(t);
	}
}

