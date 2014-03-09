package com.talool.website.panel.dealoffer;

import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.talool.core.DealOffer;
import com.talool.core.DealType;
import com.talool.core.MerchantLocation;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.service.ServiceFactory;
import com.talool.stats.DealOfferMetrics.MetricType;
import com.talool.stats.DealOfferSummary;
import com.talool.website.component.ToggleButton;
import com.talool.website.component.ToggleButton.ToggleLabelType;

public class DealOfferPublishToggle extends Panel
{

	private static final long serialVersionUID = 8801810968448950258L;
	private static final Logger LOG = Logger.getLogger(DealOfferPublishToggle.class);

	public DealOfferPublishToggle(String id, IModel<DealOfferSummary> model)
	{
		super(id);
		setDefaultModel(model);
	}

	public DealOfferPublishToggle(String id, DealOffer offer, MerchantLocation loc, Map<String, Long> metrics)
	{
		super(id);

		DealOfferSummary summary = new DealOfferSummary();
		summary.setOfferId(offer.getId());
		summary.setOfferType(offer.getType().toString());
		summary.setIsActive(offer.isActive());
		summary.setScheduledEndDate(offer.getScheduledEndDate());
		summary.setScheduledStartDate(offer.getScheduledStartDate());

		if (loc != null)
		{
			summary.setAddress1(loc.getAddress1());
			summary.setAddress2(loc.getAddress2());
			summary.setCity(loc.getCity());
			summary.setState(loc.getStateProvinceCounty());
		}
		if (metrics != null)
		{
			summary.setDealCount(metrics.get(MetricType.TotalDeals.toString()));
			summary.setMerchantCount(metrics.get(MetricType.TotalMerchants.toString()));
		}
		IModel<DealOfferSummary> model = new Model<DealOfferSummary>(summary);
		setDefaultModel(model);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final DealOfferSummary dealOffer = (DealOfferSummary) getDefaultModelObject();

		AttributeAppender warning = new AttributeAppender("class", "true-gray-font");

		if (dealOffer.getOfferType().equals(DealType.KIRKE_BOOK.toString()))
		{
			// we can let users enable kirke books
			add(new Label("toggle", "unpublishable").add(warning));
		}

		else if (dealOffer.getCity() == null)
		{
			// require a location
			add(new Label("toggle", "missing location").add(warning));
		}
		else if (!dealOffer.isCurrentlyScheduled() && dealOffer.getScheduledStartDate().getTime() > Calendar.getInstance().getTime().getTime())
		{
			add(new Label("toggle", "scheduled").add(warning));
		}
		else if (!dealOffer.isCurrentlyScheduled() && System.currentTimeMillis() > dealOffer.getScheduledEndDate().getTime())
		{
			add(new Label("toggle", "schedule ended").add(warning));
		}
		else if (isOfferReadyToPublish(dealOffer))
		{
			add(new ToggleButton("toggle", new Model<Boolean>(dealOffer.getIsActive()), ToggleLabelType.PUBLISH)
			{

				private static final long serialVersionUID = -2508151345085039614L;

				@Override
				public void onToggle(AjaxRequestTarget target)
				{
					try
					{
						TaloolService taloolService = ServiceFactory.get().getTaloolService();
						DealOffer offer = taloolService.getDealOffer(dealOffer.getOfferId());
						offer.setActive(!offer.isActive());
						taloolService.merge(offer);
						onPublishToggle(target);
					}
					catch (ServiceException se)
					{
						LOG.error("problem setting isActive flag for deal offer", se);
						Session.get().error("There was a problem saving " + dealOffer.getTitle() + ".  Contact us if you want it updated manually.");
					}
				}

			});
		}
		else
		{
			add(new Label("toggle", "empty book").add(warning));
		}

	}

	public void onPublishToggle(AjaxRequestTarget target)
	{
		// override as needed
	}

	private boolean isOfferReadyToPublish(DealOfferSummary offer)
	{
		// Do we need to validate anything else?
		boolean b = false;
		try
		{
			b = (offer.getMerchantCount() > 0 && offer.getDealCount() > 0);
		}
		catch (Exception e)
		{
			// meta data not ready
		}
		return b;
	}

}
