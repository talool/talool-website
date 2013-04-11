package com.talool.website.panel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.MerchantManagedLocation;
import com.talool.core.service.ServiceException;
import com.talool.website.models.MerchantManagedLocationListModel;

/**
 * DEMO PANEL - LETS SEE WHAT WE FEEL . THIS IS TEH SAME CONTENT AS
 * LOCATIONSPAGE.JAVA
 * 
 * @author clintz
 * 
 */
public class MerchantLocationsPanelDemo extends BasePanel
{
	private static final long serialVersionUID = 3634980968241854373L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantLocationsPanelDemo.class);
	private Long _merchantId;

	public MerchantLocationsPanelDemo(String id, PageParameters parameters)
	{
		super(id);
		_merchantId = parameters.get("id").toLongObject();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final AdminModalWindow locationModal;
		add(locationModal = new AdminModalWindow("modal"));
		final SubmitCallBack callback = new SubmitCallBack()
		{

			private static final long serialVersionUID = 6743167793934938733L;

			@Override
			public void submitSuccess(AjaxRequestTarget target)
			{
				locationModal.close(target);
				// target.add(LocationsPage.this);
			}

			@Override
			public void submitFailure(AjaxRequestTarget target)
			{

			}
		};

		final MerchantLocationPanel locationPanel = new MerchantLocationPanel(
				locationModal.getContentId(), _merchantId, callback);
		locationModal.setContent(locationPanel);
		add(new AjaxLink<Void>("locationLink")
		{
			private static final long serialVersionUID = 721835854434485151L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				locationModal.setTitle("Create Merchant Location");
				locationModal.setContent(new MerchantLocationPanel(locationModal.getContentId(),
						_merchantId, callback));
				locationModal.show(target);
			}
		});

		MerchantManagedLocationListModel model = new MerchantManagedLocationListModel();
		try
		{
			Merchant merchant = taloolService.getMerchantById(_merchantId);
			model.setMerchantId(merchant.getId());
		}
		catch (ServiceException se)
		{
			LOG.error("problem loading merchant", se);
		}

		final ListView<MerchantManagedLocation> locations = new ListView<MerchantManagedLocation>(
				"locationRptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<MerchantManagedLocation> item)
			{
				MerchantManagedLocation managedLocation = item.getModelObject();
				final Long merchantLocationId = managedLocation.getId();

				item.setModel(new CompoundPropertyModel<MerchantManagedLocation>(managedLocation));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd"));
				}
				else
				{
					item.add(new AttributeModifier("class", "even"));
				}

				item.add(new Label("merchantLocation.locationName"));

				item.add(new Label("merchantLocation.websiteUrl"));
				item.add(new Label("merchantLocation.email"));
				item.add(new Label("merchantLocation.phone"));
				item.add(new Label("merchantLocation.address.city"));
				item.add(new Label("merchantLocation.address.stateProvinceCounty"));

				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = 8817599057544892359L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						MerchantLocationPanel panel = new MerchantLocationPanel(locationModal.getContentId(),
								callback, merchantLocationId);
						locationModal.setContent(panel);
						locationModal.setTitle("Edit Merchant Location");
						locationModal.show(target);
					}
				});
			}

		};

		add(locations);
	}
}
