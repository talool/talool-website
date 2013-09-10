package com.talool.website.mobile;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.corporate.TermsPanel;


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
		
		add(new TermsPanel("content"));
	}
}
