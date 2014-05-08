package com.talool.website.marketing.pages;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.pages.mobile.MobileEmailLandingPage;

public class EmailLandingPage extends BaseMarketingPage
{
	
	private static final long serialVersionUID = -715720113027113956L;
	LandingPagePanelPicker panelPicker;

	public EmailLandingPage()
	{
		super();
		goHome();
	}

	public EmailLandingPage(PageParameters parameters)
	{
		super(parameters);
		panelPicker = new LandingPagePanelPicker(parameters);
	}
	
	private void goHome()
	{
		// redirect to the home page
		throw new RestartResponseException(HomePage.class, null);
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
			goHome();
		}
	}

	@Override
	public void handleMobile() {
		// redirect to mobile web (doing it late, so subclasses can redirect in the constructor)
		throw new RestartResponseException(MobileEmailLandingPage.class, this.parameters);
	}

}
