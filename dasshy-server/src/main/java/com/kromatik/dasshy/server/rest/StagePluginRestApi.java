package com.kromatik.dasshy.server.rest;

import com.kromatik.dasshy.server.service.StagePluginService;

import javax.ws.rs.Path;

/**
 * API for managing stage plugins
 */
@Path("/plugins")
public class StagePluginRestApi extends AbstractRestApi
{
	/** plugins service */
	private final StagePluginService		service;

	/**
	 * Default constructor
	 *
	 * @param service stage plugins service
	 */
	public StagePluginRestApi(final StagePluginService service)
	{
		this.service = service;
	}
}
