package com.talool.website.panel.message.wizard;

import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.FactoryManager;
import com.talool.core.service.CustomerService;
import com.talool.core.service.ServiceException;
import com.talool.website.models.DealOfferModel;
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
		String gender = (mg.getCriteria().getSex() == null)?"Any":mg.getCriteria().getSex().name();

		DealOfferModel dom = new DealOfferModel(mg.getCriteria().getDealOfferId());
		descriptionPanel.add(new Label("count",getCustomerCount()));
		descriptionPanel.add(new Label("book",dom.getObject().getTitle()));
		descriptionPanel.add(new Label("gender",gender));
		descriptionPanel.add(new Label("minAge",getAgeForDate(mg.getCriteria().getOlderThan())));
		descriptionPanel.add(new Label("maxAge",getAgeForDate(mg.getCriteria().getYoungerThan())));
		
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
	
	private String getAgeForDate(Date date)
	{
		if (date==null)
		{
			return "Any";
		}
		else
		{
			Calendar c = Calendar.getInstance();
			int currentYear = c.get(Calendar.YEAR);
			c.setTime(date);
			int birthYear = c.get(Calendar.YEAR);
			Integer age = currentYear - birthYear;
			return age.toString();
		}
		
	}

}
