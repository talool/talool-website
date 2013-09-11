package com.talool.website.pages.sales;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.mobile.MobileContactPage;
import com.talool.website.pages.WWWBasePage;
import com.talool.website.panel.sales.ContactPanel;

public class ContactPage extends WWWBasePage
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
