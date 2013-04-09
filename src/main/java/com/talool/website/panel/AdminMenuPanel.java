package com.talool.website.panel;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.pages.lists.BooksPage;

/**
 * 
 * @author clintz
 * 
 */
public class AdminMenuPanel extends Panel
{

	private static final long serialVersionUID = -8704038043657157579L;

	public AdminMenuPanel(String id)
	{
		super(id);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		PageParameters booksParams = new PageParameters();
		booksParams.set("method", BooksPage.METHOD_ALL);
		BookmarkablePageLink<Void> booksLink = new BookmarkablePageLink<Void>("booksLink",BooksPage.class,booksParams);
		add(booksLink);
	}

}
