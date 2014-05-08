package com.talool.website.marketing.pages.mobile;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.pages.LandingPagePanelPicker;

public class MobileEmailLandingPage extends MobilePage {

	private static final long serialVersionUID = 1L;
	LandingPagePanelPicker panelPicker;

	public MobileEmailLandingPage(PageParameters parameters)
	{
		super(parameters);
		panelPicker = new LandingPagePanelPicker(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		try
		{
			add(panelPicker.getPanel("content"));
			// TODO track it in graphite
		}
		catch(Exception e)
		{
			// redirect to the home page
			throw new RestartResponseException(MobileHomePage.class, null);
		}
	}
}
