package com.talool.website.panel.message.wizard;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.FactoryManager;
import com.talool.core.service.ServiceException;
import com.talool.website.models.DealListModel;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.message.MerchantGift;

public class MessageGiftStep extends WizardStep
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MessageGiftStep.class);
	private String dealId;

	public MessageGiftStep()
	{
		super(new ResourceModel("title"), new ResourceModel("summary"));
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();
		
		final MerchantGift mg = (MerchantGift) getDefaultModelObject();
		
		DealListModel dealsModel = new DealListModel();
		dealsModel.setMerchantId(mg.getMerchant().getId());
		
		if (mg.getDeal() == null)
		{
			try
			{
				// preselect the first item in the list
				Deal deal = dealsModel.getObject().get(0);
				mg.setDeal(deal);
				dealId = deal.getId().toString();
			}
			catch (NullPointerException npe)
			{
				// TODO don't let the merchant create a message until they have created a deal
			}
		}
		else
		{
			dealId = mg.getDeal().getId().toString();
		}
		
		final WebMarkupContainer descriptionPanel = new WebMarkupContainer("descriptionPanel");
		addOrReplace(descriptionPanel.setOutputMarkupId(true));
		
		final HiddenField<String> hf = new HiddenField<String>("dealId", new PropertyModel<String>(this,"dealId"));
		hf.setRequired(true).setOutputMarkupId(true);
		descriptionPanel.add(hf);
		
		// display a deal/gift preview and update it on selection
		final DealPreview dealPreview = new DealPreview("dealBuilder", mg.getDeal());
		dealPreview.setOutputMarkupId(true);
		addOrReplace(dealPreview);
				
		final ListView<Deal> deals = new ListView<Deal>(
				"dealRepeater", dealsModel)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Deal> item)
			{
				final Deal deal = item.getModelObject();

				item.setModel(new CompoundPropertyModel<Deal>(deal));
				
				if (deal.getId().toString().equalsIgnoreCase(dealId))
				{
					item.add(new AttributeAppender("class", " selected "));
				}
				
				item.add(new Label("title"));
				item.add(new Label("summary"));
				
				item.add(new AjaxEventBehavior("click") {
					private static final long serialVersionUID = 1L;

					protected void onEvent(AjaxRequestTarget target) {
						try
						{
							FactoryManager.get().getServiceFactory().getTaloolService().reattach(deal);
						}
						catch (ServiceException se)
						{
							LOG.error("Failed to reattach a deal",se);
						}
		                mg.setDeal(deal);
		                dealId = deal.getId().toString();
		                target.add(hf);
		                // toggle css to show selection
		                target.appendJavaScript("window.oo.toggleSelected('.deal_for_gift','#"+item.getMarkupId()+"')");
		                // update the preview
		                dealPreview.init(deal);
		                target.add(dealPreview);
		             } 
				});
			}
		};
		descriptionPanel.add(deals);
	}

}
