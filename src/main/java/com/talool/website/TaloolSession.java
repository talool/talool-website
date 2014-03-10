package com.talool.website;

import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.service.BrowserException;

/**
 * 
 * @author clintz
 * 
 */
public class TaloolSession extends WebSession
{
	private static final Logger LOG = LoggerFactory.getLogger(TaloolSession.class);
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

	public TimeZone getBestGuessTimeZone()
	{
		final String userStoredTimeZone = getMerchantAccount().getProperties().getAsString("time.zone");

		if ((StringUtils.isNotEmpty(userStoredTimeZone)))
		{
			setAndPersistTimeZone(userStoredTimeZone);
			return TimeZone.getTimeZone(userStoredTimeZone);
		}

		TimeZone timeZone = null;

		setTimeZoneFromPersistent();

		WebClientInfo webClientInfo = getClientInfo();

		if (webClientInfo == null || webClientInfo.getProperties() == null)
		{
			return TimeZone.getDefault();
		}

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

	public void setAndPersistTimeZone(final String timeZoneId)
	{
		try
		{
			getMerchantAccount().getProperties().createOrReplace("time.zone", timeZoneId);
			ServiceFactory.get().getTaloolService().merge(getMerchantAccount());
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Changed timezone for " + getMerchantAccount().getEmail() + " to " + timeZoneId);
			}

		}
		catch (ServiceException e)
		{
			e.printStackTrace();
		}

		WebClientInfo webClientInfo = getClientInfo();

		String tz = getMerchantAccount().getProperties().getAsString("time.zone");
		if (webClientInfo != null && webClientInfo.getProperties() != null && tz != null)
		{
			webClientInfo.getProperties().setTimeZone(TimeZone.getTimeZone(timeZoneId));
		}

	}

	private void setTimeZoneFromPersistent()
	{
		WebClientInfo webClientInfo = getClientInfo();

		if (StringUtils.isNotEmpty(getMerchantAccount().getProperties().getAsString("time.zone")))
		{
			if (webClientInfo != null && webClientInfo.getProperties() != null)
			{
				webClientInfo.getProperties().setTimeZone(TimeZone.getTimeZone(getMerchantAccount().getProperties().getAsString("time.zone")));
			}

		}
	}

	public void performBrowserCheck() throws BrowserException
	{
		WebClientInfo webClientInfo = getClientInfo();

		if (webClientInfo == null)
		{
			return;
		}

		try
		{
			setTimeZoneFromPersistent();

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
