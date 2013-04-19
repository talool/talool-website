package com.talool.website.panel.merchant.definition;

import java.util.UUID;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.MerchantManagedLocation;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.models.MerchantManagedLocationModel;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantLocationPanel extends BaseDefinitionPanel
{
	private static final long serialVersionUID = 661849211369766802L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantLocationPanel.class);

	public MerchantLocationPanel(final String id, final UUID merchantId, final SubmitCallBack callback)
	{
		super(id, callback);

		Merchant merchant = null;
		try
		{
			merchant = ServiceFactory.get().getTaloolService().getMerchantById(merchantId);
		}
		catch (ServiceException se)
		{
			LOG.error("problem loading merchant", se);
		}

		MerchantManagedLocation managedLocation = domainFactory.newMerchantManagedLocation(merchant);
		managedLocation.getMerchantLocation().setAddress(domainFactory.newAddress());
		managedLocation.getMerchantLocation().setLogoUrl("");
		setDefaultModel(Model.of(managedLocation));
	}

	public MerchantLocationPanel(final String id, final SubmitCallBack callback,
			final Long merchantManagedLocationId)
	{
		super(id, callback);
		;
		setDefaultModel(new MerchantManagedLocationModel(merchantManagedLocationId));
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		WebMarkupContainer locationPanel = new WebMarkupContainer("locationPanel");
		form.add(locationPanel);

		locationPanel.add(new TextField<String>("merchantLocation.address.address1").setRequired(true));

		locationPanel.add(new TextField<String>("merchantLocation.address.address2"));
		locationPanel.add(new TextField<String>("merchantLocation.address.city").setRequired(true));
		locationPanel.add(new TextField<String>("merchantLocation.address.stateProvinceCounty")
				.setRequired(true));
		locationPanel.add(new TextField<String>("merchantLocation.address.zip").setRequired(true));
		locationPanel.add(new TextField<String>("merchantLocation.address.country").setRequired(true));
		locationPanel.add(new TextField<String>("merchantLocation.locationName"));
		locationPanel.add(new TextField<String>("merchantLocation.phone").setRequired(true));
		locationPanel.add(new TextField<String>("merchantLocation.email").setRequired(true));
		locationPanel.add(new TextField<String>("merchantLocation.websiteUrl"));

	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundPropertyModel<MerchantManagedLocation> getDefaultCompoundPropertyModel()
	{
		return new CompoundPropertyModel<MerchantManagedLocation>(
				(IModel<MerchantManagedLocation>) getDefaultModel());
	}

	@Override
	public String getObjectIdentifier()
	{
		MerchantManagedLocation managedLocation = (MerchantManagedLocation) form
				.getDefaultModelObject();
		return managedLocation.getMerchantLocation().getLocationName();
	}

	@Override
	public void save() throws ServiceException
	{
		MerchantManagedLocation managedLocation = (MerchantManagedLocation) form
				.getDefaultModelObject();
		taloolService.save(managedLocation);
	}

	@Override
	public String getSaveButtonLabel()
	{
		return "Save Merchant Location";
	}
}
