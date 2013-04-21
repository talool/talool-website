package com.talool.website.panel.merchant.definition.template;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Deal;
import com.talool.website.panel.merchant.definition.DealPreview;
import com.talool.website.panel.merchant.definition.DealPreviewUpdatingBehavior;

abstract public class DealTemplatePanel extends Panel {

	private static final long serialVersionUID = -1923977191068407384L;
	protected DealPreview dealPreview;
	private HiddenField<String> title, summary;
	private TextArea<String> detailsField;
	private Boolean valid;

	public DealTemplatePanel(String id, DealPreview preview, CompoundPropertyModel<Deal> model) {
		super(id);
		dealPreview = preview;
		setDefaultModel(model);
		setMarkupId(id);
		setOutputMarkupId(true);
		valid = false;
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		title = new HiddenField<String>("title");
		add(title.setOutputMarkupId(true));
		summary = new HiddenField<String>("summary");
		add(summary.setOutputMarkupId(true));
		
		final WebMarkupContainer addMoreConditionsContainer = new WebMarkupContainer("addMoreConditionsContainer");
		add(addMoreConditionsContainer.setOutputMarkupId(true));
		final WebMarkupContainer conditionsContainer = new WebMarkupContainer("conditionsContainer");
		add(conditionsContainer.setOutputMarkupId(true));
		
		// add checkbox for "valid anytime"
		final CheckBox validAnytime = new CheckBox("valid", new PropertyModel<Boolean>(this,"valid"));
		validAnytime.add(new AjaxFormComponentUpdatingBehavior("onChange"){

			private static final long serialVersionUID = 6020047659100023077L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				syncDetails(target);
			}
			
		});
		addMoreConditionsContainer.add(validAnytime);
		
		detailsField = new TextArea<String>("details");
		detailsField.add(new DealPreviewUpdatingBehavior(dealPreview,
				DealPreviewUpdatingBehavior.DealComponent.DETAILS, "onChange"));
		conditionsContainer.add(detailsField);
		
		// TODO need to open the details fld by default if the user has defined more details.
		@SuppressWarnings("rawtypes")
		AjaxLink showMoreConditions = new AjaxLink("addMoreConditions") {

			private static final long serialVersionUID = -5901764992551535378L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				syncDetails(target);
				addMoreConditionsContainer.setVisible(false);
				conditionsContainer.add(new AttributeAppender("class","show"));
				target.add(conditionsContainer);
				target.add(addMoreConditionsContainer);	
			}
			
		};
		addMoreConditionsContainer.add(showMoreConditions);
		 
	}
	
	
	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	
	private void syncDetails(AjaxRequestTarget target) {
		detailsField.setModelValue(new String[]{cookUpDetails()});
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
	
	private String cookUpDetails() {
		StringBuilder sb = new StringBuilder();
		if (valid) {
			sb.append("Valid anytime (excluding holidays)");
		}
		return sb.toString();
	}
	
	abstract String cookUpTitle();
	abstract String cookUpSummary();

}
