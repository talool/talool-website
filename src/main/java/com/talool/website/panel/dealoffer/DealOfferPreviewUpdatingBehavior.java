package com.talool.website.panel.dealoffer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DealOfferPreviewUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
	
	private static final long serialVersionUID = 6447053015807823998L;
	private static final Logger LOG = LoggerFactory.getLogger(DealOfferPreviewUpdatingBehavior.class);
	
	public static enum DealOfferComponent {TITLE, SUMMARY, PRICE, BACKGROUND};
	private DealOfferPreview preview;
	private DealOfferComponent component;

	public DealOfferPreviewUpdatingBehavior(DealOfferPreview preview, DealOfferComponent component, String event) {
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
		case PRICE:
			preview.setPrice(getFormComponent().getValue());
			target.add(preview.priceLabel);
			break;
		case BACKGROUND:
			preview.dealOfferBackgroundUrl = getFormComponent().getValue();
			target.add(preview.background);
			break;

		}
		
		
	}

}
