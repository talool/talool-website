package com.talool.website.panel.merchant.definition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.cache.TagCache;
import com.talool.core.Category;
import com.talool.core.MediaType;
import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.talool.core.Tag;
import com.talool.core.service.ServiceException;
import com.talool.website.behaviors.OnChangeAjaxFormBehavior;
import com.talool.website.component.StateOption;
import com.talool.website.component.StateSelect;
import com.talool.website.models.CategoryListModel;
import com.talool.website.models.CategoryTagListModel;
import com.talool.website.models.MerchantModel;
import com.talool.website.models.ModelUtil;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.HttpUtils;
import com.talool.website.util.JQueryUtils;
import com.talool.website.util.SessionUtils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantPanel extends BaseDefinitionPanel
{
	private static final long serialVersionUID = -8074065320919062316L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantPanel.class);

	private ChoiceRenderer<Tag> tagChoiceRenderer = new ChoiceRenderer<Tag>("name", "name");
	private ChoiceRenderer<Category> categoryChoiceRenderer = new ChoiceRenderer<Category>("name",
			"name");

	private Category category;
	private List<Tag> tags;
	private Double latitude;
	private Double longitude;

	public MerchantPanel(final String id, final SubmitCallBack callback)
	{
		super(id, callback);

		Merchant merchant = domainFactory.newMerchant();
		MerchantLocation location = domainFactory.newMerchantLocation();
		location.setAddress(domainFactory.newAddress());
		location.setLogo(domainFactory.newMedia(merchant.getId(), "", MediaType.MERCHANT_LOGO));
		merchant.addLocation(location);

		setDefaultModel(Model.of(merchant));
	}

	public MerchantPanel(final String id, final SubmitCallBack callback, final UUID merchantId)
	{
		super(id, callback);
		setDefaultModel(new MerchantModel(merchantId, false));
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

	public Category getCategory()
	{
		final Merchant merch = (Merchant) getDefaultModelObject();
		return merch.getCategory();
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		WebMarkupContainer descriptionPanel = new WebMarkupContainer("descriptionPanel");
		form.add(descriptionPanel.setOutputMarkupId(true));

		descriptionPanel.add(new TextField<String>("name").setRequired(true));

		final ListMultipleChoice<Tag> tagChoices = new ListMultipleChoice<Tag>("tags",
				new PropertyModel<List<Tag>>(this, "tags"), new CategoryTagListModel(getCategory()),
				tagChoiceRenderer);

		tagChoices.setMaxRows(18);
		tagChoices.setOutputMarkupId(true);
		descriptionPanel.add(tagChoices.setRequired(true));

		DropDownChoice<Category> categorySelect = new DropDownChoice<Category>("category", new CategoryListModel(),
				categoryChoiceRenderer);

		categorySelect.setOutputMarkupId(true);
		categorySelect.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{

			private static final long serialVersionUID = -1909537074284102774L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				tagChoices.setChoices(TagCache.get().getTagsByCategoryName(category.getName()));
				target.add(tagChoices);
			}

		});
		descriptionPanel.add(categorySelect.setRequired(true));

		WebMarkupContainer locationPanel = new WebMarkupContainer("locationPanel");
		form.add(locationPanel);

		/*
		 * OnChangeAjaxFormBehavior addded forAddress so we can (sort of hack)
		 * ensure the address fields are updated in the event we "resolve location"
		 * on our nested form. Could also use it to validate City/Address spellings
		 * later
		 */
		final TextField<String> addr1 = new TextField<String>("primaryLocation.address.address1");
		addr1.add(new OnChangeAjaxFormBehavior());
		locationPanel.add(addr1.setRequired(true));

		final TextField<String> addr2 = new TextField<String>("primaryLocation.address.address2");
		addr2.add(new OnChangeAjaxFormBehavior());
		locationPanel.add(addr2);

		final TextField<String> city = new TextField<String>("primaryLocation.address.city");
		city.add(new OnChangeAjaxFormBehavior());
		locationPanel.add(city.setRequired(true));

		final StateSelect state = new StateSelect("primaryLocation.address.stateProvinceCounty",
				new PropertyModel<StateOption>(this, "stateOption"));
		state.add(new OnChangeAjaxFormBehavior());
		locationPanel.add(state.setRequired(true));

		locationPanel.add(new TextField<String>("primaryLocation.address.zip").setRequired(true));

		locationPanel.add(new TextField<String>("primaryLocation.address.country").setRequired(true));

		locationPanel.add(new TextField<String>("primaryLocation.locationName"));

		locationPanel.add(new TextField<String>("primaryLocation.phone").setRequired(true));

		locationPanel.add(new TextField<String>("primaryLocation.email").setRequired(true).add(
				EmailAddressValidator.getInstance()));

		locationPanel.add(new TextField<String>("primaryLocation.websiteUrl").add(new UrlValidator()));

		Form<Void> locationForm = new Form<Void>("locationForm");
		locationPanel.add(locationForm);

		final TextField<Double> latitudeField = new TextField<Double>("latitude",
				new PropertyModel<Double>(
						this, "latitude"));
		locationForm.add(latitudeField.setOutputMarkupId(true));

		final TextField<Double> longitudeField = new TextField<Double>("longitude",
				new PropertyModel<Double>(this, "longitude"));
		locationForm.add(longitudeField.setOutputMarkupId(true));

		locationForm.add(new AjaxButton("resolver", locationForm)
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				// TODO THIS MODEL ISNT GETTING UPDATED - REVISIT
				// final Address addr =
				// getDefaultCompoundPropertyModel().getObject().getPrimaryLocation()
				// .getAddress();

				try
				{
					Point point = HttpUtils.getGeometry(addr1.getValue(), addr2.getValue(),
							city.getValue(), state.getValue());
					latitude = point.getY();
					longitude = point.getX();
					target.add(latitudeField);
					target.add(longitudeField);

					StringBuilder sb = new StringBuilder();

					target.appendJavaScript(JQueryUtils.getFadeBackground(sb, latitudeField.getMarkupId(),
							"orange", "white", 1500));
					sb.setLength(0);

					target.appendJavaScript(JQueryUtils.getFadeBackground(sb, longitudeField.getMarkupId(),
							"orange", "white", 1500));
				}

				catch (Exception e)
				{
					LOG.error("There was an exception resolving lat/long: " + e.getLocalizedMessage(), e);
				}
			}

			private static final long serialVersionUID = -6660740154916274132L;

		});

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

		if (CollectionUtils.isNotEmpty(tags) || category != null)
		{
			Set<Tag> selectedTags = new HashSet<Tag>();
			if (CollectionUtils.isNotEmpty(tags))
			{
				selectedTags.addAll(tags);
			}
			// if (category != null)
			// {
			// selectedTags.add(category);
			// }
			merchant.setTags(selectedTags);
		}
		else
		{
			merchant.clearTags();
		}

		if (longitude != null && latitude != null)
		{
			final GeometryFactory factory = new GeometryFactory(
					new PrecisionModel(PrecisionModel.FLOATING), 4326);

			final Point point = factory.createPoint(new Coordinate(longitude, latitude));
			merchant.getPrimaryLocation().setGeometry(point);
		}

		taloolService.merge(merchant);
		// taloolService.save(managedLocation);
		SessionUtils.successMessage("Successfully saved merchant '", merchant.getName(), "'");

	}

	@Override
	public String getSaveButtonLabel()
	{
		return "Save Merchant";
	}

	public Double getLatitude()
	{
		if (latitude == null)
		{
			MerchantLocation location = ((Merchant) getDefaultModelObject()).getPrimaryLocation();
			if (location.getGeometry() != null)
			{
				latitude = location.getGeometry().getCoordinate().y;
			}

		}
		return latitude;
	}

	public void setLatitude(Double latitude)
	{
		this.latitude = latitude;
	}

	public Double getLongitude()
	{
		if (longitude == null)
		{
			MerchantLocation location = ((Merchant) getDefaultModelObject()).getPrimaryLocation();
			if (location.getGeometry() != null)
			{
				longitude = location.getGeometry().getCoordinate().x;
			}

		}
		return longitude;
	}

	public void setLongitude(Double longitude)
	{
		this.longitude = longitude;
	}

}
