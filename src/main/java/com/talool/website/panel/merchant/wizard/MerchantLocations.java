package com.talool.website.panel.merchant.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.FactoryManager;
import com.talool.core.MediaType;
import com.talool.core.Merchant;
import com.talool.core.MerchantMedia;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.component.MerchantMediaWizardPanel;
import com.talool.website.component.StateOption;
import com.talool.website.component.StateSelect;

public class MerchantLocations extends WizardStep
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantLocations.class);
	private MerchantMedia selectedMedia;

	private transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public MerchantLocations()
	{
		super(new ResourceModel("title"), new ResourceModel("summary"));

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onConfigure()
	{
		super.onConfigure();

		setDefaultModel(new CompoundPropertyModel<Merchant>((IModel<Merchant>) getDefaultModel()));

		final Merchant merchant = (Merchant) getDefaultModelObject();

		if (merchant.getId() == null) 
		{
			// don't show the logo panel
			addOrReplace(new WebMarkupContainer("merchantMediaLogo"));
		}
		else
		{
			// TODO add logo image
			selectedMedia = merchant.getCurrentLocation().getLogo();
			PropertyModel<MerchantMedia> selectedMediaModel = new PropertyModel<MerchantMedia>(this,"selectedMedia");
			MerchantMediaWizardPanel logoPanel = new MerchantMediaWizardPanel("merchantMediaLogo", merchant.getId(), MediaType.MERCHANT_LOGO, selectedMediaModel)
			{

				private static final long serialVersionUID = 1L;

				@Override
				public void onMediaUploadComplete(AjaxRequestTarget target, String url) {}
				
			};
			addOrReplace(logoPanel);
		}

		WebMarkupContainer locationPanel = new WebMarkupContainer("locationPanel");
		addOrReplace(locationPanel);

		final TextField<String> addr1 = new TextField<String>("currentLocation.address.address1");
		locationPanel.add(addr1.setRequired(true));

		final TextField<String> addr2 = new TextField<String>("currentLocation.address.address2");
		locationPanel.add(addr2);

		final TextField<String> city = new TextField<String>("currentLocation.address.city");
		locationPanel.add(city.setRequired(true));

		final StateSelect state = new StateSelect("currentLocation.address.stateProvinceCounty",
				new PropertyModel<StateOption>(this, "stateOption"));
		locationPanel.add(state.setRequired(true));

		locationPanel.add(new TextField<String>("currentLocation.address.zip").setRequired(true));

		locationPanel.add(new TextField<String>("currentLocation.locationName"));

		WebMarkupContainer contactPanel = new WebMarkupContainer("contactPanel");
		addOrReplace(contactPanel);

		contactPanel.add(new TextField<String>("currentLocation.phone").setRequired(true));

		contactPanel.add(new TextField<String>("currentLocation.email").setRequired(true).add(
				EmailAddressValidator.getInstance()));

		contactPanel.add(new TextField<String>("currentLocation.websiteUrl").add(new UrlValidator()));

	}

	public StateOption getStateOption()
	{
		final Merchant merch = (Merchant) getDefaultModelObject();
		if (merch.getCurrentLocation().getAddress().getStateProvinceCounty() == null)
		{
			return null;
		}

		return StateSelect.getStateOptionByCode(merch.getCurrentLocation().getAddress()
				.getStateProvinceCounty());

	}

	public void setStateOption(final StateOption stateOption)
	{
		final Merchant merch = (Merchant) getDefaultModelObject();
		merch.getCurrentLocation().getAddress().setStateProvinceCounty(stateOption.getCode());
	}

	/*
	 * Save the state of the Merchant.
	 */
	@Override
	public void applyState()
	{
		super.applyState();

		final Merchant merch = (Merchant) getDefaultModelObject();

		if (merch.getId() == null)
		{
			try
			{
				// New users need to be saved before we can save the logos
				taloolService.save(merch);
				StringBuilder sb = new StringBuilder("Saved Merchant with id:");
				LOG.debug(sb.append(merch.getId()).toString());
			}
			catch (ServiceException se)
			{
				LOG.error("failed to save new merchant:", se);
			}
			catch (Exception e)
			{
				// duplicate merchant name?
				LOG.error("random-ass-exception saving new merchant:", e);
			}
		}
		else
		{
			// get the selected MerchantMedia and add it to the location
			if (selectedMedia != null)
			{
				merch.getCurrentLocation().setLogo(selectedMedia);
			}
			
			try
			{
				// Need to save the current location in the edit flow
				taloolService.merge(merch.getCurrentLocation());
			}
			catch (ServiceException se)
			{
				LOG.error("failed to save new merchant location:", se);
			}
		}


		
	}

}
