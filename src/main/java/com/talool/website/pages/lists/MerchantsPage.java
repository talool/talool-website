package com.talool.website.pages.lists;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
import org.wicketstuff.gmap.GMap;

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
import com.talool.website.panel.merchant.wizard.MerchantWizard;
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
	private MerchantWizard wizard;
	
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
			List<Merchant> entities = taloolService.getMerchantsWithin(location, 650, null);
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
		
		final WebMarkupContainer container = new WebMarkupContainer("merchantList");
		container.setOutputMarkupId(true);
		add(container);

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
		container.add(mechants);
		
		// override the action button
		AjaxLink<Void> actionLink = new AjaxLink<Void>("actionLink")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				
				Merchant merchant = domainFactory.newMerchant();
				MerchantLocation location = domainFactory.newMerchantLocation();
				location.setAddress(domainFactory.newAddress());
				location.setLogoUrl("");
				merchant.addLocation(location);
				
				wizard.setModelObject(merchant);
				wizard.open(target);
			}
		};
		setActionLink(actionLink);
		Label actionLabel = new Label("actionLabel", getNewDefinitionPanelTitle());
		actionLabel.setOutputMarkupId(true);
		actionLink.add(actionLabel);
		actionLink.setOutputMarkupId(true);
		
		// Wizard
		wizard = new MerchantWizard("wiz", "Merchant Wizard") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target) {
				super.onFinish(target);
				// refresh the list after a deal is edited
				target.add(container);
			}
		};
		add(wizard);
		
		// preload the map to avoid a race condition with the loading of js dependencies
		GMap map = new GMap("preloadMap");
		add(map);

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
		// intentionally null
		return null;
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		// intentionally null
		return "New Merchant";
	}
}
