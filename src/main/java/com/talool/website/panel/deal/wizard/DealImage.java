package com.talool.website.panel.deal.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.Deal;
import com.talool.core.MediaType;
import com.talool.core.MerchantMedia;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.image.selection.MediaSelectionPanel;

public class DealImage extends DynamicWizardStep
{

	private static final long serialVersionUID = 1L;
	private MerchantMedia image;
	private final IDynamicWizardStep nextStep;

	public DealImage(IDynamicWizardStep previousStep, DealWizard wiz)
	{
		super(previousStep, new ResourceModel("title"), new ResourceModel("summary"));
		this.nextStep = new DealTags(this, wiz);
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();

		final Deal deal = (Deal) getDefaultModelObject();

		final DealPreview dealPreview = new DealPreview("dealBuilder", deal);
		dealPreview.setOutputMarkupId(true);
		addOrReplace(dealPreview);
		
		image = deal.getImage();
		PropertyModel<MerchantMedia> selectedMediaModel = new PropertyModel<MerchantMedia>(this,"image");
		MediaSelectionPanel imagePanel = new MediaSelectionPanel("dealImage", deal.getMerchant().getId(), MediaType.DEAL_IMAGE, selectedMediaModel)
		{

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onMediaPicked(AjaxRequestTarget target, MerchantMedia media) {
				dealPreview.init(deal);
				target.add(dealPreview);
			}
			
		};
		addOrReplace(imagePanel);
		
	}

	public MerchantMedia getImage()
	{
		final Deal deal = (Deal) getDefaultModelObject();
		return deal.getImage();
	}

	public void setImage(final MerchantMedia image)
	{
		this.image = image;
		Deal deal = (Deal) getDefaultModelObject();
		deal.setImage(image);
	}

	@Override
	public boolean isLastStep()
	{
		return false;
	}

	@Override
	public IDynamicWizardStep next()
	{
		return nextStep;
	}

	@Override
	public IDynamicWizardStep last()
	{
		return new DealSave(this);
	}

}
