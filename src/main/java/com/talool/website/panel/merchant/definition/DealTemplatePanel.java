package com.talool.website.panel.merchant.definition;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import com.talool.core.Deal;

abstract public class DealTemplatePanel extends Panel {

	private static final long serialVersionUID = -1923977191068407384L;
	protected DealPreview dealPreview;
	private HiddenField<String> title, summary;

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
		
		title = new HiddenField<String>("title");
		add(title.setOutputMarkupId(true));
		summary = new HiddenField<String>("summary");
		add(summary.setOutputMarkupId(true));
		
		// TODO add checkbox for "valid anytime"
		
		TextArea<String> detailsField = new TextArea<String>("details");
		detailsField.add(new DealPreviewUpdatingBehavior(dealPreview,
				DealPreviewUpdatingBehavior.DealComponent.DETAILS, "onChange"));
		detailsField.setRequired(true);
		add(detailsField);

		 
	}
	
	protected void syncTitle(AjaxRequestTarget target) {
		title.setModelValue(new String[]{cookUpTitle()});
		dealPreview.title = title.getValue();
		target.add(dealPreview.titleLabel);
		target.add(title);
	}
	
	protected void syncSummary(AjaxRequestTarget target) {
		summary.setModelValue(new String[]{cookUpSummary()});
		dealPreview.summary = summary.getValue();
		target.add(dealPreview.summaryLabel);
		target.add(summary);
	}
	
	abstract String cookUpTitle();
	abstract String cookUpSummary();

}
