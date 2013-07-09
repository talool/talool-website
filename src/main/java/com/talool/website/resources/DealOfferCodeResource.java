package com.talool.website.resources;

import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.IResourceStream;

/**
 * 
 * @author clintz
 * 
 */
public class DealOfferCodeResource extends ResourceStreamRequestHandler
{
	private static final long serialVersionUID = -6938694334670137741L;

	public DealOfferCodeResource(IResourceStream resourceStream, String fileName)
	{
		super(resourceStream, fileName);
	}

}
