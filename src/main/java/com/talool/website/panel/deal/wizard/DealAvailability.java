package com.talool.website.panel.deal.wizard;

import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.website.component.DealOfferSelect;
import com.talool.website.models.AvailableDealOffersListModel;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.deal.DealPreviewUpdatingBehavior;

public class DealAvailability extends WizardStep {
	
	private static final long serialVersionUID = 1L;
	private DealOffer dealOffer;
	
	public DealAvailability()
    {
		super(new ResourceModel("title"), new ResourceModel("summary"));
    }
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		Deal deal = (Deal) getDefaultModelObject();
		
		final DealPreview dealPreview = new DealPreview("dealBuilder",deal);
		dealPreview.setOutputMarkupId(true);
		addOrReplace(dealPreview);
		

		addOrReplace(new DealOfferSelect("availableDealOffers", new PropertyModel<DealOffer>(this,
				"dealOffer"), new AvailableDealOffersListModel()).setRequired(true));

		// TODO add a dynamic step for the new deal offer panel
		
		DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);
		DateTextField expires = new DateTextField("expires", converter);
		addOrReplace(expires);
		expires.add(new DealPreviewUpdatingBehavior(dealPreview,
				DealPreviewUpdatingBehavior.DealComponent.EXPIRES, "onChange"));

		addOrReplace(new CheckBox("isActive"));
		
		// TODO we need a validator on this
		TextField<String> codeField = new TextField<String>("code");
		codeField.add(new DealPreviewUpdatingBehavior(dealPreview,
				DealPreviewUpdatingBehavior.DealComponent.CODE, "onBlur"));
		addOrReplace(codeField);
		
	}
	
	public DealOffer getDealOffer() {
		Deal deal = (Deal) getDefaultModelObject();
		return deal.getDealOffer();
	}

	public void setDealOffer(DealOffer dealOffer) {
		this.dealOffer = dealOffer;
		Deal deal = (Deal) getDefaultModelObject();
		deal.setDealOffer(dealOffer);
	}
	
}
