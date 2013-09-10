package com.talool.website.pages.facebook;

import java.util.UUID;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.service.ServiceException;
import com.talool.website.mobile.opengraph.MobileOpenGraphDealOffer;
import com.talool.website.panel.opengraph.DealOfferPanel;

public class OpenGraphDealOffer extends OpenGraphRepeator {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(OpenGraphDealOffer.class);
	
	private DealOffer offer = null;

	public OpenGraphDealOffer(PageParameters parameters) {
		super(parameters);
		
		if (isMobile())
		{
			// redirect to mobile web
			throw new RestartResponseException(MobileOpenGraphDealOffer.class, parameters); 
		}
		
		String doId = parameters.get(0).toString();
		UUID dealOfferId = UUID.fromString(doId);

		try
		{
			offer = taloolService.getDealOffer(dealOfferId);
			setOgDescription(offer.getSummary());
			setOgTitle(offer.getTitle());
			setOgType("deal_pack");
			setOgImage(offer.getMerchant().getCurrentLocation().getMerchantImage().getMediaUrl());
		} 
		catch (ServiceException se)
		{
			LOG.error("Failed to get offer for Facebook:",se);
		}

	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new DealOfferPanel("object",offer));
	}

	@Override
	public String getUrlPath() {
		StringBuilder sb = new StringBuilder("/offer/");
		sb.append(offer.getId());
		return sb.toString();
	}
	
	

}
