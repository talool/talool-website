package com.talool.website.facebook.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Customer;
import com.talool.core.Deal;
import com.talool.core.MerchantLocation;
import com.talool.core.gift.EmailGift;
import com.talool.core.gift.FaceBookGift;
import com.talool.core.gift.Gift;
import com.talool.website.component.StaticImage;

@SuppressWarnings("unused")
public class LocationPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private String title;
	private String imageUrl;
	
	public LocationPanel(String id, MerchantLocation location) {
		super(id);
		
		if (location != null)
		{

			StringBuilder titleSB = new StringBuilder(location.getMerchant().getName());
			title = titleSB.toString();
			
			imageUrl = location.getMerchantImage().getMediaUrl();
		}
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		StaticImage image = new StaticImage("merchImage", false, new PropertyModel<String>(this, "imageUrl"));
		add(image);

		Label titleLabel = new Label("titleLabel", new PropertyModel<String>(this, "title"));
		add(titleLabel);
	}

}
