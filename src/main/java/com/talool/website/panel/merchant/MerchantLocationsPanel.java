package com.talool.website.panel.merchant;

import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.MerchantManagedLocation;
import com.talool.website.models.MerchantManagedLocationListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.MerchantLocationPanel;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantLocationsPanel extends BaseTabPanel
{
	private static final long serialVersionUID = 3634980968241854373L;
	private UUID _merchantId;

	public MerchantLocationsPanel(String id, PageParameters parameters)
	{
		super(id);
		_merchantId = UUID.fromString(parameters.get("id").toString());
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		MerchantManagedLocationListModel model = new MerchantManagedLocationListModel();
		model.setMerchantId(_merchantId);
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
					item.add(new AttributeModifier("class", "gray0-bg"));
				}

				item.add(new Label("merchantLocation.locationName"));
				item.add(new Label("merchantLocation.websiteUrl"));
				item.add(new Label("merchantLocation.email"));
				item.add(new Label("merchantLocation.phone"));
				item.add(new Label("merchantLocation.address.city"));
				item.add(new Label("merchantLocation.address.stateProvinceCounty"));

				BasePage page = (BasePage) this.getPage();
				final AdminModalWindow modal = page.getModal();
				final SubmitCallBack callback = page.getCallback(modal);
				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = 8817599057544892359L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						MerchantLocationPanel panel = new MerchantLocationPanel(modal.getContentId(), callback,
								merchantLocationId);
						modal.setContent(panel);
						modal.setTitle("Edit Merchant Location");
						modal.show(target);
					}
				});
			}

		};

		add(locations);
	}

	@Override
	public String getActionLabel()
	{
		return "Create Location";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return new MerchantLocationPanel(contentId, _merchantId, callback);
	}

}
