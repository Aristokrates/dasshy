package com.kromatik.dasshy.server.thrift;

import org.apache.thrift.TBase;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

/**
 * Thrift+json payload provider
 *
 * @param <T> thrift entity
 */
@Provider
@Produces("application/x-thrift+json")
@Consumes("application/x-thrift+json")
public class ThriftJsonPayLoadProvider<T extends TBase> extends PayloadProvider<T> {

	/**
	 * Default constructor
	 */
	public ThriftJsonPayLoadProvider() {
		super(DasshyMediaType.THRIFT_JSON, new ThriftJsonSerializer<T>());
	}
}
