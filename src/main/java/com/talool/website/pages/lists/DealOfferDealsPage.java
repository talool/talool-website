package com.talool.website.pages.lists;

import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.MerchantAccount;
import com.talool.website.models.DealListModel;
import com.talool.website.models.DealModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.deal.wizard.DealWizard;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

@SecuredPage
public class DealOfferDealsPage extends BasePage
{
	private static final long serialVersionUID = 6008230892463177176L;
	private static final Logger LOG = LoggerFactory.getLogger(DealOfferDealsPage.class);
	private UUID _dealOfferId;
	private DealWizard wizard;

	public DealOfferDealsPage(PageParameters parameters)
	{
		super(parameters);
		_dealOfferId = UUID.fromString(parameters.get("id").toString());
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		DealListModel model = new DealListModel();
		model.setDealOfferId(_dealOfferId);

		final WebMarkupContainer container = new WebMarkupContainer("dealList");
		container.setOutputMarkupId(true);
		add(container);

		final ListView<Deal> deals = new ListView<Deal>("dealRptr", model)
		{

			private static final long serialVersionUID = 2705519558987278333L;

			@Override
			protected void populateItem(ListItem<Deal> item)
			{
				Deal deal = item.getModelObject();
				final UUID dealId = deal.getId();

				item.setModel(new CompoundPropertyModel<Deal>(deal));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				item.add(new Label("merchant.name"));
				item.add(new Label("summary"));

				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = -3574012236379219691L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						wizard.setModelObject(new DealModel(dealId, true).getObject());
						wizard.open(target);
					}
				});

			}

		};
		container.add(deals);

		// override the action button
		final BasePage page = (BasePage) this.getPage();
		AjaxLink<Void> actionLink = new AjaxLink<Void>("actionLink")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				MerchantAccount ma = SessionUtils.getSession().getMerchantAccount();
				Deal deal = domainFactory.newDeal(null, ma, true);
				wizard.setModelObject(deal);
				wizard.open(target);
			}
		};
		page.setActionLink(actionLink);
		Label actionLabel = new Label("actionLabel", getNewDefinitionPanelTitle());
		actionLabel.setOutputMarkupId(true);
		actionLink.add(actionLabel);
		actionLink.setOutputMarkupId(true);

		// Wizard
		wizard = new DealWizard("wiz", "Deal Wizard")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target)
			{
				super.onFinish(target);
				target.add(container);
			}
		};
		add(wizard);

	}

	@Override
	public String getHeaderTitle()
	{
		MerchantAccount ma = SessionUtils.getSession().getMerchantAccount();
		StringBuilder sb = new StringBuilder(ma.getMerchant().getName());
		sb.append(" > ").append(getPageParameters().get("name")).append(" > Deals");
		return sb.toString();
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return null;
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		return "New Deal";
	}
}
