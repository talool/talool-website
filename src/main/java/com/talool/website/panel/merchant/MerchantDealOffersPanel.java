package com.talool.website.panel.merchant;

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
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.website.models.DealOfferListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.lists.DealOfferDealsPage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.MerchantDealOfferPanel;

/**
 * 
 * @author clintz,dmccuen
 * 
 */
public class MerchantDealOffersPanel extends BaseTabPanel
{
	private static final Logger LOG = LoggerFactory.getLogger(MerchantDealOffersPanel.class);
	private static final long serialVersionUID = 3634980968241854373L;
	private UUID _merchantId;

	public MerchantDealOffersPanel(String id, PageParameters parameters)
	{
		super(id);
		_merchantId = UUID.fromString(parameters.get("id").toString());
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
				final UUID dealOfferId = dealOffer.getId();

				item.setModel(new CompoundPropertyModel<DealOffer>(dealOffer));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
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
						MerchantDealOfferPanel panel = new MerchantDealOfferPanel(modal.getContentId(),
								callback, dealOfferId);
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
	public String getActionLabel()
	{
		return "Create Deal Offer";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		Merchant merch;
		try
		{
			merch = taloolService.getMerchantById(_merchantId);
			return new MerchantDealOfferPanel(contentId, domainFactory.newMerchantIdentity(merch.getId(),
					merch.getName()), callback);
		}
		catch (ServiceException e)
		{
			LOG.error("Problem getting merchant " + _merchantId, e);
		}

		return null;

	}
}