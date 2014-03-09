package com.talool.website;

import java.util.TimeZone;

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

	private WebClientInfo webClientInfo;

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

	public TimeZone getTimeZone()
	{
		TimeZone timeZone = null;

		if (webClientInfo.getProperties().getTimeZone() != null)
		{
			timeZone = webClientInfo.getProperties().getTimeZone();
		}
		// fallback to server's timezone if we can't determine the client's
		if (timeZone == null)
		{
			timeZone = TimeZone.getDefault();
		}
		return timeZone;
	}

	public void performBrowserCheck() throws BrowserException
	{
		WebClientInfo _wClientInfo = webClientInfo = (WebClientInfo) TaloolSession.get().getClientInfo();

		try
		{
			webClientInfo = _wClientInfo;
			String ua = webClientInfo.getUserAgent();

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
