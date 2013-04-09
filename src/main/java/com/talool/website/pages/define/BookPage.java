package com.talool.website.pages.define;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminMenuPanel;

public class BookPage extends BasePage
{

	private static final long serialVersionUID = 8003537582321440596L;

	public BookPage()
	{
		super();

	}

	public BookPage(PageParameters parameters)
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