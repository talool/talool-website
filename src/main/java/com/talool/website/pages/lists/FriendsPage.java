package com.talool.website.pages.lists;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminMenuPanel;

public class FriendsPage extends BasePage {
	
	private static final long serialVersionUID = 3634980968241854373L;

	public FriendsPage()
	{
		super();
	}

	public FriendsPage(PageParameters parameters)
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