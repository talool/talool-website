package com.talool.website.panel.dashboard;

import java.util.UUID;

import org.apache.wicket.markup.html.panel.Panel;

public class MerchantReachPanel extends Panel {

	private static final long serialVersionUID = 1L;
	protected UUID merchantId;
	
	public MerchantReachPanel(String id, UUID merchantId)
	{
		super(id);
		this.merchantId = merchantId;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		
	}
	
	
	
}
