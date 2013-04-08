package com.talool.website.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.AdminMenuPanel;

public class NewCustomerPage extends BasePage
{

	private static final long serialVersionUID = -8869102153066052410L;

	public NewCustomerPage()
	{
		super();

	}

	public NewCustomerPage(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new AdminMenuPanel("adminMenuPanel").setRenderBodyOnly(true));
	}

}