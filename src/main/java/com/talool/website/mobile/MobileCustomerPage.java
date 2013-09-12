package com.talool.website.mobile;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.sales.CustomerPanel;

public class MobileCustomerPage extends MobilePage {

	private static final long serialVersionUID = 1L;

	public MobileCustomerPage(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		add(new CustomerPanel("content"));
	}
}