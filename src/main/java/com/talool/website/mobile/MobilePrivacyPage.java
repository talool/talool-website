package com.talool.website.mobile;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.corporate.PrivacyPanel;

public class MobilePrivacyPage extends MobilePage {

	private static final long serialVersionUID = 1L;

	public MobilePrivacyPage(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		add(new PrivacyPanel("content"));
	}
}
