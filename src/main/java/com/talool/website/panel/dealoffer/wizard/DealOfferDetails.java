package com.talool.website.panel.dealoffer.wizard;

import java.util.Date;
import java.util.TimeZone;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
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
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author dmccuen,clintz
 * 
 */
public class DealOfferDetails extends WizardStep
{
	private static final Logger LOG = LoggerFactory.getLogger(DealOfferDetails.class);
	private static final long serialVersionUID = 1L;

	private String selectedTimeZoneId;

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
		
		final DateTimePicker start = new DateTimePicker("scheduledStartDate");
		addOrReplace(start.setOutputMarkupId(true));
		
		final DateTimePicker end = new DateTimePicker("scheduledEndDate");
		addOrReplace(end.setOutputMarkupId(true));
		
		//DateConverter converter = new PatternDateConverter("MM/dd/yyyy", true);
		// start date must be at least today
		// add(new StartEndDateFormValidator(start, end));
		
		TimeZone bestGuessTimeZone = SessionUtils.getSession().getBestGuessTimeZone();
		selectedTimeZoneId = TimeZoneDropDown.getBestSupportedTimeZone(bestGuessTimeZone).getID();
		TimeZoneDropDown timeZoneDropDown = new TimeZoneDropDown("timeZoneSelect", new PropertyModel<String>(this, "selectedTimeZoneId"));
		timeZoneDropDown.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 8587109070528314926L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				SessionUtils.getSession().setAndPersistTimeZone(selectedTimeZoneId);
				target.add(end);
				target.add(start);
			}

		});
		addOrReplace(timeZoneDropDown);

	}
}
