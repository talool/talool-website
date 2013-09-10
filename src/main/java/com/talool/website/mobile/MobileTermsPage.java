package com.talool.website.mobile;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.corporate.TermsPanel;

public class MobileTermsPage extends MobilePage {

	private static final long serialVersionUID = 1L;

	public MobileTermsPage(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		add(new TermsPanel("content"));
	}
}
