package com.talool.website.pages.sales;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.pages.WWWBasePage;
import com.talool.website.panel.sales.ContactPanel;

public class ContactPage extends WWWBasePage
{
	private static final long serialVersionUID = 8390824646913457971L;

	public ContactPage()
	{
		super();

	}

	public ContactPage(PageParameters parameters)
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
