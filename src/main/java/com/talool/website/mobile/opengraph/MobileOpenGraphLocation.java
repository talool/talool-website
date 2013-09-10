package com.talool.website.mobile.opengraph;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.MerchantLocation;
import com.talool.core.service.ServiceException;
import com.talool.website.panel.opengraph.LocationPanel;

public class MobileOpenGraphLocation extends MobileOpenGraphBase {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MobileOpenGraphLocation.class);
	
	private MerchantLocation location = null;

	public MobileOpenGraphLocation(PageParameters parameters) {
		super(parameters);
		
		long locId = parameters.get(0).toLong();

		try
		{
			location = taloolService.getMerchantLocationById(locId);
		}
		catch (ServiceException se)
		{
			LOG.error("Failed to get gift for Facebook:", se);
		}
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new LocationPanel("object",location));
	}

}
