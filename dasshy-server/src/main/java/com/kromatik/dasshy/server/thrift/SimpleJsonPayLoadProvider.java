package com.kromatik.dasshy.server.thrift;

import org.apache.thrift.TBase;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * Json payload provider
 *
 * @param <T> thrift entity
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SimpleJsonPayLoadProvider<T extends TBase> extends PayloadProvider<T> {

	/**
	 * Default constructor
	 */
	public SimpleJsonPayLoadProvider() {
		super(DasshyMediaType.JSON, new JsonSerializer<T>());
	}
}
