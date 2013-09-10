package com.talool.website.panel.opengraph;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Customer;
import com.talool.core.Deal;
import com.talool.core.gift.EmailGift;
import com.talool.core.gift.FaceBookGift;
import com.talool.core.gift.Gift;
import com.talool.website.component.StaticImage;

@SuppressWarnings("unused")
public class DealPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private String merchantName;
	private String instructions;
	private String title;
	private String dealSummary;
	private String imageUrl;
	
	public DealPanel(String id, Deal deal) {
		super(id);
		
		if (deal != null)
		{
			merchantName = deal.getMerchant().getName();
			
			StringBuilder titleSB = new StringBuilder(deal.getTitle());
			title = titleSB.toString();
			
			imageUrl = deal.getImage().getMediaUrl();
			dealSummary = deal.getSummary();
		}
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		StaticImage image = new StaticImage("dealImage", false, new PropertyModel<String>(this, "imageUrl"));
		add(image);
		
		Label summary = new Label("dealSummary", new PropertyModel<String>(this,"dealSummary"));
		add(summary);
		
		Label merchantName = new Label("merchantName", new PropertyModel<String>(this,"merchantName"));
		add(merchantName);
		
		Label titleLabel = new Label("titleLabel", new PropertyModel<String>(this,"title"));
		add(titleLabel);
	}

}
