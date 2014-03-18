package com.talool.website.mobile.opengraph;

import java.util.UUID;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.website.panel.opengraph.DealOfferPanel;

public class MobileOpenGraphDealOffer extends MobileOpenGraphBase {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MobileOpenGraphDealOffer.class);
	
	private DealOffer offer = null;
	private Merchant fundraiser = null;
	private String code = null;

	public MobileOpenGraphDealOffer(PageParameters parameters) {
		super(parameters);
		
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

}
