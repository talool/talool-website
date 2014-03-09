package com.talool.website.panel.dealoffer.wizard;

import java.util.Date;

import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.DealOffer;
import com.talool.website.component.DateTimeFieldExtended;
import com.talool.website.panel.dealoffer.DealOfferPreview;
import com.talool.website.panel.dealoffer.DealOfferPreviewUpdatingBehavior;
import com.talool.website.panel.dealoffer.DealOfferPreviewUpdatingBehavior.DealOfferComponent;

/**
 * 
 * @author dmccuen,clintz
 * 
 */
public class DealOfferDetails extends WizardStep
{
	private static final long serialVersionUID = 1L;

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

		DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);

		// DateTextField startDateField = new DateTextField("scheduledStartDate",
		// converter);
		// addOrReplace(startDateField);
		//
		// DateTextField endDateField = new DateTextField("scheduledEndDate",
		// converter);
		// addOrReplace(endDateField);

		// start date must be at least today

		DateTimeFieldExtended start = new DateTimeFieldExtended("scheduledStartDate");

		DateTimeFieldExtended end = new DateTimeFieldExtended("scheduledEndDate");

		// add(new StartEndDateFormValidator(start, end));

		addOrReplace(end);

		addOrReplace(start);

	}
}
