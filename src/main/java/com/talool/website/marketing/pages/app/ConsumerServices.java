package com.talool.website.marketing.pages.app;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.panel.CustomerPanel;


public class ConsumerServices extends BaseAppPage {

	public ConsumerServices(PageParameters params) {
		super(params);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new CustomerPanel("benefits"));
	}

}
