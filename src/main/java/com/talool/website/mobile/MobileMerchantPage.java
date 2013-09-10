package com.talool.website.mobile;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.sales.MerchantPanel;

public class MobileMerchantPage extends MobilePage {

	private static final long serialVersionUID = 1L;

	public MobileMerchantPage(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		add(new MerchantPanel("content"));
	}
}
