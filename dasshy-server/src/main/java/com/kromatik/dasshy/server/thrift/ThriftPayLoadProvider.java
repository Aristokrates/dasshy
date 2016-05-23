package com.kromatik.dasshy.server.thrift;

import org.apache.thrift.TBase;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

/**
 * Thrift payload provider
 *
 * @param <T> thrift entity
 */
@Provider
@Produces("application/x-thrift")
@Consumes("application/x-thrift")
public class ThriftPayLoadProvider<T extends TBase> extends PayloadProvider<T> {

	/**
	 * Default constructor
	 */
	public ThriftPayLoadProvider() {
		super(DasshyMediaType.THRIFT, new ThriftSerializer<T>());
	}
}
