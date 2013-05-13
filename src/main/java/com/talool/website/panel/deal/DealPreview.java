package com.talool.website.panel.deal;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.core.MerchantLocation;
import com.talool.website.component.StaticImage;

public class DealPreview extends Panel
{
	private static final String NEVER_EXPIRES = "Never";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	public String title, summary, details, code, imageUrl;
	public String merchantLogoUrl, dealOfferLogoUrl;
	private String defaultImageUrl = "http://i567.photobucket.com/albums/ss116/alphabetabeta/bg_test.png";
	private String defaultDealOfferLogoUrl = "http://i1328.photobucket.com/albums/w525/talooltools/paybackbook_logo_zps2ee39bdc.png";
	public String expires;
	public Label titleLabel, summaryLabel, detailsLabel;
	public StaticImage image, merchantLogo, dealOfferLogo;
	public DateLabel expiresLabel;

	private static final long serialVersionUID = 7091914958360426987L;

	public DealPreview(String id, Deal deal)
	{
		super(id);


		init(deal);
	}

	public void init(Deal deal)
	{
		title = deal.getTitle();
		summary = deal.getSummary();
		details = deal.geDetails();

		if (deal.getExpires() != null)
		{
			expires = dateFormat.format(deal.getExpires());
		}
		else
		{
			expires = NEVER_EXPIRES;
		}

		code = deal.getCode();
		if (deal.getImage() != null && StringUtils.isNotEmpty(deal.getImage().getMediaUrl()))
		{
			imageUrl = deal.getImage().getMediaUrl();
		}
		else
		{
			imageUrl = defaultImageUrl;
		}

		MerchantLocation loc = deal.getMerchant().getCurrentLocation();
		merchantLogoUrl = (loc.getLogo() == null) ? null:loc.getLogo().getMediaUrl();
		
		DealOffer offer = deal.getDealOffer();
		dealOfferLogoUrl = (offer.getImage()==null)?defaultDealOfferLogoUrl:offer.getImage().getMediaUrl();
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

}
