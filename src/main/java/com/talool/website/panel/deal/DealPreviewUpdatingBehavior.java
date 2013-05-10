package com.talool.website.panel.deal;

import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.FactoryManager;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;


public class DealPreviewUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
	
	private static final long serialVersionUID = 6447053015807823998L;
	private static final Logger LOG = LoggerFactory.getLogger(DealPreviewUpdatingBehavior.class);
	
	public static enum DealComponent {TITLE, SUMMARY, DETAILS, IMAGE, EXPIRES, CODE, MERCHANT, DEAL_OFFER};
	private DealPreview preview;
	private DealComponent component;
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public DealPreviewUpdatingBehavior(DealPreview preview, DealComponent component, String event) {
		super(event);
		this.preview = preview;
		this.component = component;
	}
	
	@Override
	protected void onUpdate(AjaxRequestTarget target) {
		switch (component) {
		case TITLE: 
			preview.title = getFormComponent().getValue();
			target.add(preview.titleLabel);
			break;
		case SUMMARY:
			preview.summary = getFormComponent().getValue();
			target.add(preview.summaryLabel);
			break;
		case DETAILS:
			preview.details = getFormComponent().getValue();
			target.add(preview.detailsLabel);
			break;
		case IMAGE:
			preview.imageUrl = getFormComponent().getValue();
			target.add(preview.image);
			break;
		case EXPIRES:
			preview.expires = getFormComponent().getValue();
			target.add(preview.expiresLabel);
			break;
		case CODE:
			preview.code = getFormComponent().getValue();
			//target.add(preview.codeLabel);
			break;
		case MERCHANT:
			break;
		case DEAL_OFFER:
			DealOffer offer = getDealOffer(getFormComponent().getValue());
			if (offer != null)
			{
				preview.dealOfferLogoUrl = offer.getImage().getMediaUrl();
				target.add(preview.dealOfferLogo);
			}
			break;
		}
		
		
	}
	
	private DealOffer getDealOffer(String id)
	{
		UUID dealOfferId = UUID.fromString(id);
		DealOffer offer = null;
		try
		{
			offer = taloolService.getDealOffer(dealOfferId);
		}
		catch(ServiceException se)
		{
			LOG.error("failed to get Deal Offer",se);
		}
		return offer;
	}
	


}
