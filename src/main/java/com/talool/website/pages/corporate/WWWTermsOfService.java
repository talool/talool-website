package com.talool.website.pages.corporate;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.mobile.MobileTermsPage;
import com.talool.website.pages.WWWBasePage;
import com.talool.website.panel.corporate.TermsPanel;


public class WWWTermsOfService extends WWWBasePage {

	private static final long serialVersionUID = 1L;
	
	public WWWTermsOfService()
	{
		super();
		init();
	}
	
	public WWWTermsOfService(PageParameters params) {
		super(params);
		init();
	}
	
	public void init()
	{
		if (isMobile())
		{
			throw new RestartResponseException(MobileTermsPage.class); 
		}
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new TermsPanel("terms"));
	}

}
