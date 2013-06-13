package com.talool.website.pages.facebook;

import java.util.UUID;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Customer;
import com.talool.core.Deal;
import com.talool.core.gift.EmailGift;
import com.talool.core.gift.FaceBookGift;
import com.talool.core.gift.Gift;
import com.talool.core.service.ServiceException;
import com.talool.website.component.StaticImage;

public class OpenGraphDeal extends OpenGraphRepeator {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(OpenGraphDeal.class);
	
	private Deal deal = null;
	private String merchantName;
	private String title;

	@SuppressWarnings("unused")
	public OpenGraphDeal(PageParameters parameters) {
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
		
		if (deal != null)
		{
			setOgDescription(deal.getSummary());
			setOgTitle(deal.getTitle());
			setOgType("deal");
			setOgImage(deal.getImage().getMediaUrl());
			
			merchantName = deal.getMerchant().getName();
			
			StringBuilder titleSB = new StringBuilder(deal.getTitle());
			title = titleSB.toString();
		}
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		StaticImage image = new StaticImage("dealImage", false, new PropertyModel<String>(this, "ogImage"));
		add(image);
		
		Label summary = new Label("dealSummary", new PropertyModel<String>(this,"ogDescription"));
		add(summary);
		
		Label merchantName = new Label("merchantName", new PropertyModel<String>(this,"merchantName"));
		add(merchantName);
		
		Label titleLabel = new Label("titleLabel", new PropertyModel<String>(this,"title"));
		add(titleLabel);
	}
	
	@Override
	public String getUrlPath() {
		StringBuilder sb = new StringBuilder("/deal/");
		sb.append(deal.getId());
		return sb.toString();
	}

}
