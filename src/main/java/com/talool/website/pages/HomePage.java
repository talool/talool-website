package com.talool.website.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.sales.HomepagePanel;

/**
 * @author clintz
 * 
 */
public class HomePage extends WWWBasePage
{
	private static final long serialVersionUID = 8390824646913457971L;

	public HomePage()
	{
		super();

	}

	public HomePage(PageParameters parameters)
	{
		super(parameters);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new HomepagePanel("benefits"));
	}
}
