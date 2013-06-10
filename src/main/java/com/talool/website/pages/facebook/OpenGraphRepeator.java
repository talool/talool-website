package com.talool.website.pages.facebook;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.service.CustomerService;
import com.talool.core.service.TaloolService;

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
	
	private String ogUrl;
	private String ogDescription;
	private String ogType;
	private String ogImage;
	private String ogTitle;

	public OpenGraphRepeator(PageParameters parameters)
	{
		super(parameters);
		
		ogUrl = RequestCycle.get().getUrlRenderer().renderFullUrl(Url.parse(urlFor(OpenGraphRepeator.class, 
				parameters).toString()));
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();

		WebMarkupContainer url = new WebMarkupContainer("url");
		url.add(new AttributeModifier("content",ogUrl));
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
	
}
