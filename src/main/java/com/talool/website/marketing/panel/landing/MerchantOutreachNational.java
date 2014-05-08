package com.talool.website.marketing.panel.landing;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class MerchantOutreachNational extends Panel {

	private static final long serialVersionUID = 1L;
	private int maxPercentage;

	public MerchantOutreachNational(String id, int percentage) {
		super(id);
		maxPercentage = percentage;
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		add(new Label("percentage",maxPercentage));
	}

}
