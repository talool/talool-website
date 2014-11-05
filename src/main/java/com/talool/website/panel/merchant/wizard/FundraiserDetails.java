package com.talool.website.panel.merchant.wizard;

import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.Merchant;
import com.talool.website.component.StateOption;
import com.talool.website.component.StateSelect;

public class FundraiserDetails extends WizardStep
{

	private static final long serialVersionUID = 1L;

	public FundraiserDetails()
	{
		super(new ResourceModel("title"), new ResourceModel("summary"));
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();

		WebMarkupContainer descriptionPanel = new WebMarkupContainer("descriptionPanel");
		addOrReplace(descriptionPanel.setOutputMarkupId(true));

		// TODO check for duplicate fundraiser names
		descriptionPanel.add(new TextField<String>("name").setRequired(true));
		
		final StateSelect state = new StateSelect("currentLocation.stateProvinceCounty",
				new PropertyModel<StateOption>(this, "stateOption"));
		descriptionPanel.add(state.setRequired(true));

	}
	
	public StateOption getStateOption()
	{
		final Merchant merch = (Merchant) getDefaultModelObject();
		if (merch.getCurrentLocation().getStateProvinceCounty() == null)
		{
			return null;
		}

		return StateSelect.getStateOptionByCode(merch.getCurrentLocation()
				.getStateProvinceCounty());

	}

	public void setStateOption(final StateOption stateOption)
	{
		final Merchant merch = (Merchant) getDefaultModelObject();
		merch.getCurrentLocation().setStateProvinceCounty(stateOption.getCode());
	}

}
