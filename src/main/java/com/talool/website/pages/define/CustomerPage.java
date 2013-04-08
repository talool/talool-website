package com.talool.website.pages.define;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminMenuPanel;

public class CustomerPage extends BasePage
{

	private static final long serialVersionUID = -8869102153066052410L;

	public CustomerPage()
	{
		super();

	}

	public CustomerPage(PageParameters parameters)
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