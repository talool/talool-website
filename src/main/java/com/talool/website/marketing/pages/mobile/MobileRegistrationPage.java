package com.talool.website.marketing.pages.mobile;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.panel.RegistrationPanel;

public class MobileRegistrationPage extends MobilePage {

	private static final long serialVersionUID = 1L;
	private boolean isFundraiser;
	private boolean isPublisher;

	public MobileRegistrationPage()
	{
		super();
		isFundraiser = false;
		isPublisher = false;
	}
	
	public MobileRegistrationPage(PageParameters parameters)
	{
		super(parameters);
		int flag = parameters.get(0).toInt();
		isFundraiser = flag==1;
		isPublisher = flag==2;
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new RegistrationPanel("content", isFundraiser, isPublisher));
	}
}
