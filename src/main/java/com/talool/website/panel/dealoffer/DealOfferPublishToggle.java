package com.talool.website.panel.dealoffer;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.joda.time.DateTime;

import com.talool.core.DealOffer;
import com.talool.core.DealType;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.service.ServiceFactory;
import com.talool.stats.DealOfferSummary;
import com.talool.website.component.ToggleButton;
import com.talool.website.component.ToggleButton.ToggleLabelType;

public class DealOfferPublishToggle extends Panel {

	private static final long serialVersionUID = 8801810968448950258L;
	private static final Logger LOG = Logger.getLogger(DealOfferPublishToggle.class);

	public DealOfferPublishToggle(String id, IModel<DealOfferSummary> model) {
		super(id, model);
		setDefaultModel(model);
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		final DealOfferSummary dealOffer = (DealOfferSummary) getDefaultModelObject();
		
		boolean isExpired = false;
		if (dealOffer.getExpires() != null)
		{
			DateTime localDate = new DateTime(dealOffer.getExpires().getTime());
			isExpired = localDate.isBeforeNow();
		}
		
		if (dealOffer.getOfferType().equals(DealType.KIRKE_BOOK.toString()))
		{
			// we can let users enable kirke books
			add(new Label("toggle", "unpublishable"));
		}
		else if (dealOffer.getExpires() == null)
		{
			// require expiration date
			add(new Label("toggle", "missing expiration date"));
		}
		else if (isExpired)
		{
			add(new Label("toggle", "expired"));
		}
		else if (dealOffer.getCity() == null)
		{
			// require a location
			add(new Label("toggle", "missing location"));
		}
		else if (isOfferReadyToPublish(dealOffer))
		{
			add(new ToggleButton("toggle", new Model<Boolean>(dealOffer.getIsActive()), ToggleLabelType.YES_NO){

				private static final long serialVersionUID = -2508151345085039614L;

				@Override
				public void onToggle(AjaxRequestTarget target) {
					try
					{
						TaloolService taloolService = ServiceFactory.get().getTaloolService();
						DealOffer offer = taloolService.getDealOffer(dealOffer.getOfferId());
						offer.setActive(!offer.isActive());
						taloolService.merge(offer);
					}
					catch (ServiceException se)
					{
						LOG.error("problem setting isActive flag for deal offer", se);
						Session.get().error("There was a problem saving " +dealOffer.getTitle() + ".  Contact us if you want it updated manually.");
					}
				}
				
			});
		}
		else
		{
			add(new Label("toggle", "empty book"));
		}
		
	}
	
	private boolean isOfferReadyToPublish(DealOfferSummary offer)
	{
		// Do we need to validate anything else?
		boolean b = false;
		try {
			b = (offer.getMerchantCount() > 0 && offer.getDealCount() > 0);
		}
		catch (Exception e)
		{
			// meta data not ready
		}
		return b;
	}

}
