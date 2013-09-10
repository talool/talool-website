package com.talool.website.panel.opengraph;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.DealOffer;
import com.talool.website.component.StaticImage;

@SuppressWarnings("unused")
public class DealOfferPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private String merchantName;
	private String title;
	private String offerSummary;
	private String imageUrl;
	
	public DealOfferPanel(String id, DealOffer offer) {
		super(id);
		
		if (offer != null)
		{
			merchantName = offer.getMerchant().getName();
			
			StringBuilder titleSB = new StringBuilder(offer.getTitle());
			title = titleSB.toString();
			
			imageUrl = offer.getMerchant().getCurrentLocation().getMerchantImage().getMediaUrl();
			offerSummary = offer.getSummary();
		}
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		StaticImage image = new StaticImage("merchantImage", false, new PropertyModel<String>(this, "imageUrl"));
		add(image);
		
		Label summary = new Label("summary", new PropertyModel<String>(this,"offerSummary"));
		add(summary);
		
		Label merchantName = new Label("merchantName", new PropertyModel<String>(this,"merchantName"));
		add(merchantName);
		
		Label titleLabel = new Label("titleLabel", new PropertyModel<String>(this,"title"));
		add(titleLabel);
	}

}
