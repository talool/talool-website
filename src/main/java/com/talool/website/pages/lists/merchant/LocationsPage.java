package com.talool.website.pages.lists.merchant;

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
import com.talool.core.MerchantLocation;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.models.MerchantLocationListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminMenuPanel;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.MerchantLocationPanel;
import com.talool.website.panel.MerchantPanel;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.panel.SubmitCallBack;

public class LocationsPage extends BasePage {
	
	private static final long serialVersionUID = 3634980968241854373L;
	private static final Logger LOG = LoggerFactory.getLogger(LocationsPage.class);
	private Long _merchantId;

	public LocationsPage()
	{
		super();
	}

	public LocationsPage(PageParameters parameters)
	{
		super(parameters);
		_merchantId = parameters.get("id").toLongObject();
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new AdminMenuPanel("adminMenuPanel").setRenderBodyOnly(true));
		
		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		add(feedback.setOutputMarkupId(true));
		
		final AdminModalWindow locationModal;
		add(locationModal = new AdminModalWindow("modal"));
		final SubmitCallBack callback = new SubmitCallBack()
		{

			private static final long serialVersionUID = 6743167793934938733L;

			@Override
			public void submitSuccess(AjaxRequestTarget target)
			{
				locationModal.close(target);
				target.add(LocationsPage.this);
			}

			@Override
			public void submitFailure(AjaxRequestTarget target)
			{

			}
		};

		final MerchantLocationPanel locationPanel = new MerchantLocationPanel(locationModal.getContentId(), callback);
		locationModal.setContent(locationPanel);
		add(new AjaxLink<Void>("locationLink")
		{
			private static final long serialVersionUID = 721835854434485151L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				locationModal.setTitle("Create Merchant Location");
				locationModal.setContent(new MerchantLocationPanel(locationModal.getContentId(), callback));
				locationModal.show(target);
			}
		});
		
		StringBuffer pageTitle = new StringBuffer("Merchant Locations for ");
		MerchantLocationListModel model = new MerchantLocationListModel();
		try {
			Merchant merchant = ServiceFactory.get().getTaloolService().getMerchantById(_merchantId);
			pageTitle.append(merchant.getName());
			model.setMerchantId(merchant.getId());
		} catch (ServiceException se) {
			LOG.error("problem loading merchant", se);
		}
		add(new Label("pageTitle",pageTitle.toString()));
		
		final ListView<MerchantLocation> locations = new ListView<MerchantLocation>("locationRptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<MerchantLocation> item)
			{
				MerchantLocation location = item.getModelObject();
				final Long merchantLocationId = location.getId();
				
				item.setModel(new CompoundPropertyModel<MerchantLocation>(location));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd"));
				}
				else
				{
					item.add(new AttributeModifier("class", "even"));
				}
				
				item.add(new Label("locationName"));
				item.add(new Label("websiteUrl"));
				item.add(new Label("email"));
				item.add(new Label("phone"));
				item.add(new Label("address.city"));
				item.add(new Label("address.stateProvinceCounty"));
				
				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = 8817599057544892359L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						MerchantLocationPanel panel = new MerchantLocationPanel(locationModal.getContentId(), callback,
								merchantLocationId);
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