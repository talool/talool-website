package com.talool.website.panel.merchant;

import java.util.Date;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.MediaType;
import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.MerchantMedia;
import com.talool.core.service.ServiceException;
import com.talool.website.component.DealDeleteLink;
import com.talool.website.models.DealListModel;
import com.talool.website.models.DealModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.deal.wizard.DealWizard;
import com.talool.website.panel.image.EditableImage;
import com.talool.website.util.SessionUtils;

public class MerchantDealsPanel extends BaseTabPanel
{
	private static final long serialVersionUID = 3634980968241854373L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantDealsPanel.class);
	private static final String talool = "Talool";
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

		// TODO make this a paginated list with DealSummary objects
		final ListView<Deal> customers = new ListView<Deal>("dealRptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Deal> item)
			{
				final Deal deal = item.getModelObject();
				final UUID dealId = deal.getId();

				// Hibernate.initialize(deal);
				// Hibernate.initialize(deal.getMerchant());

				item.setModel(new CompoundPropertyModel<Deal>(deal));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				item.add(new Label("title"));
				item.add(new Label("summary"));
				item.add(new Label("details"));
				
				String url = null;
				if (deal.getImage() != null) 
				{
					url = deal.getImage().getMediaUrl();
				}
				item.add(new EditableImage("editableImage",Model.of(url), _merchantId, MediaType.DEAL_IMAGE)
				{

					private static final long serialVersionUID = 1L;

					@Override
					public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media) 
					{
						try
						{
							deal.setImage(media);
							taloolService.merge(deal);
							target.add(this);
						}
						catch (ServiceException se)
						{
							LOG.error("Failed to save new image with managedLocation",se);
						}
						
					}
					
				});
				
				Date exp = deal.getExpires();
				if (exp != null)
				{
					DateTime localDate = new DateTime(exp.getTime());
					DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM d, yyyy");
					String expDate = formatter.print(localDate);
					item.add(new Label("expires",expDate));
				}
				else
				{
					item.add(new Label("expires"));
				}
				
				
				item.add(new Label("isActive"));
				item.add(new Label("dealOffer.title"));

				item.add(new AjaxLink<Void>("editLink")
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						wizard.setModelObject(new DealModel(dealId, true).getObject());
						wizard.open(target);
					}
				});
				
				final BasePage page = (BasePage)getPage();
				DealDeleteLink deleteLink = new DealDeleteLink("deleteLink", deal)
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onDeleteComplete(boolean success,
							AjaxRequestTarget target) {
						if (success) target.add(container);
						target.add(page.feedback);
					}
				};
				item.add(deleteLink);
			}

		};
		container.add(customers);

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
				Deal deal = domainFactory.newDeal(_merchantId, ma, true);
				try
				{
					Merchant merchant = taloolService.getMerchantById(_merchantId);
					deal.setMerchant(merchant);
				}
				catch (ServiceException se)
				{
					LOG.error("failed to find merchant for new deal:", se);
				}

				wizard.setModelObject(deal);
				wizard.open(target);
			}
		};
		page.setActionLink(actionLink);
		Label actionLabel = new Label("actionLabel", getActionLabel());
		actionLabel.setOutputMarkupId(true);
		actionLink.add(actionLabel);
		actionLink.setOutputMarkupId(true);

		// Wizard
		wizard = new DealWizard("dealWiz", "Deal Wizard")
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
		addOrReplace(wizard.setOutputMarkupId(true));

	}

	@Override
	public String getActionLabel()
	{
		return "Create Deal";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		// intentionally null
		return null;
	}

}