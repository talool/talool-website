package com.talool.website.pages.lists;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminMenuPanel;

public class DealsPage extends BasePage {

	private static final long serialVersionUID = 1133795226226645331L;

	public DealsPage()
	{
		super();
	}

	public DealsPage(PageParameters parameters)
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
