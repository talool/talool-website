package com.talool.website.panel.merchant.definition;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Deal;

public class MoneyOffDealTemplatePanel extends DealTemplatePanel {

	private static final long serialVersionUID = 4682200089094814554L;
	private String amount, entree;
	private FormComponent<String> title, summary;
	
	public MoneyOffDealTemplatePanel(String id, DealPreview preview, CompoundPropertyModel<Deal> model) {
		super(id, preview, model);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		TextField<String> amount = new TextField<String>("amount", new PropertyModel<String>(this,"amount"));
		amount.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long serialVersionUID = -4991792391118027313L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				title.setModelValue(new String[]{cookUpTitle()});
				dealPreview.title = title.getValue();
				target.add(dealPreview.titleLabel);
				target.add(title);
			}
			
		});
		amount.setRequired(true);
		add(amount);
		
		TextField<String> entree = new TextField<String>("entree", new PropertyModel<String>(this,"entree"));
		entree.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long serialVersionUID = 1321114986782061545L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				summary.setModelValue(new String[]{cookUpSummary()});
				dealPreview.summary = summary.getValue();
				target.add(dealPreview.summaryLabel);
				target.add(summary);
			}
			
		});
		entree.setRequired(true);
		add(entree);
		
		title = new HiddenField<String>("title");
		add(title.setOutputMarkupId(true));
		summary = new HiddenField<String>("summary");
		add(summary.setOutputMarkupId(true));
		 
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getEntree() {
		return entree;
	}

	public void setEntree(String entree) {
		this.entree = entree;
	}
	
	private String cookUpTitle() {
		StringBuilder sb = new StringBuilder("$");
		sb.append(getAmount()).append(" Off");
		return sb.toString();
	}
	
	private String cookUpSummary() {
		StringBuilder sb = new StringBuilder(cookUpTitle());
		sb.append(" 2 or more ").append(getEntree()).append(" entrees.");
		return sb.toString();
	}



}
