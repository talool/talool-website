package com.talool.website.panel.merchant.wizard;

import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.validator.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.FactoryManager;
import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.utils.HttpUtils;
import com.talool.website.component.StateOption;
import com.talool.website.component.StateSelect;
import com.vividsolutions.jts.geom.Geometry;

public class MerchantLocationStep extends WizardStep
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantLocationStep.class);
	
	private transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public MerchantLocationStep()
	{
		super(new ResourceModel("title"), new ResourceModel("summary"));
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onConfigure()
	{
		super.onConfigure();

		setDefaultModel(new CompoundPropertyModel<Merchant>((IModel<Merchant>) getDefaultModel()));

		WebMarkupContainer locationPanel = new WebMarkupContainer("locationPanel");
		addOrReplace(locationPanel);

		final TextField<String> addr1 = new TextField<String>("currentLocation.address1");
		locationPanel.add(addr1.setRequired(true));

		final TextField<String> addr2 = new TextField<String>("currentLocation.address2");
		locationPanel.add(addr2);

		final TextField<String> city = new TextField<String>("currentLocation.city");
		locationPanel.add(city.setRequired(true));

		final StateSelect state = new StateSelect("currentLocation.stateProvinceCounty",
				new PropertyModel<StateOption>(this, "stateOption"));
		locationPanel.add(state.setRequired(true));

		locationPanel.add(new TextField<String>("currentLocation.zip").setRequired(true));

		locationPanel.add(new TextField<String>("currentLocation.locationName"));

		locationPanel.add(new TextField<String>("currentLocation.phone").setRequired(true));

		locationPanel.add(new TextField<String>("currentLocation.websiteUrl").setRequired(true).add(new UrlValidator()));
		
		// TODO drop the email from the data model
		Merchant merch = (Merchant) getDefaultModelObject();
		if (merch.getCurrentLocation().getEmail() == null)
		{
			merch.getCurrentLocation().setEmail("foo@talool.com");
		}
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

	/*
	 * Save the state of the Merchant.
	 */
	@Override
	public void applyState()
	{
		super.applyState();

		final Merchant merch = (Merchant) getDefaultModelObject();
		MerchantLocation merchantLocation = merch.getCurrentLocation();
		Geometry newGeo = null;
		try
		{
			newGeo = HttpUtils.getGeometry(merchantLocation);
			merchantLocation.setGeometry(newGeo);
			// TODO update any offers using this geo
		}
		catch (ServiceException se)
		{
			error(se.getErrorCode().getMessage());
			LOG.error("There was an exception resolving lat/long of location: " + se.getLocalizedMessage(), se);
		}
		catch (Exception e)
		{
			error("We were unable to process your location.");
			LOG.error("There was an exception resolving lat/long of location: " + e.getLocalizedMessage(), e);
		}
		
		// Only save if we have to and the geo is not null
		if (merch.getId() == null && newGeo != null)
		{
			try
			{
				// New locations need to be saved before we can save the logos, images, geo,
				// etc
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
		else if (newGeo == null)
		{
			// this shouldn't be possible, but let's log it just in case
			LOG.error("Somehow we got a null location for merchant: "+merch.getId());
		}

	}

}
