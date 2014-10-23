package com.talool.website.panel.message.wizard;

import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.ResourceModel;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.talool.core.FactoryManager;
import com.talool.core.service.CustomerService;
import com.talool.core.service.ServiceException;
import com.talool.website.models.DealOfferModel;
import com.talool.website.panel.message.MessageJobPojo;
import com.talool.website.util.SessionUtils;

public class MessageConfirmationStep extends DynamicWizardStep
{

	private static final long serialVersionUID = 1L;
	
	protected transient static final CustomerService customerService = FactoryManager.get()
			.getServiceFactory().getCustomerService();

	public MessageConfirmationStep(IDynamicWizardStep previousStep)
	{
		super(previousStep, new ResourceModel("title"), new ResourceModel("summary"));
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();
		final MessageJobPojo mg = (MessageJobPojo) getDefaultModelObject();
		
		WebMarkupContainer descriptionPanel = new WebMarkupContainer("descriptionPanel");
		addOrReplace(descriptionPanel.setOutputMarkupId(true));
		String gender = (mg.getCriteria().getSex() == null)?"Any":mg.getCriteria().getSex().name();

		DealOfferModel dom = new DealOfferModel(mg.getCriteria().getDealOfferId());
		descriptionPanel.add(new Label("title",mg.getTitle()));
		descriptionPanel.add(new Label("count",getCustomerCount()));
		descriptionPanel.add(new Label("book",dom.getObject().getTitle()));
		descriptionPanel.add(new Label("gender",gender));
		descriptionPanel.add(new Label("minAge",getAgeForDate(mg.getCriteria().getOlderThan())));
		descriptionPanel.add(new Label("maxAge",getAgeForDate(mg.getCriteria().getYoungerThan())));
		
		DateTimeZone tz = DateTimeZone.forTimeZone(SessionUtils.getSession().getBestGuessTimeZone());
		DateTime localDate = new DateTime(mg.getStartDate().getTime(), tz);
		DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM d, yyyy h:mm a z");
		
		String startDate = formatter.print(localDate);
		descriptionPanel.add(new Label("date",startDate));
		
	}
	
	private long getCustomerCount()
	{
		long customerCount;
		// call service to get the list of customers
		try
		{
			final MessageJobPojo mg = (MessageJobPojo) getDefaultModelObject();
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

	@Override
	public boolean isLastStep() {
		return true;
	}

	@Override
	public IDynamicWizardStep next() {
		return null;
	}

}
