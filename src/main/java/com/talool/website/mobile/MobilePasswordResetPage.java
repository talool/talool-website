package com.talool.website.mobile;

import java.util.UUID;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.customer.ResetPasswordPanel;

public class MobilePasswordResetPage extends MobilePage
{

	private static final long serialVersionUID = 1L;
	private UUID customerId;
	private String passwordResetCode;

	public MobilePasswordResetPage(PageParameters parameters)
	{
		super(parameters);
		customerId = UUID.fromString(parameters.get(0).toString());
		passwordResetCode = parameters.get(1).toString();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new ResetPasswordPanel("content", customerId, passwordResetCode));
	}
}
