package com.talool.website.pages;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.mobile.MobileHomePage;

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
		init();
	}

	public WWWBasePage(PageParameters parameters)
	{
		super(parameters);
		init();
	}
	
	private void init()
	{
		final HttpServletRequest request = ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest());
		String ua = request.getHeader("User-Agent");
		
		if (StringUtils.contains(ua, "Android") ||
			StringUtils.contains(ua, "iPhone") ||
			StringUtils.contains(ua, "iPad")) 
		{
			// redirect to mobile web
			throw new RestartResponseException(MobileHomePage.class, null); 
			// TODO map requests to a corresponding mobile page, rather than sending all requests to the mobile home page
		}
	}


}
