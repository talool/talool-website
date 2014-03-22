package com.talool.website.panel.dealoffer.wizard;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.wicket.kendo.ui.form.datetime.DateTimePicker;
import com.talool.core.DealOffer;
import com.talool.website.component.TimeZoneDropDown;
import com.talool.website.panel.dealoffer.DealOfferPreview;
import com.talool.website.panel.dealoffer.DealOfferPreviewUpdatingBehavior;
import com.talool.website.panel.dealoffer.DealOfferPreviewUpdatingBehavior.DealOfferComponent;
import com.talool.website.util.CssClassToggle;
import com.talool.website.util.SessionUtils;
import com.talool.website.validators.StartEndDateFormValidator;

/**
 * 
 * @author dmccuen,clintz
 * 
 */
public class DealOfferDetails extends WizardStep
{
	private static final Logger LOG = LoggerFactory.getLogger(DealOfferDetails.class);
	private static final long serialVersionUID = 1L;

	private String selectedTimeZoneId, lastTimeZoneId;
	private DateTimePicker start, end;

	public Date date = new Date();

	public DealOfferDetails()
	{
		super(new ResourceModel("title"), new ResourceModel("summary"));
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();

		final DealOffer offer = (DealOffer) getDefaultModelObject();

		final DealOfferPreview offerPreview = new DealOfferPreview("offerBuilder", offer);
		offerPreview.setOutputMarkupId(true);
		addOrReplace(offerPreview);

		TextField<String> title = new TextField<String>("title");
		addOrReplace(title.setRequired(true));
		title.add(new DealOfferPreviewUpdatingBehavior(offerPreview, DealOfferComponent.TITLE, "onChange"));

		TextArea<String> summary = new TextArea<String>("summary");
		addOrReplace(summary.setRequired(true));
		summary.add(new DealOfferPreviewUpdatingBehavior(offerPreview, DealOfferComponent.SUMMARY, "onChange"));

		TextField<String> price = new TextField<String>("price");
		addOrReplace(price.setRequired(true));
		price.add(new DealOfferPreviewUpdatingBehavior(offerPreview, DealOfferComponent.PRICE, "onChange"));

		TimeZone bestGuessTimeZone = SessionUtils.getSession().getBestGuessTimeZone();
		selectedTimeZoneId = TimeZoneDropDown.getBestSupportedTimeZone(bestGuessTimeZone).getID();
		lastTimeZoneId = selectedTimeZoneId;
		LOG.debug("init with timezone: " + selectedTimeZoneId);

		// convert the start/end dates to merchant_account timezone, for display
		Date endDate = convertTimeZone(offer.getScheduledEndDate(), TimeZone.getTimeZone(selectedTimeZoneId), TimeZone.getDefault());
		Date startDate = convertTimeZone(offer.getScheduledStartDate(), TimeZone.getTimeZone(selectedTimeZoneId), TimeZone.getDefault());
		end = new DateTimePicker("scheduledEndDate", Model.of(endDate));
		start = new DateTimePicker("scheduledStartDate", Model.of(startDate));
		addOrReplace(end.setOutputMarkupId(true));
		addOrReplace(start.setOutputMarkupId(true));

		// start date must be at least today
		add(new StartEndDateFormValidator(start, end));

		final TimeZoneDropDown timeZoneDropDown = new TimeZoneDropDown("timeZoneSelect", new PropertyModel<String>(this, "selectedTimeZoneId"));
		timeZoneDropDown.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 8587109070528314926L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				SessionUtils.getSession().setAndPersistTimeZone(selectedTimeZoneId);

				// adjust from old TZ to new TZ
				Date endDate = convertTimeZone(end.getModelObject(), TimeZone.getTimeZone(selectedTimeZoneId), TimeZone.getTimeZone(lastTimeZoneId));
				Date startDate = convertTimeZone(start.getModelObject(), TimeZone.getTimeZone(selectedTimeZoneId), TimeZone.getTimeZone(lastTimeZoneId));
				lastTimeZoneId = selectedTimeZoneId;
				end.setModelObject(endDate);
				start.setModelObject(startDate);
				target.add(start);
				target.add(end);
			}

		});
		addOrReplace(timeZoneDropDown.setOutputMarkupId(true));

		final WebMarkupContainer currentTimeZone = new WebMarkupContainer("currentTimeZone");
		addOrReplace(currentTimeZone.setOutputMarkupId(true));
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

		final DealOffer offer = (DealOffer) getDefaultModelObject();

		// get start/end dates from pickers and convert back to UTC
		Date endDate = convertTimeZone(end.getModelObject(), TimeZone.getDefault(), TimeZone.getTimeZone(selectedTimeZoneId));
		Date startDate = convertTimeZone(start.getModelObject(), TimeZone.getDefault(), TimeZone.getTimeZone(selectedTimeZoneId));
		offer.setScheduledEndDate(endDate);
		offer.setScheduledStartDate(startDate);
	}

	private Date convertTimeZone(Date date, TimeZone toTimeZone, TimeZone fromTimeZone)
	{
		// get the offset
		int millisInHour = (1000 * 60 * 60);
		int fromOffset = fromTimeZone.getOffset(date.getTime()) / millisInHour;
		LOG.info("From Offset of " + fromTimeZone + " is " + fromOffset);
		int toOffset = toTimeZone.getOffset(date.getTime()) / millisInHour;
		LOG.info("To Offset of " + toTimeZone + " is " + toOffset);
		int offset = toOffset - fromOffset;

		// convert the date
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, offset);
		return c.getTime();
	}

}
