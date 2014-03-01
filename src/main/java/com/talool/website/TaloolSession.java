package com.talool.website;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.Request;

import com.talool.core.DealOffer;
import com.talool.core.MerchantAccount;
import com.talool.website.service.BrowserException;

/**
 * 
 * @author clintz
 * 
 */
public class TaloolSession extends WebSession
{
	private static final long serialVersionUID = 5796824961553926305L;
	private DealOffer lastDealOffer;

	private static enum AcceptedBrowserName
	{
		chrome, safari, firefox
	};

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

	public DealOffer getLastDealOffer()
	{
		return lastDealOffer;
	}

	public void setLastDealOffer(DealOffer lastDealOffer)
	{
		this.lastDealOffer = lastDealOffer;
	}

	public static void performBrowserCheck() throws BrowserException
	{
		try
		{
			WebClientInfo info = (WebClientInfo) TaloolSession.get().getClientInfo();
			String ua = info.getUserAgent();

			for (AcceptedBrowserName name : AcceptedBrowserName.values())
			{
				if (StringUtils.containsIgnoreCase(ua, name.toString()))
				{
					return;
				}
			}

			throw new BrowserException();

		}
		catch (NullPointerException e)
		{}
	}

}
