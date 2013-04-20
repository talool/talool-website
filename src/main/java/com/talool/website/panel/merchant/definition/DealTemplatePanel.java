package com.talool.website.panel.merchant.definition;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import com.talool.core.Deal;

abstract public class DealTemplatePanel extends Panel {

	private static final long serialVersionUID = -1923977191068407384L;
	protected DealPreview dealPreview;

	public DealTemplatePanel(String id, DealPreview preview, CompoundPropertyModel<Deal> model) {
		super(id);
		dealPreview = preview;
		setDefaultModel(model);
		setMarkupId(id);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		// TODO add checkbox for "valid anytime"
		
		TextArea<String> detailsField = new TextArea<String>("details");
		detailsField.add(new DealPreviewUpdatingBehavior(dealPreview,
				DealPreviewUpdatingBehavior.DealComponent.DETAILS, "onChange"));
		detailsField.setRequired(true);
		add(detailsField);

		 
	}

}
