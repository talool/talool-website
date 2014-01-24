package com.talool.website.panel.dealoffer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FindDealsPreviewUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
	
	private static final long serialVersionUID = 6447053015807823998L;
	private static final Logger LOG = LoggerFactory.getLogger(FindDealsPreviewUpdatingBehavior.class);
	
	public static enum FindDealsComponent {TITLE, SUMMARY, PRICE, BACKGROUND, ICON};
	private FindDealsPreview preview;
	private FindDealsComponent component;

	public FindDealsPreviewUpdatingBehavior(FindDealsPreview preview, FindDealsComponent component, String event) {
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
			preview.price = getFormComponent().getValue();
			target.add(preview.priceLabel);
			break;
		case BACKGROUND:
			preview.dealOfferBackgroundUrl = getFormComponent().getValue();
			target.add(preview.background);
			break;
		case ICON:
			preview.dealOfferIconUrl = getFormComponent().getValue();
			target.add(preview.icon);
			break;

		}
		
		
	}

}
