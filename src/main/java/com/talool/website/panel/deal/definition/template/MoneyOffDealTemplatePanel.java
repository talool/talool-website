package com.talool.website.panel.deal.definition.template;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import com.talool.website.panel.deal.DealPreview;

public class MoneyOffDealTemplatePanel extends DealTemplatePanel {

	private static final long serialVersionUID = 4682200089094814554L;
	private String amount, entree;
	
	public MoneyOffDealTemplatePanel(String id, DealPreview preview) {
		super(id, preview);
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
				syncTitle(target);
			}
			
		});
		amount.setRequired(true);
		add(amount);
		
		TextField<String> entree = new TextField<String>("entree", new PropertyModel<String>(this,"entree"));
		entree.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long serialVersionUID = 1321114986782061545L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				syncSummary(target);
			}
			
		});
		entree.setRequired(true);
		add(entree);

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
	
	public String cookUpTitle() {
		StringBuilder sb = new StringBuilder("$");
		sb.append(getAmount()).append(" Off");
		return sb.toString();
	}
	
	public String cookUpSummary() {
		StringBuilder sb = new StringBuilder(cookUpTitle());
		sb.append(" 2 or more ").append(getEntree()).append(" entrees.");
		return sb.toString();
	}



}
