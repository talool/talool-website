package com.talool.website.panel.merchant.definition;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import com.talool.core.Deal;

public class OtherDealTemplatePanel extends DealTemplatePanel {

	private static final long serialVersionUID = 4682200089094814554L;
	
	public OtherDealTemplatePanel(String id, DealPreview preview, CompoundPropertyModel<Deal> model) {
		super(id, preview, model);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		TextField<String> titleField = new TextField<String>("title");
		titleField.add(new DealPreviewUpdatingBehavior(dealPreview,
				DealPreviewUpdatingBehavior.DealComponent.TITLE, "onChange"));
		titleField.setRequired(true);
		add(titleField);
		
		TextArea<String> summaryField = new TextArea<String>("summary");
		summaryField.add(new DealPreviewUpdatingBehavior(dealPreview,
				DealPreviewUpdatingBehavior.DealComponent.SUMMARY, "onChange"));
		summaryField.setRequired(true);
		add(summaryField);
		 
	}



}
