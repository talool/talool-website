package com.talool.website.pages.lists;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.gmap.GMap;

import com.talool.core.Merchant;
import com.talool.website.models.MerchantListModel;
import com.talool.website.models.MerchantModel;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.MerchantManagementPage;
import com.talool.website.pages.dashboard.MerchantDashboard;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.wizard.MerchantWizard;
import com.talool.website.service.PermissionService;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author clintz
 * 
 */
@SecuredPage
public class MerchantsPage extends BasePage
{
	private static final long serialVersionUID = 9023714664854633955L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantsPage.class);
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

		final WebMarkupContainer container = new WebMarkupContainer("merchantList");
		container.setOutputMarkupId(true);
		add(container);

		boolean canViewAllMerchants = PermissionService.get().canViewAnalytics(
				SessionUtils.getSession().getMerchantAccount().getEmail());

		MerchantListModel model = new MerchantListModel();
		if (!canViewAllMerchants)
		{
			LOG.info(SessionUtils.getSession().getMerchantAccount().getEmail());
			model.setMerchantId(SessionUtils.getSession().getMerchantAccount().getMerchant().getId());
		}

		final ListView<Merchant> mechants = new ListView<Merchant>("merchRptr", model)
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
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				PageParameters booksParams = new PageParameters();
				booksParams.set("id", merchant.getId());
				booksParams.set("name", merchant.getName());
				String url = (String) urlFor(MerchantManagementPage.class, booksParams);
				ExternalLink namelLink = new ExternalLink("nameLink", Model.of(url),
						new PropertyModel<String>(merchant, "name"));

				item.add(namelLink);
				item.add(new Label("category.name"));
				item.add(new Label("primaryLocation.address1"));
				item.add(new Label("primaryLocation.address2"));
				item.add(new Label("primaryLocation.niceCityState"));
				item.add(new Label("primaryLocation.zip"));

				StringBuilder hasMultiple = new StringBuilder();
				hasMultiple.append(merchant.getLocations().size());
				item.add(new Label("multiple", hasMultiple.toString()));

				// TODO - at some point, this tags label can be based on a model
				// item.add(new Label("tags", ModelUtil.geTagSummary(merchant)));

				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = 8817599057544892359L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						wizard.setModelObject(new MerchantModel(merchantId, true).getObject());
						wizard.open(target);
					}
				});

				PageParameters dashboardParams = new PageParameters();
				dashboardParams.set("id", merchant.getId());
				String dashboardUrl = (String) urlFor(MerchantDashboard.class, dashboardParams);
				ExternalLink dashboardLink = new ExternalLink("dashboardLink", Model.of(dashboardUrl));
				item.add(dashboardLink);
				// Only show this link for Talool users... for now.
				dashboardLink.setVisible(isTaloolUserLoggedIn);

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
				final Merchant merchant = domainFactory.newMerchant();
				merchant.getLocations().get(0).setCreatedByMerchantAccount(SessionUtils.getSession().getMerchantAccount());
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
		wizard = new MerchantWizard("wiz", "Merchant Wizard")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target)
			{
				super.onFinish(target);
				// refresh the list after a deal is edited
				target.add(container);
			}
		};
		add(wizard);

		// preload the map to avoid a race condition with the loading of js
		// dependencies
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
		return "New Merchant";
	}
}
