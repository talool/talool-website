package com.talool.website.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.AdminMenuPanel;

public class UsersPage extends BasePage {
	
	private static final long serialVersionUID = 2102415289760762365L;

	public UsersPage()
	{
		super();
	}

	public UsersPage(PageParameters parameters)
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
