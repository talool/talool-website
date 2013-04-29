package com.talool.website.panel.deal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Deal;
import com.talool.website.component.StaticImage;

public class DealPreview extends Panel
{
	private static final String NEVER_EXPIRES = "Never";

	public String title, summary, details, code, imageUrl;
	private String merchantLogoUrl, dealOfferLogoUrl;
	private String defaultImageUrl = "http://i567.photobucket.com/albums/ss116/alphabetabeta/bg_test.png";
	private String defaultDealOfferLogoUrl = "http://i1328.photobucket.com/albums/w525/talooltools/paybackbook_logo_zps2ee39bdc.png";
	public String expires;
	public Label titleLabel, summaryLabel, detailsLabel;
	public StaticImage image, merchantLogo, dealOfferLogo;
	public DateLabel expiresLabel;
	
	// Temporary list of logos.  Should be tied to the merchant.
	private List<String> logos = new ArrayList<String>();
	private int logoIdx = 0;

	private static final long serialVersionUID = 7091914958360426987L;

	public DealPreview(String id, Deal deal)
	{
		super(id);
		if (deal.getId() != null)
		{
			title = deal.getTitle();
			summary = deal.getSummary();
			details = deal.geDetails();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			if (deal.getExpires() != null)
			{
				expires = formatter.format(deal.getExpires());
			}
			else
			{
				expires = NEVER_EXPIRES;
			}

			code = deal.getCode();
			if (StringUtils.isNotEmpty(deal.getImageUrl()))
			{
				imageUrl = deal.getImageUrl();
			}
			else
			{
				imageUrl = defaultImageUrl;
			}
		}
		else
		{
			imageUrl = defaultImageUrl;
		}
		
		this.logos.add("http://i1328.photobucket.com/albums/w525/talooltools/Parma_logo_zpsd7363952.png");
		this.logos.add("http://i1328.photobucket.com/albums/w525/talooltools/La_Revolucion_logo_zpsf2bd7958.png");
		this.logos.add("http://i1328.photobucket.com/albums/w525/talooltools/Jovie_logo_zps35c45fe9.png");
		merchantLogoUrl = this.logos.get(0);
		dealOfferLogoUrl = defaultDealOfferLogoUrl;

	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(titleLabel = new Label("title", new PropertyModel<String>(this, "title")));
		titleLabel.setOutputMarkupId(true);
		add(summaryLabel = new Label("summary", new PropertyModel<String>(this, "summary")));
		summaryLabel.setOutputMarkupId(true);
		add(detailsLabel = new Label("details", new PropertyModel<String>(this, "details")));
		detailsLabel.setOutputMarkupId(true);

		DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);
		add(expiresLabel = new DateLabel("expires", new PropertyModel<Date>(this, "expires"), converter));
		expiresLabel.setOutputMarkupId(true);

		// add(codeLabel = new Label("code", new
		// PropertyModel<String>(this,"title")));
		// codeLabel.setOutputMarkupId(true);

		add(image = new StaticImage("image", false, new PropertyModel<String>(this, "imageUrl")));
		image.setOutputMarkupId(true);
		
		add(merchantLogo = new StaticImage("merchantLogo", false, new PropertyModel<String>(this, "merchantLogoUrl")));
		merchantLogo.setOutputMarkupId(true);
		
		add(dealOfferLogo = new StaticImage("dealOfferLogo", false, new PropertyModel<String>(this, "dealOfferLogoUrl")));
		dealOfferLogo.setOutputMarkupId(true);
	}
	
	public String getMerchantLogoUrl() {
		return merchantLogoUrl;
	}

	public void setMerchantLogoUrl(String merchantLogoUrl) {
		logoIdx++;
		if (logoIdx >= logos.size()) logoIdx=0;
		this.merchantLogoUrl = logos.get(logoIdx);
	}

	public String getDealOfferLogoUrl() {
		return dealOfferLogoUrl;
	}

	public void setDealOfferLogoUrl(String dealOfferLogoUrl) {
		this.dealOfferLogoUrl = dealOfferLogoUrl;
	}
	
}
