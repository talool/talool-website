package com.talool.website.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.service.TaloolService;
import com.talool.website.panel.AdminMenuPanel;
import com.talool.website.panel.NiceFeedbackPanel;

/**
 * 
 * @author clintz
 * 
 */
public abstract class BasePage extends WebPage
{

	private static final long serialVersionUID = -7463278066879672957L;

	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	protected transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();

	public FeedbackPanel feedback;

	public BasePage()
	{
		super();
		init();
	}

	public BasePage(IModel<?> model)
	{
		super(model);
		init();
	}

	public BasePage(PageParameters parameters)
	{
		super(parameters);
		init();
	}

	private void init()
	{
		add(new AdminMenuPanel("adminMenuPanel").setRenderBodyOnly(true));

		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		add(feedback.setOutputMarkupId(true));
	}

	public FeedbackPanel getFeedback()
	{
		return feedback;
	}

}
