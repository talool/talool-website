package com.talool.website.facebook.pages;

import java.util.UUID;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.domain.Properties;
import com.talool.utils.KeyValue;
import com.talool.website.facebook.pages.mobile.MobileOpenGraphDealOffer;
import com.talool.website.facebook.panel.DealOfferPanel;

public class OpenGraphDealOffer extends OpenGraphRepeator {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(OpenGraphDealOffer.class);
	
	private DealOffer offer = null;
	private Merchant fundraiser = null;
	private String code = null;

	public OpenGraphDealOffer(PageParameters parameters) {
		super(parameters);
		
		if (isMobile())
		{
			// redirect to mobile web
			throw new RestartResponseException(MobileOpenGraphDealOffer.class, parameters); 
		}
		
		String doId = parameters.get(0).toString();
		UUID dealOfferId = UUID.fromString(doId);
		
		// fundraising tracking code from purchase of book
		StringValue c = parameters.get(1);
		if (c != null)
		{
			code = c.toString();
			if (code != null)
			{
				try
				{
					fundraiser = taloolService.getFundraiserByTrackingCode(code);
				}
				catch (ServiceException se)
				{
					LOG.error("Failed to get Fundraiser for code:",se);
				}
			}
		}

		try
		{
			
			
			offer = taloolService.getDealOffer(dealOfferId);
			Properties props = offer.getProperties();
			if (props.getAsBool(KeyValue.fundraisingBook) && fundraiser != null)
			{
				
				// create a custom title and description for the fundraiser
				setOgTitle(offer.getTitle());
				
				StringBuilder description = new StringBuilder("I purchased the ");
				description.append(offer.getTitle())
						   .append(" to support ")
				           .append(fundraiser.getName())
				           .append(".  Use tracking code ")
				           .append(code)
						   .append(" when you purchase to show your support too!");
				setOgDescription(description.toString());
				
			}
			else
			{
				setOgDescription(offer.getSummary());
				setOgTitle(offer.getTitle());
			}
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
		add(new DealOfferPanel("object",offer, fundraiser, code));
	}

	@Override
	public String getUrlPath() {
		StringBuilder sb = new StringBuilder("/offer/");
		sb.append(offer.getId());
		if (fundraiser != null & code != null)
		{
			sb.append("/").append(code);
		}
		return sb.toString();
	}
	
	

}
