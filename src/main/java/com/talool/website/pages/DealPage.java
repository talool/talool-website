package com.talool.website.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.AdminMenuPanel;

public class DealPage extends BasePage {

	private static final long serialVersionUID = 1133795226226645331L;

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
