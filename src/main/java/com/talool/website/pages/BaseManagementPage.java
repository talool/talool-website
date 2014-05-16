package com.talool.website.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * 
 * @author dmccuen
 * 
 */
abstract public class BaseManagementPage extends BasePage
{

	private static final long serialVersionUID = -7456940218372349185L;

	public BaseManagementPage(PageParameters parameters)
	{
		super(parameters);
	}
	
	public BaseManagementPage()
	{
		super();
	}
	
	
}
