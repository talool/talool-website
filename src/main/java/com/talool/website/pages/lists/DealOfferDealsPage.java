package com.talool.website.pages.lists;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Deal;
import com.talool.website.models.DealListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.DealOfferDealPanel;
import com.talool.website.util.SecuredPage;

@SecuredPage
public class DealOfferDealsPage extends BasePage
{
	private static final long serialVersionUID = 6008230892463177176L;
	private Long _dealOfferId;

	public DealOfferDealsPage(PageParameters parameters)
	{
		super(parameters);
		_dealOfferId = parameters.get("id").toLong();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		DealListModel model = new DealListModel();
		model.setDealOfferId(_dealOfferId);
		final ListView<Deal> deals = new ListView<Deal>("dealRptr", model)
		{

			private static final long serialVersionUID = 2705519558987278333L;

			@Override
			protected void populateItem(ListItem<Deal> item)
			{
				Deal deal = item.getModelObject();
				final Long dealId = deal.getId();

				item.setModel(new CompoundPropertyModel<Deal>(deal));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "gray0-bg"));
				}

				item.add(new Label("title"));
				item.add(new Label("summary"));

				final AdminModalWindow definitionModal = getModal();
				final SubmitCallBack callback = getCallback(definitionModal);
				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = -3574012236379219691L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						DealOfferDealPanel panel = new DealOfferDealPanel(definitionModal.getContentId(),
								callback, dealId);
						definitionModal.setContent(panel);
						definitionModal.setTitle("Edit Deal");
						definitionModal.show(target);
					}
				});

			}

		};

		add(deals);

	}

	@Override
	public String getHeaderTitle()
	{
		// TODO add another param to the url or just hit the db and pull out the details from the id?  Hit the DB...
		StringBuilder sb = new StringBuilder("Merchants > {Merchant Name} >");
		sb.append(getPageParameters().get("name")).append(" > Deals");
		return sb.toString();
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		// NOTE: this is called via the super constructor, so can't way for
		// _dealOfferId to be set in this constructor.
		_dealOfferId = getPageParameters().get("id").toLong();
		return new DealOfferDealPanel(contentId, _dealOfferId, callback);
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		return "Create New Deal";
	}
}
