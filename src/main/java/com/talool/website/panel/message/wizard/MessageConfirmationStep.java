package com.talool.website.panel.message.wizard;

import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.FactoryManager;
import com.talool.core.service.CustomerService;
import com.talool.core.service.ServiceException;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.message.MerchantGift;

public class MessageConfirmationStep extends WizardStep
{

	private static final long serialVersionUID = 1L;
	
	protected transient static final CustomerService customerService = FactoryManager.get()
			.getServiceFactory().getCustomerService();

	public MessageConfirmationStep()
	{
		super(new ResourceModel("title"), new ResourceModel("summary"));
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();
		final MerchantGift mg = (MerchantGift) getDefaultModelObject();
		
		WebMarkupContainer descriptionPanel = new WebMarkupContainer("descriptionPanel");
		addOrReplace(descriptionPanel.setOutputMarkupId(true));

		descriptionPanel.add(new Label("title",mg.getDeal().getTitle()));
		descriptionPanel.add(new Label("count",getCustomerCount()));
		
		// display a deal/gift preview and update it on selection
		final DealPreview dealPreview = new DealPreview("dealBuilder", mg.getDeal());
		dealPreview.setOutputMarkupId(true);
		addOrReplace(dealPreview);
		
	}
	
	private long getCustomerCount()
	{
		long customerCount;
		// call service to get the list of customers
		try
		{
			final MerchantGift mg = (MerchantGift) getDefaultModelObject();
			customerCount = customerService.getCustomerCount(mg.getCriteria());
		}
		catch (ServiceException se)
		{
			customerCount = 0l;
		}
		return customerCount;
	}

}
