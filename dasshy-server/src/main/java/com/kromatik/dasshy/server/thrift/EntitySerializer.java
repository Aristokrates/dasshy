package com.kromatik.dasshy.server.thrift;

import org.apache.thrift.TBase;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Thrift reader/writer
 *
 * @param <T>
 */
public interface EntitySerializer<T extends TBase>
{
	/**
	 * Serialize
	 *
	 * @param t entity to serialize
	 * @param outputStream output stream
	 *
	 * @throws Exception
	 */
	void write(final T t, final OutputStream outputStream);

	/**
	 * Deserialize
	 *
	 * @param tClass thrift class to deserialize to
	 * @param inputStream input stream
	 *
	 * @return thrifr entity
	 *
	 * @throws Exception
	 */
	T read(final Class<T> tClass, final InputStream inputStream);
}
