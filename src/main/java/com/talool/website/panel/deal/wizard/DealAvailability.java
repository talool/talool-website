package com.talool.website.panel.deal.wizard;

import java.util.Calendar;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardModel;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.website.component.DealOfferSelect;
import com.talool.website.models.AvailableDealOffersListModel;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.deal.DealPreviewUpdatingBehavior;
import com.talool.website.panel.deal.DealPreviewUpdatingBehavior.DealComponent;

public class DealAvailability extends DynamicWizardStep
{

	private static final long serialVersionUID = 1L;
	private DealOffer dealOffer;
	private DealWizard wizard;
	private boolean addDealOffer = false;

	public DealAvailability(IDynamicWizardStep previousStep, DealWizard wiz)
	{
		super(previousStep, new ResourceModel("title"), new ResourceModel("summary"));
		this.wizard = wiz;
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();

		((DynamicWizardModel) getWizardModel()).setLastVisible(false);

		Deal deal = (Deal) getDefaultModelObject();
		if (deal.getExpires() == null)
		{
			// temp date to help with loading deals
			Calendar c = Calendar.getInstance();
			c.set(2014, 8, 1);
			deal.setExpires(c.getTime());
		}

		final DealPreview dealPreview = new DealPreview("dealBuilder", deal);
		dealPreview.setOutputMarkupId(true);
		addOrReplace(dealPreview);

		AvailableDealOffersListModel listModel = new AvailableDealOffersListModel(deal);
		DealOfferSelect dealOfferSelect = new DealOfferSelect("availableDealOffers", new PropertyModel<DealOffer>(this,
				"dealOffer"), listModel);
		dealOfferSelect.setRequired(true);
		dealOfferSelect.add(new DealPreviewUpdatingBehavior(dealPreview, DealComponent.DEAL_OFFER, "onChange"));
		addOrReplace(dealOfferSelect);

		AjaxLink<Void> newDealOffer = new AjaxLink<Void>("newDealOffer")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				// go back to the previous step
				addDealOffer = true;
				wizard.goBack(target);
			}

		};
		addOrReplace(newDealOffer.setVisible(false)); // wired off for now

		DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);
		DateTextField expires = new DateTextField("expires", converter);
		addOrReplace(expires);
		expires.add(new DealPreviewUpdatingBehavior(dealPreview,
				DealPreviewUpdatingBehavior.DealComponent.EXPIRES, "onChange"));

		addOrReplace(new CheckBox("isActive"));

	}

	public DealOffer getDealOffer()
	{
		Deal deal = (Deal) getDefaultModelObject();
		return deal.getDealOffer();
	}

	public void setDealOffer(DealOffer dealOffer)
	{
		this.dealOffer = dealOffer;
		Deal deal = (Deal) getDefaultModelObject();
		deal.setDealOffer(dealOffer);
	}

	@Override
	public boolean isLastStep()
	{
		return true;
	}

	@Override
	public IDynamicWizardStep next()
	{
		return this;
	}

	@Override
	public IDynamicWizardStep last()
	{
		return new DealSave(this);
	}

	@Override
	public IDynamicWizardStep previous()
	{
		// loop back through the steps to find one that is a CreateDealOffer
		DynamicWizardStep previousStep = (DynamicWizardStep) super.previous();
		while (previousStep != null)
		{
			if (previousStep instanceof CreateDealOffer)
			{
				break;
			}
			else
			{
				previousStep = (DynamicWizardStep) previousStep.previous();
			}
		}

		/*
		 * if there was no CreateDealOffer in the stack, create one.
		 */
		if (previousStep == null)
		{
			previousStep = new CreateDealOffer(super.previous(), wizard);
		}

		return (addDealOffer) ? previousStep : super.previous();
	}

}
