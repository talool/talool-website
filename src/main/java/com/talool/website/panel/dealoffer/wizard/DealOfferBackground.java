package com.talool.website.panel.dealoffer.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.DealOffer;
import com.talool.core.MediaType;
import com.talool.core.MerchantMedia;
import com.talool.website.panel.dealoffer.DealOfferPreview;
import com.talool.website.panel.image.selection.MediaSelectionPanel;

public class DealOfferBackground extends WizardStep
{

	private static final long serialVersionUID = 1L;
	private MerchantMedia background;

	public DealOfferBackground()
	{
		super(new ResourceModel("title"), new ResourceModel("summary"));
	}
	
	@Override
	protected void onConfigure()
	{
		super.onConfigure();

		final DealOffer offer = (DealOffer) getDefaultModelObject();
		
		final DealOfferPreview offerPreview = new DealOfferPreview("offerBuilder", offer);
		offerPreview.setOutputMarkupId(true);
		addOrReplace(offerPreview);
		
		background = offer.getDealOfferLogo();
		
		PropertyModel<MerchantMedia> selectedMediaModel = new PropertyModel<MerchantMedia>(this,"background");
			
		MediaSelectionPanel imagePanel = new MediaSelectionPanel("dealOfferBackgound", offer.getMerchant().getId(), MediaType.DEAL_OFFER_BACKGROUND_IMAGE, selectedMediaModel)
		{

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onMediaPicked(AjaxRequestTarget target, MerchantMedia media) {
				offerPreview.init(offer);
				target.add(offerPreview);
			}
			
		};
		addOrReplace(imagePanel);
		
		
	}

	public MerchantMedia getBackground()
	{
		final DealOffer offer = (DealOffer) getDefaultModelObject();
		return offer.getDealOfferBackground();
	}

	public void setBackground(final MerchantMedia image)
	{
		this.background = image;
		DealOffer offer = (DealOffer) getDefaultModelObject();
		offer.setDealOfferBackground(image);
	}

}
