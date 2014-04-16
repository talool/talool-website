package com.talool.website.marketing.pages;

import java.util.UUID;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.pages.mobile.MobilePasswordResetPage;
import com.talool.website.marketing.panel.ResetPasswordPanel;

public class PasswordResetPage extends BaseMarketingPage
{

	private static final long serialVersionUID = 1L;
	private UUID customerId;
	private String passwordResetCode;

	public PasswordResetPage()
	{
		super();
	}

	public PasswordResetPage(PageParameters params)
	{
		super(params);
		if (isMobile())
		{
			throw new RestartResponseException(MobilePasswordResetPage.class, params);
		}
		String cId = params.get(0).toString();
		customerId = UUID.fromString(cId);
		passwordResetCode = params.get(1).toString();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new ResetPasswordPanel("content", customerId, passwordResetCode));
	}

}
