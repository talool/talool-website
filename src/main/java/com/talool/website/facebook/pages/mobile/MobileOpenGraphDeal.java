package com.talool.website.facebook.pages.mobile;

import java.util.UUID;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.service.ServiceException;
import com.talool.website.facebook.panel.DealPanel;

public class MobileOpenGraphDeal extends MobileOpenGraphBase {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MobileOpenGraphDeal.class);
	
	private Deal deal = null;

	public MobileOpenGraphDeal(PageParameters parameters) {
		super(parameters);
		
		String dId = parameters.get(0).toString();
		UUID dealId = UUID.fromString(dId);

		try
		{
			deal = taloolService.getDeal(dealId);
		} 
		catch (ServiceException se)
		{
			LOG.error("Failed to get gift for Facebook:",se);
		}
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();	
		add(new DealPanel("object",deal));
	}

}
