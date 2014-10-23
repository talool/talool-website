package com.talool.website.panel.message.wizard;

import java.util.Date;
import java.util.TimeZone;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.wicket.kendo.ui.form.datetime.DateTimePicker;
import com.talool.core.FactoryManager;
import com.talool.core.service.CustomerService;
import com.talool.website.component.TimeZoneDropDown;
import com.talool.website.panel.message.MessageJobPojo;
import com.talool.website.util.CssClassToggle;
import com.talool.website.util.SessionUtils;
import com.talool.website.util.TaloolDateUtil;

public class JobScheduleStep extends DynamicWizardStep
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(JobScheduleStep.class);
	protected transient static final CustomerService customerService = FactoryManager.get()
			.getServiceFactory().getCustomerService();
	
	private String selectedTimeZoneId, lastTimeZoneId;
	private DateTimePicker start;
	
	public Date date = new Date();

	public JobScheduleStep(IDynamicWizardStep previousStep)
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
		
		TextField<String> title = new TextField<String>("title");
		descriptionPanel.addOrReplace(title.setRequired(true));
		
		TimeZone bestGuessTimeZone = SessionUtils.getSession().getBestGuessTimeZone();
		selectedTimeZoneId = TimeZoneDropDown.getBestSupportedTimeZone(bestGuessTimeZone).getID();
		lastTimeZoneId = selectedTimeZoneId;
		LOG.debug("init with timezone: " + selectedTimeZoneId);

		// convert the start dates to merchant_account timezone, for display
		Date startDate = TaloolDateUtil.convertTimeZone(mg.getStartDate(), TimeZone.getTimeZone(selectedTimeZoneId), TimeZone.getDefault());
		start = new DateTimePicker("scheduledStartDate", Model.of(startDate));
		descriptionPanel.addOrReplace(start.setOutputMarkupId(true));
		
		final TimeZoneDropDown timeZoneDropDown = new TimeZoneDropDown("timeZoneSelect", new PropertyModel<String>(this, "selectedTimeZoneId"));
		timeZoneDropDown.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 8587109070528314926L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				SessionUtils.getSession().setAndPersistTimeZone(selectedTimeZoneId);

				// adjust from old TZ to new TZ
				Date startDate = TaloolDateUtil.convertTimeZone(start.getModelObject(), TimeZone.getTimeZone(selectedTimeZoneId), TimeZone.getTimeZone(lastTimeZoneId));
				lastTimeZoneId = selectedTimeZoneId;
				start.setModelObject(startDate);
				target.add(start);
			}

		});
		descriptionPanel.addOrReplace(timeZoneDropDown.setOutputMarkupId(true));

		final WebMarkupContainer currentTimeZone = new WebMarkupContainer("currentTimeZone");
		descriptionPanel.addOrReplace(currentTimeZone.setOutputMarkupId(true));
		currentTimeZone.add(new Label("currentTimeZoneLabel", selectedTimeZoneId));
		currentTimeZone.add(new AjaxLink<Void>("changeTimeZone")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				timeZoneDropDown.add(new CssClassToggle("hide", "show"));
				target.add(timeZoneDropDown);
				currentTimeZone.setVisible(false);
				target.add(currentTimeZone);
			}

		});
	}
	
	@Override
	public void applyState()
	{
		super.applyState();

		final MessageJobPojo mg = (MessageJobPojo) getDefaultModelObject();

		// get start/end dates from pickers and convert back to UTC
		Date startDate = TaloolDateUtil.convertTimeZone(start.getModelObject(), TimeZone.getDefault(), TimeZone.getTimeZone(selectedTimeZoneId));
		mg.setStartDate(startDate);
	}

	@Override
	public boolean isLastStep() {
		return false;
	}

	@Override
	public IDynamicWizardStep next() {
		return new MessageConfirmationStep(this);
	}
	
	@Override
	public IDynamicWizardStep last()
	{
		return new MessageConfirmationStep(this);
	}

}
