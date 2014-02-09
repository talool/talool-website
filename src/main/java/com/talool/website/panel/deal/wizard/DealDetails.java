package com.talool.website.panel.deal.wizard;

import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.Deal;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.deal.definition.template.DealTemplatePanel;
import com.talool.website.panel.deal.definition.template.OtherDealTemplatePanel;

public class DealDetails extends DynamicWizardStep
{

	private static final long serialVersionUID = 1L;
	private final IDynamicWizardStep nextStep;

	public DealDetails(DealWizard wiz)
	{
		super(null, new ResourceModel("title"), new ResourceModel("summary"));
		this.nextStep = new DealImage(this, wiz);
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();

		final Deal deal = (Deal) getDefaultModelObject();

		final DealPreview dealPreview = new DealPreview("dealBuilder", deal);
		dealPreview.setOutputMarkupId(true);
		addOrReplace(dealPreview);

		DealTemplatePanel dTemplate = new OtherDealTemplatePanel("templateSelectPanel", dealPreview, deal.getTitle(), deal.getSummary());
		dTemplate.setDefaultModel(getDefaultModel());
		addOrReplace(dTemplate);
		
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
