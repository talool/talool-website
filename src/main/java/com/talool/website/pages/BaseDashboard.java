package com.talool.website.pages;

import java.util.UUID;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.service.TaloolService;


public class BaseDashboard extends WebPage {

	private static final long serialVersionUID = 1L;
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	protected transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();

	public FeedbackPanel feedback;
	
	protected UUID merchantId;

	public BaseDashboard()
	{
		super();
		
		// debugging
		merchantId =UUID.fromString("01f6be1e-322d-4710-9baa-c6ba1374dd38");
		
		init();
	}
	
	public BaseDashboard(PageParameters parameters)
	{
		super(parameters);
		
		String id = parameters.get("id").toString();
		merchantId = UUID.fromString(id);
		
		init();
	}
	
	public void init()
	{
		
	}
	
	
	public FeedbackPanel getFeedback()
	{
		return feedback;
	}
}
