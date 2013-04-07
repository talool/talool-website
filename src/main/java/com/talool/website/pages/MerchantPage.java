package com.talool.website.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.AdminMenuPanel;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantPage extends BasePage
{

	private static final long serialVersionUID = 9023714664854633955L;

	public MerchantPage()
	{
		super();
	}

	public MerchantPage(PageParameters parameters)
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
