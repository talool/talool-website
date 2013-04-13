package com.talool.website;

import org.apache.wicket.Session;
import org.apache.wicket.core.request.ClientInfo;
import org.apache.wicket.request.Request;

import com.talool.core.MerchantAccount;

/**
 * 
 * @author clintz
 * 
 */
public class TaloolSession extends Session
{
	private static final long serialVersionUID = 5796824961553926305L;

	@Override
	public ClientInfo getClientInfo()
	{
		return null;
	}

	public MerchantAccount getMerchantAccount()
	{
		return merchantAccount;
	}

	public void setMerchantAccount(MerchantAccount merchantAccount)
	{
		this.merchantAccount = merchantAccount;
	}

	private MerchantAccount merchantAccount;

	public TaloolSession(Request request)
	{
		super(request);
	}

	public boolean isSignedOn()
	{
		return merchantAccount == null ? false : true;
	}

}
