package com.talool.website.marketing.pages;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.pages.mobile.MobileContactPage;
import com.talool.website.marketing.panel.ContactPanel;

public class ContactPage extends BaseMarketingPage
{
	private static final long serialVersionUID = 8390824646913457971L;

	public ContactPage()
	{
		super();
		init();
	}

	public ContactPage(PageParameters parameters)
	{
		super(parameters);
		init();
	}
	
	public void init()
	{
		if (isMobile())
		{
			throw new RestartResponseException(MobileContactPage.class); 
		}
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		add(new ContactPanel("content"));
	}
}
