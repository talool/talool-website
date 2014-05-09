package com.talool.website.marketing.pages.mobile;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.pages.LandingPagePanelPicker;

public class MobileEmailLandingPage extends MobilePage {

	private static final long serialVersionUID = 1L;
	LandingPagePanelPicker panelPicker;
	WebMarkupContainer buttons;

	public MobileEmailLandingPage(PageParameters parameters)
	{
		super(parameters);
		panelPicker = new LandingPagePanelPicker(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		buttons = new WebMarkupContainer("buttons");
		add(buttons.setOutputMarkupId(true));
		
		try
		{
			add(panelPicker.getPanel("content"));
			
			if (!panelPicker.showFaq())
			{
				buttons.add(new AttributeAppender("class"," hideFaq"));
				
			}
			// TODO track it in graphite
		}
		catch(Exception e)
		{
			// redirect to the home page
			throw new RestartResponseException(MobileHomePage.class, null);
		}
	}
}
