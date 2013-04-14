package com.talool.website.panel.merchant;

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
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.DealOfferDealPanel;

public class MerchantDealsPanel extends BaseTabPanel
{

	private static final long serialVersionUID = 3634980968241854373L;
	private Long _merchantId;

	public MerchantDealsPanel(String id, PageParameters parameters)
	{
		super(id);
		_merchantId = parameters.get("id").toLongObject();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		DealListModel model = new DealListModel();
		model.setMerchantId(_merchantId);
		final ListView<Deal> customers = new ListView<Deal>("dealRptr", model)
		{

			private static final long serialVersionUID = 1L;

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
				item.add(new Label("expires"));
				item.add(new Label("isActive"));
				item.add(new Label("merchant.name"));
				item.add(new Label("dealOffer.title"));

				BasePage page = (BasePage) this.getPage();
				final AdminModalWindow modal = page.getModal();
				final SubmitCallBack callback = page.getCallback(modal);
				item.add(new AjaxLink<Void>("editLink")
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						DealOfferDealPanel panel = new DealOfferDealPanel(modal.getContentId(), callback, dealId);
						modal.setContent(panel);
						modal.setTitle("Edit Merchant Deal");
						modal.show(target);
					}
				});
			}

		};

		add(customers);
	}

	@Override
	public String getActionLabel() {
		return "Create Merchant Deal";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback) {
		
		return new DealOfferDealPanel(contentId, callback);
	}


}