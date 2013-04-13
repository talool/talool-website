package com.talool.website.panel.merchant;

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
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.DealOffer;
import com.talool.website.models.DealOfferListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.lists.DealOfferDealsPage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.MerchantDealOfferPanel;

public class MerchantDealOffersPanel extends BaseTabPanel
{

	private static final long serialVersionUID = 3634980968241854373L;
	private Long _merchantId;
	private Long _merchantAccountId;

	public MerchantDealOffersPanel(String id, PageParameters parameters)
	{
		super(id);
		_merchantId = parameters.get("id").toLongObject();
		
		// TODO we need to be able to get the logged in user or default to a Talool Account
		_merchantAccountId = (long) 1;
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		DealOfferListModel model = new DealOfferListModel();
		model.setMerchantId(_merchantId);
		final ListView<DealOffer> customers = new ListView<DealOffer>("offerRptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<DealOffer> item)
			{
				
				DealOffer dealOffer = item.getModelObject();
				final Long dealOfferId = dealOffer.getId();

				item.setModel(new CompoundPropertyModel<DealOffer>(dealOffer));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "gray0-bg"));
				}

				PageParameters dealsParams = new PageParameters();
				dealsParams.set("id", dealOffer.getId());
				dealsParams.set("name", dealOffer.getTitle());
				String url = (String) urlFor(DealOfferDealsPage.class, dealsParams);
				ExternalLink titleLink = new ExternalLink("titleLink", Model.of(url),
						new PropertyModel<String>(dealOffer, "title"));
				item.add(titleLink);
				
				item.add(new Label("summary"));
				item.add(new Label("price"));
				item.add(new Label("dealType"));
				item.add(new Label("expires"));
				item.add(new Label("isActive"));

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
						MerchantDealOfferPanel panel = new MerchantDealOfferPanel(modal.getContentId(), callback, dealOfferId);
						modal.setContent(panel);
						modal.setTitle("Edit Merchant Deal Offer");
						modal.show(target);
					}
				});
			}

		};

		add(customers);
	}

	@Override
	public String getActionLabel() {
		return "Create Merchant Deal Offer";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback) {
		
		return new MerchantDealOfferPanel(contentId, _merchantId, _merchantAccountId, callback);
	}


}