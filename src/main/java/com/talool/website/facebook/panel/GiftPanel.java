package com.talool.website.facebook.panel;

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
public class GiftPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private String merchantName;
	private String instructions;
	private String title;
	private String dealSummary;
	private String imageUrl;
	
	public GiftPanel(String id, Gift gift) {
		super(id);
		
		Deal deal = gift.getDealAcquire().getDeal();
		Customer customer = gift.getFromCustomer();
		
		if (deal != null && customer != null)
		{
			merchantName = deal.getMerchant().getName();
			
			// check the share type
			StringBuilder instructionsSB = new StringBuilder("To accept your gift, install Talool for iOS or Android and");
			if (gift instanceof FaceBookGift)
			{
				instructionsSB.append(" login with Facebook.");
			}
			else
			{
				instructionsSB.append(" register with this email: ").append( ((EmailGift)gift).getToEmail());
			}
			instructions = instructionsSB.toString();
			
			StringBuilder titleSB = new StringBuilder(customer.getFirstName());
			titleSB.append(" ").append(customer.getLastName()).append(" has sent you a gift for ").append(deal.getTitle());
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
		
		Label how = new Label("how", new PropertyModel<String>(this,"instructions"));
		add(how);
		
		Label titleLabel = new Label("titleLabel", new PropertyModel<String>(this,"title"));
		add(titleLabel);
	}

}
