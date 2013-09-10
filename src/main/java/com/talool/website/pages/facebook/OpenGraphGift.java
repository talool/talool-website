package com.talool.website.pages.facebook;

import java.util.UUID;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.gift.Gift;
import com.talool.core.service.ServiceException;
import com.talool.website.mobile.opengraph.MobileOpenGraphGift;
import com.talool.website.panel.opengraph.GiftPanel;

public class OpenGraphGift extends OpenGraphRepeator {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(OpenGraphGift.class);
	
	private Gift gift = null;

	public OpenGraphGift(PageParameters parameters) {
		super(parameters);
		
		if (isMobile())
		{
			// redirect to mobile web
			throw new RestartResponseException(MobileOpenGraphGift.class, parameters); 
		}
		
		String gId = parameters.get(0).toString();
		UUID giftId = UUID.fromString(gId);
		try
		{
			gift = customerService.getGift(giftId);
			Deal deal = gift.getDealAcquire().getDeal();
			if (deal != null)
			{
				setOgDescription(deal.getSummary());
				setOgTitle(deal.getTitle());
				setOgType("deal");
				setOgImage(deal.getImage().getMediaUrl());
			}
		} 
		catch (ServiceException se)
		{
			LOG.error("Failed to get gift for Facebook:",se);
		}
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new GiftPanel("object",gift));
	}
	
	@Override
	public String getUrlPath() {
		StringBuilder sb = new StringBuilder("/gift/");
		sb.append(gift.getId());
		return sb.toString();
	}

}
