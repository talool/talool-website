package com.talool.website.panel.dealoffer.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.DealOffer;
import com.talool.core.MediaType;
import com.talool.core.MerchantMedia;
import com.talool.website.component.DealTypeDropDownChoice;
import com.talool.website.component.MerchantMediaWizardPanel;
import com.talool.website.panel.dealoffer.DealOfferPreview;
import com.talool.website.panel.dealoffer.DealOfferPreviewUpdatingBehavior;
import com.talool.website.panel.dealoffer.DealOfferPreviewUpdatingBehavior.DealOfferComponent;

public class DealOfferDetails extends WizardStep
{

	private static final long serialVersionUID = 1L;
	private MerchantMedia background;

	public DealOfferDetails()
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
		
		TextField<String> title = new TextField<String>("title");
		addOrReplace(title.setRequired(true));
		title.add(new DealOfferPreviewUpdatingBehavior(offerPreview, DealOfferComponent.TITLE, "onChange"));
		
		TextArea<String> summary = new TextArea<String>("summary");
		addOrReplace(summary.setRequired(true));
		summary.add(new DealOfferPreviewUpdatingBehavior(offerPreview, DealOfferComponent.SUMMARY, "onChange"));
		
		TextField<String> price = new TextField<String>("price");
		addOrReplace(price.setRequired(true));
		price.add(new DealOfferPreviewUpdatingBehavior(offerPreview, DealOfferComponent.PRICE, "onChange"));

		DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);
		addOrReplace(new DateTextField("expires", converter));

		addOrReplace(new CheckBox("isActive"));
		
		
		background = offer.getDealOfferLogo();
		
		PropertyModel<MerchantMedia> selectedMediaModel = new PropertyModel<MerchantMedia>(this,"background");
		MerchantMediaWizardPanel mediaPanel = 
				new MerchantMediaWizardPanel("dealOfferBackgound", offer.getMerchant().getId(), MediaType.DEAL_OFFER_BACKGROUND_IMAGE, selectedMediaModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media) {
				// re-init the preview
				offerPreview.init(offer);
				target.add(offerPreview);
			}
			
		};
		addOrReplace(mediaPanel);
		mediaPanel.getMediaSelect()
			.add(new DealOfferPreviewUpdatingBehavior(offerPreview, DealOfferComponent.BACKGROUND, "onChange"));
			
		
		
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
