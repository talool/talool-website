package com.talool.website.mobile;

import java.util.UUID;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.customer.ResetPasswordPanel;

public class MobilePasswordResetPage extends MobilePage {

	private static final long serialVersionUID = 1L;
	private UUID customerId;

	public MobilePasswordResetPage(PageParameters parameters)
	{
		super(parameters);
		String cId = parameters.get(0).toString();
		customerId = UUID.fromString(cId);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();	
		add(new ResetPasswordPanel("content", customerId));
	}
}
