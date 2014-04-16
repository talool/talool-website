package com.talool.website.marketing.pages.mobile;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.panel.ContactPanel;


public class MobileContactPage extends MobilePage {

	private static final long serialVersionUID = 1L;

	public MobileContactPage(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		add(new ContactPanel("content"));
	}
}
