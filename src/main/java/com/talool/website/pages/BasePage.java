package com.talool.website.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.service.TaloolService;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author clintz
 * 
 */
public abstract class BasePage extends WebPage
{
	private static final long serialVersionUID = -7463278066879672957L;

	protected transient static final TaloolService taloolService = ServiceFactory.get()
			.getTaloolService();

	public BasePage()
	{
		super();
	}

	public BasePage(IModel<?> model)
	{
		super(model);
	}

	public BasePage(PageParameters parameters)
	{
		super(parameters);
	}

}
