package com.talool.website.panel.merchant.definition;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.UrlValidator;

import com.talool.core.Merchant;
import com.talool.core.Tag;
import com.talool.core.service.ServiceException;
import com.talool.website.component.StateOption;
import com.talool.website.component.StateSelect;
import com.talool.website.models.MerchantModel;
import com.talool.website.models.ModelUtil;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantPanel extends BaseDefinitionPanel
{
	private static final long serialVersionUID = -8074065320919062316L;
	private String tags;

	public MerchantPanel(final String id, final SubmitCallBack callback)
	{
		super(id, callback);

		Merchant merchant = domainFactory.newMerchant();
		merchant.setPrimaryLocation(domainFactory.newMerchantLocation());
		merchant.getPrimaryLocation().setAddress(domainFactory.newAddress());
		merchant.getPrimaryLocation().setLogoUrl("");
		setDefaultModel(Model.of(merchant));
	}

	public MerchantPanel(final String id, final SubmitCallBack callback, final Long merchantId)
	{
		super(id, callback);
		setDefaultModel(new MerchantModel(merchantId));
	}

	public StateOption getStateOption()
	{
		final Merchant merch = (Merchant) getDefaultModelObject();
		if (merch.getPrimaryLocation().getAddress().getStateProvinceCounty() == null)
		{
			return null;
		}

		return StateSelect.getStateOptionByCode(merch.getPrimaryLocation().getAddress()
				.getStateProvinceCounty());

	}

	public void setStateOption(final StateOption stateOption)
	{
		final Merchant merch = (Merchant) getDefaultModelObject();
		merch.getPrimaryLocation().getAddress().setStateProvinceCounty(stateOption.getCode());
	}

	public String getTags()
	{
		final Merchant merch = (Merchant) getDefaultModelObject();
		return ModelUtil.getCommaSeperatedTags(merch);
	}

	public void setTags(String tags)
	{
		this.tags = tags;
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		WebMarkupContainer locationPanel = new WebMarkupContainer("locationPanel");
		form.add(locationPanel);

		form.add(new TextField<String>("name").setRequired(true));
		form.add(new TextField<String>("tags", new PropertyModel<String>(this, "tags")));

		locationPanel.add(new TextField<String>("primaryLocation.address.address1").setRequired(true));

		locationPanel.add(new TextField<String>("primaryLocation.address.address2"));

		locationPanel.add(new TextField<String>("primaryLocation.address.city").setRequired(true));

		locationPanel.add(new StateSelect("primaryLocation.address.stateProvinceCounty",
				new PropertyModel<StateOption>(this, "stateOption")).setRequired(true));

		locationPanel.add(new TextField<String>("primaryLocation.address.zip").setRequired(true));

		locationPanel.add(new TextField<String>("primaryLocation.address.country").setRequired(true));

		locationPanel.add(new TextField<String>("primaryLocation.locationName"));

		locationPanel.add(new TextField<String>("primaryLocation.phone").setRequired(true));

		locationPanel.add(new TextField<String>("primaryLocation.email").setRequired(true).add(
				EmailAddressValidator.getInstance()));

		locationPanel.add(new TextField<String>("primaryLocation.websiteUrl").add(new UrlValidator()));

	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundPropertyModel<Merchant> getDefaultCompoundPropertyModel()
	{
		return new CompoundPropertyModel<Merchant>((IModel<Merchant>) getDefaultModel());
	}

	@Override
	public String getObjectIdentifier()
	{
		Merchant merchant = (Merchant) form.getDefaultModelObject();
		return merchant.getName();
	}

	@Override
	public void save() throws ServiceException
	{
		Merchant merchant = (Merchant) form.getDefaultModelObject();

		if (StringUtils.isNotEmpty(tags))
		{
			Set<Tag> selectedTags = taloolService.getOrCreateTags(tags.split(","));
			merchant.setTags(selectedTags);
		}
		else
		{
			merchant.clearTags();
		}

		taloolService.save(merchant);

	}

	@Override
	public String getSaveButtonLabel()
	{
		return "Save Merchant";
	}
}
