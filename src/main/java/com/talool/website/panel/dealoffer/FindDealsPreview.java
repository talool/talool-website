package com.talool.website.panel.dealoffer;

import org.apache.wicket.model.PropertyModel;

import com.talool.core.DealOffer;
import com.talool.website.component.StaticImage;

public class FindDealsPreview extends DealOfferPreview
{
	public String dealOfferIconUrl;
	private final String defaultIconUrl = "/img/DealOfferIcon.png";
	public StaticImage icon;

	private static final long serialVersionUID = 7091914958360426987L;

	public FindDealsPreview(String id, DealOffer offer)
	{
		super(id, offer);
	}

	public void init(DealOffer offer)
	{
		super.init(offer);
		
		dealOfferIconUrl = (offer.getDealOfferIcon() == null) ? defaultIconUrl : offer.getDealOfferIcon().getMediaUrl();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(icon = new StaticImage("icon", false, new PropertyModel<String>(this, "dealOfferIconUrl")));
		icon.setOutputMarkupId(true);
	}

}
