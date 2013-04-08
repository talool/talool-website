package com.talool.website.pages.define;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminMenuPanel;

public class DealPage extends BasePage
{

	private static final long serialVersionUID = -8170770093265696680L;

	public DealPage()
	{
		super();

	}

	public DealPage(PageParameters parameters)
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
