package com.talool.website.panel.merchant.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.Deal;
import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.MediaType;
import com.talool.core.Merchant;
import com.talool.core.MerchantMedia;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.image.selection.MediaSelectionPanel;
import com.talool.website.util.SessionUtils;

public class MerchantLocationLogo extends WizardStep
{

	private static final long serialVersionUID = 1L;
	private MerchantMedia selectedLogo;
	
	private transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();

	public MerchantLocationLogo()
	{
		super(new ResourceModel("title"), new ResourceModel("summary"));

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onConfigure()
	{
		super.onConfigure();

		setDefaultModel(new CompoundPropertyModel<Merchant>((IModel<Merchant>) getDefaultModel()));

		final Merchant merchant = (Merchant) getDefaultModelObject();
		final Deal dummyDeal = getDummyDeal(merchant);
		final DealPreview dealPreview = new DealPreview("dealBuilder", dummyDeal);
		dealPreview.setOutputMarkupId(true);
		addOrReplace(dealPreview);

		selectedLogo = merchant.getCurrentLocation().getLogo();
		PropertyModel<MerchantMedia> selectedLogoModel = new PropertyModel<MerchantMedia>(this,"selectedLogo");

		MediaSelectionPanel imagePanel = new MediaSelectionPanel("merchantMediaLogo", merchant.getId(), MediaType.MERCHANT_LOGO, selectedLogoModel)
		{

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onMediaPicked(AjaxRequestTarget target, MerchantMedia media) {
				dummyDeal.getMerchant().getCurrentLocation().setLogo(media);
				dealPreview.init(dummyDeal);
				target.add(dealPreview);
			}
			
		};
		addOrReplace(imagePanel);
			
	}
	
	private Deal getDummyDeal(Merchant merchant)
	{
		Deal deal = domainFactory.newDeal(merchant.getId(), SessionUtils.getSession().getMerchantAccount(), true);
		deal.setMerchant(merchant);
		deal.setTitle("Buy 1, Get 1 Free");
		deal.setSummary("Buy 1 sample, get a second of equal or lesser value free.");
		deal.setDetails("Not valid on holidays.");
		deal.setImage(merchant.getCurrentLocation().getMerchantImage());
		return deal;
	}

	
	/*
	 * Save the state of the Merchant.
	 */
	@Override
	public void applyState()
	{
		super.applyState();

		final Merchant merch = (Merchant) getDefaultModelObject();
		
		// get the selected MerchantMedia and add it to the location
		if (selectedLogo != null)
		{
			merch.getCurrentLocation().setLogo(selectedLogo);
		}
			
	}

}
