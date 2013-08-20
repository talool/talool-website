package com.talool.website.pages.corporate;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.sales.CustomerPanel;


public class ConsumerServices extends BaseCorporatePage {

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
