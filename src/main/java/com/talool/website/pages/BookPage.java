package com.talool.website.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.AdminMenuPanel;

public class BookPage extends BasePage {
	
	private static final long serialVersionUID = 4465255303084700956L;

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
