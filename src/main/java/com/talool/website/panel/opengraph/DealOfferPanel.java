package com.talool.website.panel.opengraph;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.DealOffer;
import com.talool.core.Merchant;
import com.talool.website.component.StaticImage;

@SuppressWarnings("unused")
public class DealOfferPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private String title;
	private String offerSummary;
	private String instructions;
	private String support;
	private boolean showSupport = false;
	
	public DealOfferPanel(String id, DealOffer offer, Merchant fundraiser, String code) {
		super(id);
		
		if (offer != null)
		{
			
			StringBuilder titleSB = new StringBuilder(offer.getTitle());
			title = titleSB.toString();
			
			// check the share type
			StringBuilder instructionsSB = new StringBuilder("Install Talool for iOS or Android to purchase this book.");
			instructions = instructionsSB.toString();
			
			if (code!=null && fundraiser!=null)
			{
				StringBuilder sb = new StringBuilder();
				sb.append("  Use tracking code ")
		          .append(code)
				  .append(" when you purchase this digital book to show your support for ")
				  .append(fundraiser.getName())
				  .append("!");
				support = sb.toString();
				showSupport = true;
			}
			
			offerSummary = offer.getSummary();
		}
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		Label summary = new Label("summary", new PropertyModel<String>(this,"offerSummary"));
		add(summary);
		
		Label how = new Label("how", new PropertyModel<String>(this,"instructions"));
		add(how);
		
		WebMarkupContainer supContainer = new WebMarkupContainer("supportContainer");
		add(supContainer);
		Label support = new Label("support", new PropertyModel<String>(this,"support"));
		supContainer.add(support);
		supContainer.setVisible(showSupport);
		
		Label titleLabel = new Label("titleLabel", new PropertyModel<String>(this,"title"));
		add(titleLabel);
	}

}
