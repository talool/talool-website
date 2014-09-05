package com.talool.website.marketing.pages;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.pages.mobile.MobileHomePage;

/**
 * @author clintz
 * 
 */
public class BaseMarketingPage extends WebPage
{
	private static final long serialVersionUID = 8390824646913457971L;

	protected PageParameters parameters;
	
	public BaseMarketingPage()
	{
		super();
	}

	public BaseMarketingPage(PageParameters parameters)
	{
		super(parameters);
		this.parameters = parameters;
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		if (isMobile()) 
		{
			handleMobile();
		}
	}

	public boolean isMobile()
	{
		final HttpServletRequest request = ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest());
		String ua = request.getHeader("User-Agent");
		
		return (StringUtils.contains(ua, "Android") ||
			StringUtils.contains(ua, "iPhone") ||
			StringUtils.contains(ua, "iPad"));
	}
	
	public void handleMobile()
	{
		// redirect to mobile web (doing it late, so subclasses can redirect in the constructor)
		throw new RestartResponseException(MobileHomePage.class, null);
	}
}
