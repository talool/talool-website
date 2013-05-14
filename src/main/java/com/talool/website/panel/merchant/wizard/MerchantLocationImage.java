package com.talool.website.panel.merchant.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.MediaType;
import com.talool.core.Merchant;
import com.talool.core.MerchantMedia;
import com.talool.website.component.MerchantMediaWizardPanel;
import com.talool.website.panel.merchant.MerchantPreview;
import com.talool.website.panel.merchant.MerchantPreviewUpdatingBehavior;
import com.talool.website.panel.merchant.MerchantPreviewUpdatingBehavior.MerchantComponent;

public class MerchantLocationImage extends WizardStep
{

	private static final long serialVersionUID = 1L;
	private MerchantMedia selectedImage;

	public MerchantLocationImage()
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
		
		final MerchantPreview merchantPerview = new MerchantPreview("merchantBuilder", merchant);
		merchantPerview.setOutputMarkupId(true);
		addOrReplace(merchantPerview);

		selectedImage = merchant.getCurrentLocation().getMerchantImage();
		PropertyModel<MerchantMedia> selectedImageModel = new PropertyModel<MerchantMedia>(this,"selectedImage");
		MerchantMediaWizardPanel imagePanel = new MerchantMediaWizardPanel("merchantMediaImage", merchant.getId(), MediaType.MERCHANT_IMAGE, selectedImageModel)
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media) {
				selectedImage = media;
				merchant.getCurrentLocation().setMerchantImage(media);
				merchantPerview.init(merchant);
				target.add(merchantPerview);
			}
			
		};
		addOrReplace(imagePanel);
		imagePanel.getMediaSelect()
			.add(new MerchantPreviewUpdatingBehavior(merchantPerview, MerchantComponent.IMAGE, "onChange"));
		

	}

	/*
	 * Save the state of the Merchant.
	 */
	@Override
	public void applyState()
	{
		super.applyState();

		final Merchant merch = (Merchant) getDefaultModelObject();

		if (selectedImage != null)
		{
			merch.getCurrentLocation().setMerchantImage(selectedImage);
		}
		
	}

}
