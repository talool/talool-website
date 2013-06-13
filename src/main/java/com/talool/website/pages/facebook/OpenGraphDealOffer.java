package com.talool.website.pages.facebook;

import java.util.UUID;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.service.ServiceException;
import com.talool.website.component.StaticImage;

public class OpenGraphDealOffer extends OpenGraphRepeator {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(OpenGraphDealOffer.class);
	
	private DealOffer offer = null;
	private String merchantName;
	private String title;

	@SuppressWarnings("unused")
	public OpenGraphDealOffer(PageParameters parameters) {
		super(parameters);
		
		String doId = parameters.get(0).toString();
		UUID dealOfferId = UUID.fromString(doId);

		try
		{
			offer = taloolService.getDealOffer(dealOfferId);
		} 
		catch (ServiceException se)
		{
			LOG.error("Failed to get offer for Facebook:",se);
		}
		
		if (offer != null)
		{
			setOgDescription(offer.getSummary());
			setOgTitle(offer.getTitle());
			setOgType("deal_pack");
			setOgImage(offer.getMerchant().getCurrentLocation().getMerchantImage().getMediaUrl());
			
			merchantName = offer.getMerchant().getName();
			
			StringBuilder titleSB = new StringBuilder(offer.getTitle());
			title = titleSB.toString();
		}
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		StaticImage image = new StaticImage("merchantImage", false, new PropertyModel<String>(this, "ogImage"));
		add(image);
		
		Label summary = new Label("summary", new PropertyModel<String>(this,"ogDescription"));
		add(summary);
		
		Label merchantName = new Label("merchantName", new PropertyModel<String>(this,"merchantName"));
		add(merchantName);
		
		Label titleLabel = new Label("titleLabel", new PropertyModel<String>(this,"title"));
		add(titleLabel);
	}

	@Override
	public String getUrlPath() {
		StringBuilder sb = new StringBuilder("/offer/");
		sb.append(offer.getId());
		return sb.toString();
	}
	
	

}
