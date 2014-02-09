package com.talool.website.panel.dealoffer.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.MediaType;
import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.talool.core.MerchantMedia;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.image.selection.MediaSelectionPanel;
import com.talool.website.util.SessionUtils;

public class DealOfferLogo extends WizardStep
{

	private static final long serialVersionUID = 1L;
	private MerchantMedia logo;

	private transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();
	
	public DealOfferLogo()
	{
		super(new ResourceModel("title"), new ResourceModel("summary"));
	}
	
	@Override
	protected void onConfigure()
	{
		super.onConfigure();

		final DealOffer offer = (DealOffer) getDefaultModelObject();
		final Deal dummyDeal = getDummyDeal();
		final DealPreview dealPreview = new DealPreview("dealBuilder", dummyDeal);
		dealPreview.setOutputMarkupId(true);
		addOrReplace(dealPreview);
		
		logo = offer.getDealOfferLogo();
		PropertyModel<MerchantMedia> iconModel = new PropertyModel<MerchantMedia>(this, "logo");
		
		MediaSelectionPanel imagePanel = new MediaSelectionPanel("dealOfferLogo", offer.getMerchant().getId(), MediaType.DEAL_OFFER_LOGO, iconModel)
		{

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onMediaPicked(AjaxRequestTarget target, MerchantMedia media) {
				dummyDeal.getDealOffer().setDealOfferLogo(media);
				dealPreview.init(dummyDeal);
				target.add(dealPreview);
			}
			
		};
		addOrReplace(imagePanel);
	}
	
	private Deal getDummyDeal()
	{
		Deal deal = domainFactory.newDeal(null, SessionUtils.getSession().getMerchantAccount(), true);
		deal.setMerchant(getDummyMerchant());
		deal.setTitle("Buy 1, Get 1 Free");
		deal.setSummary("Buy 1 Widget, get a Second Widget of Equal or Lesser Value Free.");
		deal.setDetails("May not be combined with any other offer, discount or promotion. Not valid on holidays, and subject to rules of use.");
		
		MerchantMedia dealImage = domainFactory.newMedia(null, "/img/dummyMerchantImage.png", MediaType.DEAL_IMAGE);
		deal.setImage(dealImage);
		
		return deal;
	}
	
	private Merchant getDummyMerchant()
	{
		Merchant merchant = domainFactory.newMerchant(true);
		merchant.setName("Sample Merchant");
		
		MerchantLocation location = domainFactory.newMerchantLocation();
		MerchantMedia merchantLogo = domainFactory.newMedia(null, "/img/dummyMerchantLogo.png", MediaType.MERCHANT_LOGO);
		location.setLogo(merchantLogo);
		merchant.setCurrentLocation(location);
		
		return merchant;
	}
	
	public MerchantMedia getLogo()
	{
		final DealOffer offer = (DealOffer) getDefaultModelObject();
		return offer.getDealOfferLogo();
	}

	public void setLogo(final MerchantMedia image)
	{
		this.logo = image;
		DealOffer offer = (DealOffer) getDefaultModelObject();
		offer.setDealOfferLogo(image);
	}
}
