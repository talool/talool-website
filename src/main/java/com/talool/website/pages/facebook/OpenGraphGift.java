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

public class OpenGraphGift extends OpenGraphRepeator {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(OpenGraphGift.class);
	
	private Gift gift = null;
	private Deal deal = null;
	private Customer customer = null;
	private String merchantName;
	private String instructions;
	private String title;

	@SuppressWarnings("unused")
	public OpenGraphGift(PageParameters parameters) {
		super(parameters);
		
		String gId = parameters.get(0).toString();
		UUID giftId = UUID.fromString(gId);

		try
		{
			gift = customerService.getGift(giftId);
			deal = gift.getDealAcquire().getDeal();
			customer = gift.getFromCustomer();
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
			
			// check the share type
			StringBuilder instructionsSB = new StringBuilder("To accept your deal, install Talool for iOS or Android and");
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
		
		Label how = new Label("how", new PropertyModel<String>(this,"instructions"));
		add(how);
		
		Label titleLabel = new Label("titleLabel", new PropertyModel<String>(this,"title"));
		add(titleLabel);
	}

}
