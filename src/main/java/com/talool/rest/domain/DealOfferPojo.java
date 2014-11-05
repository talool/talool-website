package com.talool.rest.domain;

import java.util.UUID;

import com.talool.core.DealOffer;

public class DealOfferPojo {
	private String title;
	private String summary;
	private String backgroundImage;
	private String logo;
	private String icon;
	private String merchant;
	private float price;
	private UUID id;
	private long startDate;
	private long endDate;
	
	public DealOfferPojo(DealOffer offer)
	{
		this.title = offer.getTitle();
		this.summary = offer.getTitle();
		this.price = offer.getPrice();
		this.id = offer.getId();
		
		if (offer.getDealOfferBackground() != null)
		{
			this.backgroundImage = offer.getDealOfferBackground().getMediaUrl();
		}
		if (offer.getDealOfferIcon() != null)
		{
			this.icon = offer.getDealOfferIcon().getMediaUrl();
		}
		if (offer.getDealOfferLogo() != null)
		{
			this.logo = offer.getDealOfferLogo().getMediaUrl();
		}
		if (offer.getMerchant() != null)
		{
			this.merchant = offer.getMerchant().getName();
		}
		if (offer.getScheduledStartDate() != null)
		{
			this.startDate = offer.getScheduledStartDate().getTime();
		}
		if (offer.getScheduledEndDate() != null)
		{
			this.endDate = offer.getScheduledEndDate().getTime();
		}
	}

	public String getTitle() {
		return title;
	}

	public String getSummary() {
		return summary;
	}

	public float getPrice() {
		return price;
	}

	public UUID getId() {
		return id;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public String getLogo() {
		return logo;
	}

	public String getIcon() {
		return icon;
	}

	public String getMerchant() {
		return merchant;
	}

	public long getStartDate() {
		return startDate;
	}

	public long getEndDate() {
		return endDate;
	}
	
	
}
