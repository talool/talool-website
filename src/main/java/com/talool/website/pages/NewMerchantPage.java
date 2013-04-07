package com.talool.website.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.AdminMenuPanel;

/**
 * 
 * @author clintz
 * 
 */
public class NewMerchantPage extends BasePage
{

	private static final long serialVersionUID = -7718256037209979704L;

	public NewMerchantPage()
	{
		super();
	}

	public NewMerchantPage(PageParameters parameters)
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
