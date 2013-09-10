package com.talool.website.pages.facebook;

import java.util.UUID;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.service.ServiceException;
import com.talool.website.mobile.opengraph.MobileOpenGraphDeal;
import com.talool.website.panel.opengraph.DealPanel;

public class OpenGraphDeal extends OpenGraphRepeator {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(OpenGraphDeal.class);
	
	private Deal deal = null;

	public OpenGraphDeal(PageParameters parameters) {
		super(parameters);
		
		if (isMobile())
		{
			// redirect to mobile web
			throw new RestartResponseException(MobileOpenGraphDeal.class, parameters); 
		}
		
		String dId = parameters.get(0).toString();
		UUID dealId = UUID.fromString(dId);

		try
		{
			deal = taloolService.getDeal(dealId);
			setOgDescription(deal.getSummary());
			setOgTitle(deal.getTitle());
			setOgType("deal");
			setOgImage(deal.getImage().getMediaUrl());
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
	
	@Override
	public String getUrlPath() {
		StringBuilder sb = new StringBuilder("/deal/");
		sb.append(deal.getId());
		return sb.toString();
	}

}
