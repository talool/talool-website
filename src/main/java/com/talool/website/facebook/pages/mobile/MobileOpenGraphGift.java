package com.talool.website.facebook.pages.mobile;

import java.util.UUID;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.gift.Gift;
import com.talool.core.service.ServiceException;
import com.talool.website.facebook.panel.GiftPanel;

public class MobileOpenGraphGift extends MobileOpenGraphBase {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MobileOpenGraphGift.class);
	
	private Gift gift = null;

	public MobileOpenGraphGift(PageParameters parameters) {
		super(parameters);
		
		String gId = parameters.get(0).toString();
		UUID giftId = UUID.fromString(gId);
		try
		{
			gift = customerService.getGift(giftId);
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
		
		StringBuilder deeplinkJS = new StringBuilder();
		deeplinkJS.append("window.ooConfig = {mobile:{deeplink:\"talool://gift/")
		  .append(gift.getId()).append("\"}};");
		add(new Label("deeplink",deeplinkJS.toString()).setEscapeModelStrings(false));
	}

}
