package com.talool.website.panel.message.wizard;

import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.DealOffer;
import com.talool.website.component.DealOfferSelect;
import com.talool.website.models.DealOfferListModel;
import com.talool.website.panel.message.MessageJobPojo;

public class MessageBookStep extends DynamicWizardStep
{

	private static final long serialVersionUID = 1976804266839904494L;

	public MessageBookStep(IDynamicWizardStep previousStep)
	{
		super(previousStep, new ResourceModel("title"), new ResourceModel("summary"));
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();
		
		final MessageJobPojo mg = (MessageJobPojo) getDefaultModelObject();

		// select book
		DealOfferListModel listModel = new DealOfferListModel();
		listModel.setExcludeKirke(true);
		listModel.setMerchantId(mg.getMerchant().getId());
		DealOfferSelect dealOfferSelect = new DealOfferSelect("availableDealOffers", new PropertyModel<DealOffer>(mg,
				"offer"), listModel);
		dealOfferSelect.setRequired(true);
		addOrReplace(dealOfferSelect);
		
		// enter a message
		TextArea<String> message = new TextArea<String>("message", new PropertyModel<String>(mg,"message"));
		message.setRequired(true);
		add(message);
		
	}

	@Override
	public boolean isLastStep()
	{
		return false;
	}

	@Override
	public IDynamicWizardStep next() {
		return new MessageCriteriaStep(this);
	}

	@Override
	public IDynamicWizardStep last()
	{
		return new MessageConfirmationStep(this);
	}

}
