package com.talool.website.panel.deal.wizard;

import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardModel;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.core.FactoryManager;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.component.DealOfferSelect;
import com.talool.website.models.AvailableDealOffersListModel;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.deal.DealPreviewUpdatingBehavior;

public class DealAvailability extends DynamicWizardStep {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DealAvailability.class);
	private DealOffer dealOffer;
	
	private transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	
	public DealAvailability(IDynamicWizardStep previousStep)
    {
		super(previousStep, new ResourceModel("title"), new ResourceModel("summary"));
    }
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		((DynamicWizardModel)getWizardModel()).setLastVisible(false);
		
		Deal deal = (Deal) getDefaultModelObject();
		if (deal.getId() != null)
		{
			try
			{
				// TODO merge didn't work here
				taloolService.refresh(deal);
			}
			catch (ServiceException se)
			{
				LOG.error("There was an exception merging the deal: ", se);
			}
		}
		
		final DealPreview dealPreview = new DealPreview("dealBuilder",deal);
		dealPreview.setOutputMarkupId(true);
		addOrReplace(dealPreview);
		
		addOrReplace(new DealOfferSelect("availableDealOffers", new PropertyModel<DealOffer>(this,
				"dealOffer"), new AvailableDealOffersListModel()).setRequired(true));

		/*
		 *  TODO add a dynamic step for the new deal offer panel
		 *  but need to make sure it's previous step is DealTags
		 */
		
		DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);
		DateTextField expires = new DateTextField("expires", converter);
		addOrReplace(expires);
		expires.add(new DealPreviewUpdatingBehavior(dealPreview,
				DealPreviewUpdatingBehavior.DealComponent.EXPIRES, "onChange"));

		addOrReplace(new CheckBox("isActive"));
		
		// TODO we need a validator for strings that feed a type 39 bar code
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

	@Override
	public boolean isLastStep() {
		return true;
	}

	@Override
	public IDynamicWizardStep next() {
		return this;
	}
	
}
