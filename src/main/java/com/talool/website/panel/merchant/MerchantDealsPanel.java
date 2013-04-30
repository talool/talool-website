package com.talool.website.panel.merchant;

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

import com.talool.core.Deal;
import com.talool.website.models.DealListModel;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.deal.definition.DealOfferDealPanel;
import com.talool.website.panel.deal.wizard.DealWizard;

public class MerchantDealsPanel extends BaseTabPanel
{
	private static final long serialVersionUID = 3634980968241854373L;
	private UUID _merchantId;
	private DealWizard wizard;

	public MerchantDealsPanel(String id, PageParameters parameters)
	{
		super(id);
		_merchantId = UUID.fromString(parameters.get("id").toString());
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		DealListModel model = new DealListModel();
		model.setMerchantId(_merchantId);
		
		final WebMarkupContainer container = new WebMarkupContainer("dealList");
		container.setOutputMarkupId(true);
		add(container);
		
		final ListView<Deal> customers = new ListView<Deal>("dealRptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Deal> item)
			{

				final Deal deal = item.getModelObject();
				
				item.setModel(new CompoundPropertyModel<Deal>(deal));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "gray0-bg"));
				}

				item.add(new Label("title"));
				item.add(new Label("summary"));
				item.add(new Label("expires"));
				item.add(new Label("isActive"));
				item.add(new Label("merchant.name"));
				item.add(new Label("dealOffer.title"));
				item.add(new Label("createdBy", deal.getCreatedByEmail() + " / "
						+ deal.getCreatedByMerchantName()));

				item.add(new Label("lastUpdatedBy", deal.getUpdatedByEmail() + " / "
						+ deal.getUpdatedByMerchantName()));

				item.add(new AjaxLink<Void>("editLink")
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						wizard.setModelObject(deal);
						wizard.open(target);
					}
				});
			}

		};
		container.add(customers);
		
		// Wizard
		wizard = new DealWizard("wiz", "Deal Wizard") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target) {
				super.onFinish(target);
				// refresh the list after a deal is edited
				target.add(container);
			}
		};
		add(wizard);
	}

	@Override
	public String getActionLabel()
	{
		return "Create Merchant Deal";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{

		return new DealOfferDealPanel(contentId, callback);
	}

}