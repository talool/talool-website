package com.talool.website.pages;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.mobile.MobileHomePage;
import com.talool.website.panel.customer.ResetPasswordPanel;

/**
 * @author clintz
 * 
 */
public class WWWBasePage extends WebPage
{
	private static final long serialVersionUID = 8390824646913457971L;

	public WWWBasePage()
	{
		super();
	}

	public WWWBasePage(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		if (isMobile()) 
		{
			// redirect to mobile web (doing it late, so subclasses can redirect in the constructor)
			throw new RestartResponseException(MobileHomePage.class, null);
		}
	}

	protected boolean isMobile()
	{
		final HttpServletRequest request = ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest());
		String ua = request.getHeader("User-Agent");
		
		return (StringUtils.contains(ua, "Android") ||
			StringUtils.contains(ua, "iPhone") ||
			StringUtils.contains(ua, "iPad"));
	}
}
