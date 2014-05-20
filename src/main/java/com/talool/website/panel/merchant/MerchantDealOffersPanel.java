package com.talool.website.panel.merchant;

import java.util.Date;
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
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.DealType;
import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.MerchantLocation;
import com.talool.core.service.ServiceException;
import com.talool.website.models.DealOfferListModel;
import com.talool.website.models.DealOfferModel;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.DealOfferManagementPage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.dealoffer.wizard.DealOfferWizard;
import com.talool.website.util.SessionUtils;

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
	private DealOfferWizard wizard;

	public MerchantDealOffersPanel(String id, PageParameters parameters)
	{
		super(id);
		_merchantId = UUID.fromString(parameters.get("id").toString());
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));

		DealOfferListModel model = new DealOfferListModel();
		model.setMerchantId(_merchantId);
		final ListView<DealOffer> books = new ListView<DealOffer>("offerRptr", model)
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
				String url = (String) urlFor(DealOfferManagementPage.class, dealsParams);
				ExternalLink titleLink = new ExternalLink("titleLink", Model.of(url),
						new PropertyModel<String>(dealOffer, "title"));
				item.add(titleLink);

				item.add(new Label("summary"));
				item.add(new Label("price"));
				item.add(new Label("dealType"));
				//item.add(new Label("expires"));
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
						wizard.setModelObject(new DealOfferModel(dealOfferId).getObject());
						wizard.open(target);
					}
				});
			}

		};

		container.add(books.setOutputMarkupId(true));
		
		final BasePage page = (BasePage)getPage();
		
		// override the action button
		AjaxLink<Void> actionLink = new AjaxLink<Void>("actionLink")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				MerchantAccount merchantAccount = SessionUtils.getSession().getMerchantAccount();
				final DealOffer offer = domainFactory.newDealOffer(merchantAccount.getMerchant(), merchantAccount);

				// set defaults
				offer.setDealType(DealType.PAID_BOOK);
				MerchantLocation offerLocation = offer.getMerchant().getPrimaryLocation();
				offer.setGeometry(offerLocation.getGeometry());
				offer.setScheduledStartDate(new Date());
				offer.setScheduledEndDate(new Date());
				
				try
				{
					Merchant merchant = taloolService.getMerchantById(_merchantId);
					offer.setMerchant(merchant);
				}
				catch(ServiceException se)
				{
					LOG.error("failed to get the merchant by id", se);
				}
				

				wizard.setModelObject(offer);
				wizard.open(target);
			}
		};
		page.setActionLink(actionLink);
		Label actionLabel = new Label("actionLabel", "New Book");
		actionLabel.setOutputMarkupId(true);
		actionLink.add(actionLabel);
		actionLink.setOutputMarkupId(true);

		// Wizard
		wizard = new DealOfferWizard("wiz", "Book Wizard")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target)
			{
				super.onFinish(target);

				target.add(container);

				target.add(page.feedback);
			}
		};
		add(wizard);
	}

	@Override
	public String getActionLabel()
	{
		return "New Book";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return null;
	}
}