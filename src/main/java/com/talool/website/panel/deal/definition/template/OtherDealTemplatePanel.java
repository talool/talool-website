package com.talool.website.panel.deal.definition.template;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Deal;
import com.talool.website.panel.deal.DealPreview;

public class OtherDealTemplatePanel extends DealTemplatePanel {

	private static final long serialVersionUID = 4682200089094814554L;
	private String titleShim, summaryShim;
	
	public OtherDealTemplatePanel(String id, DealPreview preview, String title, String summary) {
		super(id, preview);
		titleShim = title;
		summaryShim = summary;
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		TextField<String> titleField = new TextField<String>("titleShim", new PropertyModel<String>(this,"titleShim"));
		titleField.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long serialVersionUID = 3469924425755938212L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				syncTitle(target);
			}
			
		});
		titleField.setRequired(true);
		add(titleField);
		
		TextArea<String> summaryField = new TextArea<String>("summaryShim", new PropertyModel<String>(this,"summaryShim"));
		summaryField.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long serialVersionUID = 8898671266627340754L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				syncSummary(target);
			}
			
		});
		summaryField.setRequired(true);
		add(summaryField);
		 
	}

	@Override
	String cookUpTitle() {
		return titleShim;
	}

	@Override
	String cookUpSummary() {
		return summaryShim;
	}

	public String getTitleShim() {
		return titleShim;
	}

	public void setTitleShim(String titleShim) {
		this.titleShim = titleShim;
	}

	public String getSummaryShim() {
		return summaryShim;
	}

	public void setSummaryShim(String summaryShim) {
		this.summaryShim = summaryShim;
	}



}
