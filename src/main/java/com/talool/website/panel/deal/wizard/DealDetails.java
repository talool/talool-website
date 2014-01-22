package com.talool.website.panel.deal.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.Deal;
import com.talool.core.MediaType;
import com.talool.core.MerchantMedia;
import com.talool.website.component.MerchantMediaWizardPanel;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.deal.DealPreviewUpdatingBehavior;
import com.talool.website.panel.deal.DealPreviewUpdatingBehavior.DealComponent;
import com.talool.website.panel.deal.definition.template.DealTemplatePanel;
import com.talool.website.panel.deal.definition.template.DealTemplateSelectPanel;
import com.talool.website.panel.deal.definition.template.OtherDealTemplatePanel;

public class DealDetails extends DynamicWizardStep
{

	private static final long serialVersionUID = 1L;
	private MerchantMedia image;
	private final IDynamicWizardStep nextStep;

	public DealDetails(DealWizard wiz)
	{
		super(null, new ResourceModel("title"), new ResourceModel("summary"));
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
		MerchantMediaWizardPanel mediaPanel = 
				new MerchantMediaWizardPanel("dealImage", deal.getMerchant().getId(), MediaType.DEAL_IMAGE, selectedMediaModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media) {
				// re-init the preview
				dealPreview.init(deal);
				target.add(dealPreview);
			}
			
		};
		mediaPanel.getMediaSelect()
			.add(new DealPreviewUpdatingBehavior(dealPreview, DealComponent.IMAGE, "onChange"));
		addOrReplace(mediaPanel);

		DealTemplatePanel dTemplate = new OtherDealTemplatePanel("templateSelectPanel", dealPreview, deal.getTitle(), deal.getSummary());
		dTemplate.setDefaultModel(getDefaultModel());
		addOrReplace(dTemplate);
		
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
