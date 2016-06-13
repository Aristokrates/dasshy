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

