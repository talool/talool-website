package com.talool.website.pages.lists;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Location;
import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.talool.core.service.ServiceException;
import com.talool.domain.LocationImpl;
import com.talool.website.models.MerchantListModel;
import com.talool.website.models.ModelUtil;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.MerchantManagementPage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.MerchantPanel;
import com.talool.website.util.SecuredPage;

/**
 * 
 * @author clintz
 * 
 */
@SecuredPage
public class MerchantsPage extends BasePage
{
	private static final long serialVersionUID = 9023714664854633955L;

	public MerchantsPage()
	{
		super();
	}

	public MerchantsPage(PageParameters parameters)
	{
		super(parameters);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		try
		{
			final Location location = new LocationImpl(105.2700, 40.0150);
			List<Merchant> entities = taloolService.getMerchantsWithin(location, 650);
			for (final Merchant merchant : entities)
			{
				for (MerchantLocation mloc : merchant.getLocations())
				{
					System.out.println(merchant.getName() + " " + mloc.getDistanceInMeters());

				}

			}

		}
		catch (ServiceException e)
		{
			e.printStackTrace();
		}

		final ListView<Merchant> mechants = new ListView<Merchant>("merchRptr", new MerchantListModel())
		{

			private static final long serialVersionUID = 8844000843574646422L;

			@Override
			protected void populateItem(ListItem<Merchant> item)
			{
				Merchant merchant = item.getModelObject();
				final UUID merchantId = merchant.getId();

				item.setModel(new CompoundPropertyModel<Merchant>(merchant));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "gray0-bg"));
				}

				PageParameters booksParams = new PageParameters();
				booksParams.set("id", merchant.getId());
				booksParams.set("name", merchant.getName());
				String url = (String) urlFor(MerchantManagementPage.class, booksParams);
				ExternalLink namelLink = new ExternalLink("nameLink", Model.of(url),
						new PropertyModel<String>(merchant, "name"));

				item.add(namelLink);

				item.add(new Label("primaryLocation.address.niceCityState"));
				item.add(new Label("accounts", merchant.getNumberOfMerchantAccounts()));

				// TODO - at some point, this tags label can be based on a model
				item.add(new Label("tags", ModelUtil.geTagSummary(merchant)));

				final AdminModalWindow definitionModal = getModal();
				final SubmitCallBack callback = getCallback(definitionModal);
				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = 8817599057544892359L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						MerchantPanel panel = new MerchantPanel(definitionModal.getContentId(), callback,
								merchantId);
						definitionModal.setContent(panel);
						definitionModal.setTitle("Edit Merchant");
						definitionModal.show(target);
					}
				});

			}

		};

		add(mechants);

	}

	@Override
	protected void setHeaders(WebResponse response)
	{
		super.setHeaders(response);
	}

	@Override
	public String getHeaderTitle()
	{
		return "Merchants";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return new MerchantPanel(contentId, callback);
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		return "Create New Merchant";
	}
}
