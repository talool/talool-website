package com.talool.website.panel.merchant;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.talool.website.component.StaticImage;

public class MerchantPreview extends Panel
{
	public String merchantImageUrl;
	public String merchantName;
	private String defaultImageUrl = "/img/missing.jpg";
	public StaticImage image;

	private static final long serialVersionUID = 1L;

	public MerchantPreview(String id, Merchant merchant)
	{
		super(id);

		init(merchant);
	}

	public void init(Merchant merchant)
	{

		MerchantLocation loc = merchant.getCurrentLocation();
		
		if (loc.getMerchantImage() != null)
		{
			merchantImageUrl = loc.getMerchantImage().getMediaUrl();
		}
		else
		{
			merchantImageUrl = defaultImageUrl;
		}
		merchantName = merchant.getName();

	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new Label("merchantName", new PropertyModel<String>(this, "merchantName")));
		add(image = new StaticImage("image", false, new PropertyModel<String>(this, "merchantImageUrl")));
		image.setOutputMarkupId(true);

	}

}
