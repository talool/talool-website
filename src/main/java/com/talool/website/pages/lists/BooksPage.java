package com.talool.website.pages.lists;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminMenuPanel;

public class BooksPage extends BasePage {
	
	private static final long serialVersionUID = 4465255303084700956L;

	public BooksPage()
	{
		super();
	}

	public BooksPage(PageParameters parameters)
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
