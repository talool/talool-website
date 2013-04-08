package com.talool.website.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.AdminMenuPanel;

public class NewBookPage extends BasePage
{

	private static final long serialVersionUID = 8003537582321440596L;

	public NewBookPage()
	{
		super();

	}

	public NewBookPage(PageParameters parameters)
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