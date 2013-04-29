package com.talool.website.panel.merchant.definition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.UrlValidator;

import com.talool.core.Merchant;
import com.talool.core.MerchantManagedLocation;
import com.talool.core.Tag;
import com.talool.core.service.ServiceException;
import com.talool.website.component.StateOption;
import com.talool.website.component.StateSelect;
import com.talool.website.models.MerchantModel;
import com.talool.website.models.ModelUtil;
import com.talool.website.models.TagListModel;
import com.talool.website.models.TagListModel.CATEGORY;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantPanel extends BaseDefinitionPanel
{
	private static final long serialVersionUID = -8074065320919062316L;
	private Tag category;
	private List<Tag> tags;

	public MerchantPanel(final String id, final SubmitCallBack callback)
	{
		super(id, callback);

		Merchant merchant = domainFactory.newMerchant();
		merchant.setPrimaryLocation(domainFactory.newMerchantLocation());
		merchant.getPrimaryLocation().setAddress(domainFactory.newAddress());
		merchant.getPrimaryLocation().setLogoUrl("");
		setDefaultModel(Model.of(merchant));
	}

	public MerchantPanel(final String id, final SubmitCallBack callback, final UUID merchantId)
	{
		super(id, callback);
		setDefaultModel(new MerchantModel(merchantId));
		setTags(getTags());
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

	public List<Tag> getTags()
	{
		final Merchant merch = (Merchant) getDefaultModelObject();
		return ModelUtil.getTagList(merch);
	}

	public void setTags(List<Tag> tags)
	{
		this.tags = tags;
	}
	
	public Tag getCategory() {
		final Merchant merch = (Merchant) getDefaultModelObject();
		return ModelUtil.getCategory(merch);
	}

	public void setCategory(Tag category) {
		this.category = category;
	}


	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		WebMarkupContainer descriptionPanel = new WebMarkupContainer("descriptionPanel");
		form.add(descriptionPanel.setOutputMarkupId(true));
		
		descriptionPanel.add(new TextField<String>("name").setRequired(true));
		
		ChoiceRenderer<Tag> cr = new ChoiceRenderer<Tag>("name","name");
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final ListMultipleChoice tagChoices = new ListMultipleChoice("tags", new PropertyModel<List<Tag>>(this, "tags"), new TagListModel(category),cr);
		tagChoices.setMaxRows(18);
		tagChoices.setOutputMarkupId(true);
		descriptionPanel.add(tagChoices.setRequired(true));
		
		DropDownChoice<Tag> categorySelect = new DropDownChoice<Tag>("category", new PropertyModel<Tag>(this, "category"), new TagListModel(CATEGORY.ROOT),cr);
		categorySelect.setOutputMarkupId(true);
		categorySelect.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long serialVersionUID = -1909537074284102774L;

			@SuppressWarnings("unchecked")
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				tagChoices.setChoices( TagListModel.getModel(category));
				target.add(tagChoices);
			}
			
		});
		descriptionPanel.add(categorySelect.setRequired(true));

		WebMarkupContainer locationPanel = new WebMarkupContainer("locationPanel");
		form.add(locationPanel);
		
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
		
		// Load the primary location as a managed location by default
		MerchantManagedLocation managedLocation = domainFactory.newMerchantManagedLocation(merchant);
		managedLocation.setMerchantLocation(merchant.getPrimaryLocation());

		if (CollectionUtils.isNotEmpty(tags) || category!=null)
		{
			Set<Tag> selectedTags = new HashSet<Tag>();
			if (CollectionUtils.isNotEmpty(tags))
			{
				selectedTags.addAll(tags);
			}
			if (category != null)
			{
				selectedTags.add(category);
			}
			merchant.setTags(selectedTags);
		}
		else
		{
			merchant.clearTags();
		}

		taloolService.save(merchant);
		taloolService.save(managedLocation);
		SessionUtils.successMessage("Successfully saved merchant '", merchant.getName(), "'");

	}

	@Override
	public String getSaveButtonLabel()
	{
		return "Save Merchant";
	}
	
}
