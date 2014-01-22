package com.talool.website.panel.deal.definition.template;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Deal;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.deal.DealPreviewUpdatingBehavior;

abstract public class DealTemplatePanel extends Panel {

	private static final long serialVersionUID = -1923977191068407384L;
	protected DealPreview dealPreview;
	private HiddenField<String> title, summary;
	private TextArea<String> detailsField;

	public DealTemplatePanel(String id, DealPreview preview) {
		super(id);
		dealPreview = preview;
		setMarkupId(id);
		setOutputMarkupId(true);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		title = new HiddenField<String>("title");
		add(title.setOutputMarkupId(true));
		summary = new HiddenField<String>("summary");
		add(summary.setOutputMarkupId(true));

		detailsField = new TextArea<String>("details");
		detailsField.add(new DealPreviewUpdatingBehavior(dealPreview,
				DealPreviewUpdatingBehavior.DealComponent.DETAILS, "onChange"));
		add(detailsField);
		
		// TODO need to open the details fld by default if the user has defined more details.
		@SuppressWarnings("rawtypes")
		AjaxLink addDefaultConditions = new AjaxLink("addDefaultConditions") {

			private static final long serialVersionUID = -5901764992551535378L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				syncDetails(target, true);	
			}
			
		};
		add(addDefaultConditions);
		 
	}
	
	private void syncDetails(AjaxRequestTarget target, boolean useDefault) {
		detailsField.setModelValue(new String[]{cookUpDetails(useDefault)});
		dealPreview.details = detailsField.getValue();
		target.add(dealPreview.detailsLabel);
		target.add(detailsField);
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
	
	private String cookUpDetails(boolean useDefault) {
		StringBuilder sb = new StringBuilder();
		
		Deal deal = (Deal) getDefaultModelObject();
		String details = deal.geDetails();
		
		if (useDefault) 
		{
			sb.append("May not be combined with any other offer, discount or promotion. Not valid on holidays, and subject to rules of use.");
		}
		else if (!details.isEmpty())
		{
			sb.append(details);
		}
		
		return sb.toString();
	}
	
	abstract String cookUpTitle();
	abstract String cookUpSummary();

}
