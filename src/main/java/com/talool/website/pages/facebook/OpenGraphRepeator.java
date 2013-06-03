package com.talool.website.pages.facebook;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenGraphRepeator extends WebPage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(OpenGraphRepeator.class);

	public OpenGraphRepeator(PageParameters parameters)
	{
		super(parameters);
		
		String ogUrl = RequestCycle.get().getUrlRenderer().renderFullUrl(Url.parse(urlFor(OpenGraphRepeator.class, 
				parameters).toString()));
		WebMarkupContainer url = new WebMarkupContainer("url");
		url.add(new AttributeModifier("content",ogUrl));
		add(url);
		
		String app_id = parameters.get("fb:app_id").toString();
		WebMarkupContainer appid = new WebMarkupContainer("app_id");
		appid.add(new AttributeModifier("content",app_id));
		add(appid);
		
		String ogDescription = parameters.get("og:description").toString();
		WebMarkupContainer desc = new WebMarkupContainer("description");
		desc.add(new AttributeModifier("content",ogDescription));
		add(desc);
		
		String ogType = parameters.get("og:type").toString();
		WebMarkupContainer tp = new WebMarkupContainer("type");
		tp.add(new AttributeModifier("content",ogType));
		add(tp);
		
		String ogImage = parameters.get("og:image").toString();
		WebMarkupContainer img = new WebMarkupContainer("image");
		img.add(new AttributeModifier("content",ogImage));
		add(img);
		
		String ogTitle = parameters.get("og:title").toString();
		WebMarkupContainer title = new WebMarkupContainer("title");
		title.add(new AttributeModifier("content",ogTitle));
		add(title);
		
		String body = parameters.get("body").toString();
		add(new Label("body",body));
	}
	
}
