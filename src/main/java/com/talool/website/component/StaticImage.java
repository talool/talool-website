package com.talool.website.component;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Static image component used for setting <img src>
 * 
 * Supports cache busting
 * 
 * @author clintz
 * 
 */
public class StaticImage extends WebComponent
{
	private static final long serialVersionUID = -7600332611457262341L;
	private boolean cacheBust = false;

	public StaticImage(String id, boolean cacheBust)
	{
		super(id);
		this.cacheBust = cacheBust;
	}

	public StaticImage(String id, boolean cacheBust, IModel<String> model)
	{
		super(id, model);
		this.cacheBust = cacheBust;
	}

	public StaticImage(String id, boolean cacheBust, String url)
	{
		this(id, cacheBust, new Model<String>(url));
	}

	protected void onComponentTag(ComponentTag tag)
	{
		checkComponentTag(tag, "img");

		String url = getDefaultModelObjectAsString();
		if (cacheBust)
		{
			url = url + "?" + System.currentTimeMillis();
		}
		tag.put("src", url);
	}

	@Override
	public boolean isVisible()
	{
		return StringUtils.isNotEmpty(getDefaultModelObjectAsString());
	}
}