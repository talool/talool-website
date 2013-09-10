package com.talool.website.pages.facebook;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.service.CustomerService;
import com.talool.core.service.TaloolService;
import com.talool.website.mobile.MobileHomePage;

public abstract class OpenGraphRepeator extends WebPage {

	private static final long serialVersionUID = 1L;
	private static final String appId = "342739092494152";
	private static final String appNamespace = "taloolclient";
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	
	protected transient static final CustomerService customerService = FactoryManager.get()
			.getServiceFactory().getCustomerService();

	protected transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();
	
	private String ogDescription;
	private String ogType;
	private String ogImage;
	private String ogTitle;

	public OpenGraphRepeator(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();

		WebMarkupContainer url = new WebMarkupContainer("url");
		url.add(new AttributeModifier("content",getUrl()));
		add(url);
		
		WebMarkupContainer appid = new WebMarkupContainer("app_id");
		appid.add(new AttributeModifier("content",appId));
		add(appid);
		
		WebMarkupContainer desc = new WebMarkupContainer("description");
		desc.add(new AttributeModifier("content",ogDescription));
		add(desc);
		
		WebMarkupContainer tp = new WebMarkupContainer("type");
		tp.add(new AttributeModifier("content",ogType));
		add(tp);
		
		WebMarkupContainer img = new WebMarkupContainer("image");
		img.add(new AttributeModifier("content",ogImage));
		add(img);
		
		WebMarkupContainer title = new WebMarkupContainer("title");
		title.add(new AttributeModifier("content",ogTitle));
		add(title);

	}

	public void setOgDescription(String ogDescription) {
		this.ogDescription = ogDescription;
	}

	public void setOgType(String ogType) {
		StringBuilder sb = new StringBuilder(appNamespace);
		sb.append(":").append(ogType);
		this.ogType = sb.toString();
	}

	public void setOgImage(String ogImage) {
		this.ogImage = ogImage;
	}

	public void setOgTitle(String ogTitle) {
		this.ogTitle = ogTitle;
	}
	
	public String getUrl()
	{
		StringBuilder sb = new StringBuilder(getHost());
		sb.append(getUrlPath());
		return sb.toString();
	}
	
	abstract public String getUrlPath();
	
	public String getHost()
	{
		Request request = getRequest(); //RequestCycle.get().getRequest();
		Url url = request.getClientUrl();
		String hostName = url.getHost();
		int port = url.getPort();
		String protocol = url.getProtocol();
		StringBuilder sb = new StringBuilder(protocol);
		sb.append("://").append(hostName);
		if (port > 80) sb.append(":").append(port);
		
		return sb.toString();
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
