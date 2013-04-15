package com.talool.website.panel.merchant.definition;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

public class DealPreviewUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
	
	private static final long serialVersionUID = 6447053015807823998L;

	public static enum DealComponent {TITLE, SUMMARY, DETAILS, IMAGE, EXPIRES, CODE, MERCHANT, DEAL_OFFER};
	private DealPreview preview;
	private DealComponent component;

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
			break;
		}
		
		
	}
	


}
