package com.talool.website.pages.corporate;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.mobile.MobilePrivacyPage;
import com.talool.website.pages.WWWBasePage;
import com.talool.website.panel.corporate.PrivacyPanel;


public class WWWPrivacyPolicy extends WWWBasePage {

	private static final long serialVersionUID = 1L;
	
	public WWWPrivacyPolicy()
	{
		super();
		init();
	}
	
	public WWWPrivacyPolicy(PageParameters params) {
		super(params);
		init();
	}
	
	public void init()
	{
		if (isMobile())
		{
			throw new RestartResponseException(MobilePrivacyPage.class); 
		}
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new PrivacyPanel("privacy"));
	}

}
