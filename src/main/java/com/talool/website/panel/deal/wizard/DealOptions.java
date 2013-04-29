package com.talool.website.panel.deal.wizard;

import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.ResourceModel;

public class DealOptions extends WizardStep{

	private static final long serialVersionUID = 1L;

	public DealOptions()
    {
		super(new ResourceModel("title"), new ResourceModel("summary"));
    }
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		addOrReplace(new RequiredTextField<String>("summary"));
	}
}
