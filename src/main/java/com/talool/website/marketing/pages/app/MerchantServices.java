package com.talool.website.marketing.pages.app;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.panel.MerchantPanel;


public class MerchantServices extends BaseAppPage {

	private static final long serialVersionUID = 1L;
	
	public MerchantServices(PageParameters params) {
		super(params);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new MerchantPanel("benefits"));
	}

}
