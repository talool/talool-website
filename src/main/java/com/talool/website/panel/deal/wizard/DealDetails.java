package com.talool.website.panel.deal.wizard;

import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.Deal;
import com.talool.website.panel.deal.definition.DealDetailsPanel;

public class DealDetails extends WizardStep {

	private static final long serialVersionUID = 1L;

	public DealDetails()
    {
        super(new ResourceModel("title"), new ResourceModel("summary"));
    }

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
        addOrReplace(new DealDetailsPanel("panel"));
	}
	
	@Override
	protected void onConfigure() {
		// TODO Auto-generated method stub
		super.onConfigure();
		
		Deal deal = (Deal) getDefaultModelObject();
	}
}
