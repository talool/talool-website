package com.talool.website.panel.dealoffer;

import java.text.NumberFormat;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.DealOffer;
import com.talool.website.component.StaticImage;

public class DealOfferPreview extends Panel
{
	private static final NumberFormat priceFormat = NumberFormat.getCurrencyInstance();
	
	public String title, summary, price, dealOfferBackgroundUrl;
	private final String defaultBackgoundUrl = "/img/DealOfferBG.png";
	public Label titleLabel, summaryLabel, priceLabel;
	public StaticImage background;

	private static final long serialVersionUID = 7091914958360426987L;

	public DealOfferPreview(String id, DealOffer offer)
	{
		super(id);

		init(offer);
	}

	public void init(DealOffer offer)
	{
		title = offer.getTitle();
		summary = offer.getSummary();
		
		if (offer.getPrice() == null || offer.getPrice() == 0)
		{
			price = "Price: FREE";
		}
		else
		{
			price = "Price: "+ priceFormat.format(offer.getPrice());
		}

		dealOfferBackgroundUrl = (offer.getDealOfferBackground() == null) ? defaultBackgoundUrl : offer.getDealOfferBackground().getMediaUrl();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(titleLabel = new Label("title", new PropertyModel<String>(this, "title")));
		titleLabel.setOutputMarkupId(true);
		add(summaryLabel = new Label("summary", new PropertyModel<String>(this, "summary")));
		summaryLabel.setOutputMarkupId(true);
		add(priceLabel = new Label("price", new PropertyModel<String>(this, "price")));
		priceLabel.setOutputMarkupId(true);

		add(background = new StaticImage("background", false, new PropertyModel<String>(this, "dealOfferBackgroundUrl")));
		background.setOutputMarkupId(true);
	}

	public void setPrice(String input) 
	{
		double p = 0;
		try {
			p = Double.parseDouble(input);
		}
		catch (Exception e)
		{
			// message the user? or just blank out the field?
		}
		if (p==0)
		{
			price = "Price: FREE";
		}
		else
		{
			price = "Price: "+ priceFormat.format(p);
		}
	}
	

}
